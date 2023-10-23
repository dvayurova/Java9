package edu.school21.sockets.server;

import edu.school21.sockets.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    @Autowired
    @Qualifier("UsersServiceImpl")
    private UsersService usersService;

    public Server() {
    }

    public Server(UsersService usersService) {
        this.usersService = usersService;
    }

    public void start(int port){

    try{
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Hello from Server!");
        Socket socket = serverSocket.accept();

    } catch (IOException e) {
        throw new RuntimeException(e);
    }

}


}
