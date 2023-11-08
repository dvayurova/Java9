package edu.school21.sockets.server;

import edu.school21.sockets.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

@Component("Server")
public class Server {

    @Autowired
    @Qualifier("UsersServiceImpl")
    private UsersService usersService;

    private static Set<PrintWriter> clientWriters = new HashSet<>();

    public Server() {
    }

    public Server(UsersService usersService) {
        this.usersService = usersService;
    }

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                ClientHandler clientHandler = new ClientHandler(serverSocket.accept());
                clientHandler.start();
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                out.println("Hello from server!");
                boolean authorized = false;
                while (!authorized && socket.isConnected()) {
                    String input = in.readLine();
                    if (input.equals("Exit")) {
                        out.println("You have left the chat.");
                        break;
                    } else if (input.equals("signUp")) {
                        out.println("Enter username:");
                        String username = in.readLine();
                        out.println("Enter password:");
                        String password = in.readLine();
                        usersService.signUp(username, password);
                        out.println("Successful!");
                        out.println("Start messaging");
                        authorized = true;
                    } else if (input.equals("signIn")) {
                        out.println("Enter username:");
                        String username = in.readLine();
                        out.println("Enter password:");
                        String password = in.readLine();
                        if (usersService.signIn(username, password)) {
                            out.println("Start messaging");
                            authorized = true;
                        } else {
                            out.println("Incorrect username or password");
                            break;
                        }
                    }
                }
                if (!authorized) return;
                synchronized (clientWriters) {
                    clientWriters.add(out);
                }

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                    if (message.equals("Exit")) {
                        out.println("You have left the chat.");
                        clientWriters.remove(out);
                        socket.close();
                        break;
                    }
                    synchronized (clientWriters) {
                        for (PrintWriter writer : clientWriters) {
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


}
