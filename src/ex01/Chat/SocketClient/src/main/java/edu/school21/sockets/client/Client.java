package edu.school21.sockets.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {



    public static void start(int port) throws IOException {
        try (Socket socket = new Socket("localhost", port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in))) {


            String name = "";

            while (true) {
                String fromServer = in.readLine();
                System.out.println(fromServer);
                if (fromServer.equals("Start messaging")) break;
                String consoleInput = consoleIn.readLine();
                if (consoleInput.equals("Exit") || fromServer.equals("Incorrect username or password")) System.exit(0);
                out.println(consoleInput);
                if (fromServer.equals("Enter username:")) {
                    name = consoleInput;
                }
            }

            new InputThread(name, in).start();

            String userInput;
            while ((userInput = consoleIn.readLine()) != null) {
                if (userInput.equals("Exit")) {
                    out.println(userInput);
                    in.readLine();
                    System.exit(0);
                }
                out.println(name + ": " + userInput);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static class InputThread extends Thread {
        private String name;
        private BufferedReader in;

        public InputThread(String name, BufferedReader in) {
            this.name = name;
            this.in = in;
        }

        public void run() {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    String[] splitedMessage = message.split(": ");
                    String userName = splitedMessage[0];
                    if (!userName.equals(name)) {
                        System.out.println(message);
                    }


                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
