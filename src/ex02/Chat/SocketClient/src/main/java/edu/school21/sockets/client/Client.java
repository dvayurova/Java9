package edu.school21.sockets.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import com.google.gson.Gson;
import edu.school21.sockets.models.Message;


public class Client {

    public static void start(int port) throws IOException {
        try (Socket socket = new Socket("localhost", port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in))) {
            Gson gson = new Gson();
            String name = "";
            String fromServer;
            while (true) {
                fromServer = in.readLine();
                if(fromServer.startsWith("Hello from server!") || fromServer.startsWith("1.") || fromServer.startsWith("Rooms:")){
                    System.out.println(fromServer);
                    fromServer =  readInLoop(in);
                    if(fromServer == null) continue;
                } else{
                    System.out.println(fromServer);
                }
                if (fromServer.equals("You have successfully logged in")) {
                    fromServer = in.readLine();
                    if(fromServer.startsWith("Last chat name")){
                        readInLoop(in);
                    }
                    continue;
                }
                if (fromServer.equals("Start messaging")) break;
                if (fromServer.equals("Incorrect username or password")) System.exit(0);
                String consoleInput = consoleIn.readLine();
                if (consoleInput.equals("Exit")) System.exit(0);
                out.println(consoleInput);
                if (fromServer.equals("Enter username:")) {
                    name = consoleInput;
                }
            }

            new InputThread(name, in).start();

            String userInput;
            while ((userInput = consoleIn.readLine()) != null) {
                Message sms = new Message(name, userInput);
                if (userInput.equals("Exit")) {
                    out.println(gson.toJson(sms));
                    in.readLine();
                    System.exit(0);
                }
                out.println(gson.toJson(sms));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String readInLoop(BufferedReader in) throws IOException {
        String fromServer;
        while ((fromServer = in.readLine()) != null) {
            if (fromServer.isEmpty()) {
                break;
            }
            System.out.println(fromServer);
        }
        return fromServer;
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
