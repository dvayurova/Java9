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

@Component("Server")
public class Server {

    @Autowired
    @Qualifier("UsersServiceImpl")
    private UsersService usersService;

    public Server() {
    }

    public Server(UsersService usersService) {
        this.usersService = usersService;
    }

    public void start(int port) {

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("Hello from server!");
            String message;
            while ((message = in.readLine()) != null) {
                if (message.equals("signUp")) {
                    signUp(out, in);
                } else{
                    out.println("Please enter \"signUp\"");
                }
            }
            in.close();
            out.close();
            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void signUp(PrintWriter out, BufferedReader in) throws IOException {
        out.println("Enter username:");
        String username = in.readLine();
        out.println("Enter password:");
        String password = in.readLine();
        usersService.signUp(username, password);
        out.println("Successful!");
    }


}
