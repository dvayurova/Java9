package edu.school21.sockets.helpers;

import edu.school21.sockets.models.User;
import edu.school21.sockets.services.MessageService;
import edu.school21.sockets.services.UsersService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler extends Thread {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private static Map<Long, PrintWriter> clientWriters = new HashMap<>();
    private UsersService usersService;
    private MessageService messageService;

    public ClientHandler(Socket socket, UsersService usersService, MessageService messageService) {
        this.socket = socket;
        this.usersService = usersService;
        this.messageService = messageService;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println("Hello from server!");
            User user = null;
            Authorization authorization = new Authorization(out, in, usersService);
            while (user == null) {
                String input = in.readLine();
                if (input.equals("Exit")) {
                    out.println("You have left the chat.");
                    break;
                } else if(((user = authorization.authorise(input)) == null) && input.equals("signIn")){
                    break;
                }
            }
            if (user == null) return;
            synchronized (clientWriters) {
                clientWriters.put(user.getId(), out);
            }

            String message;
            while ((message = in.readLine()) != null) {
                if (message.equals("Exit")) {
                    out.println("You have left the chat.");
                    clientWriters.remove(out);
                    socket.close();
                    break;
                }
                messageService.send(message, user.getId());
                synchronized (clientWriters) {
                    for (Long key : clientWriters.keySet()) {
                        PrintWriter writer = clientWriters.get(key);
                        writer.println(message);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            synchronized (clientWriters) {
                clientWriters.remove(out);
            }
        }
    }
}