package edu.school21.sockets.server;

import edu.school21.sockets.helpers.ClientHandler;
import edu.school21.sockets.services.MessageService;
import edu.school21.sockets.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;

@Component("Server")
public class Server {

    @Autowired
    @Qualifier("UsersServiceImpl")
    private UsersService usersService;

    @Autowired
    @Qualifier("MessageServiceImpl")
    private MessageService messageService;

    public Server() {
    }

    public Server(UsersService usersService, MessageService messageService) {
        this.usersService = usersService;
        this.messageService = messageService;
    }

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                ClientHandler clientHandler = new ClientHandler(serverSocket.accept(), usersService, messageService);
                clientHandler.start();
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
