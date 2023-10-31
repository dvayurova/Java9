package edu.school21.sockets.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void start(int port) throws IOException {
        Socket socket = new Socket("localhost", port);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(inFromServer.readLine());
        out.println(consoleIn.readLine());
        while (socket.isConnected()) {
            String serverMessage = inFromServer.readLine();
            if(serverMessage.equals("Incorrect username or password")) {
                break;
            }
            System.out.println(serverMessage);
            out.println(consoleIn.readLine());
        }
        inFromServer.close();
        out.close();
        socket.close();

    }
}
