package edu.school21.sockets.app;

import edu.school21.sockets.client.Client;

import java.io.IOException;
public class Main {
    public static void main(String[] args)  {
        if (args.length != 1 || !args[0].startsWith("--server-port=")) {
            System.err.println("Please enter a server port value in format \"--server-port=8081\"");
            System.exit(-1);
        }
        try {
            Client.start(Integer.parseInt(args[0].substring(args[0].indexOf('=') + 1)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}