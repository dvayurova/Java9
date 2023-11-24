package edu.school21.sockets.client;

import java.io.*;
import java.net.Socket;

import com.google.gson.Gson;
import edu.school21.sockets.models.Message;


public class Client {

    private Socket socket;
    private BufferedReader inputFromServer;
    private PrintWriter outToServer;
    private BufferedReader consoleIn;
    private String name = "";
    private Gson gson;


    public Client(String address, int port) {
        try {
            socket = new Socket(address, port);
            inputFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outToServer = new PrintWriter(socket.getOutputStream(), true);
            consoleIn = new BufferedReader(new InputStreamReader(System.in));
            gson = new Gson();
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
            close();
        }
    }

    public void start() throws IOException {
        try {
            String fromServer;
            authorise();
            fromServer = inputFromServer.readLine();
            if (fromServer.equals("Incorrect username or password")) System.exit(0);
            chooseAction(fromServer);
            getInRoom();
            new InputThread(name, inputFromServer).start();
            sendMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void close() {
        try {
            if (!socket.isClosed()) {
                inputFromServer.close();
                outToServer.close();
                socket.close();
                consoleIn.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private String clientInput() throws IOException {
        String consoleInput = consoleIn.readLine();
        if (consoleInput.equals("Exit")) System.exit(0);
        outToServer.println(consoleInput);
        return consoleInput;
    }

    private void authorise() throws IOException {
        String fromServer;
        while (!(fromServer = inputFromServer.readLine()).equals("You have successfully logged in")) {
            if (fromServer.startsWith("Hello from server!")) {
                System.out.println(fromServer);
                readInLoop(inputFromServer);
            } else {
                System.out.println(fromServer);
            }
            String consoleInput = clientInput();
            if (fromServer.equals("Enter username:")) {
                name = consoleInput;
            }
        }
    }

    private void chooseAction(String fromServer) throws IOException {
        if (fromServer.startsWith("Last chat name:")) {
            System.out.println(fromServer);
        }
        readInLoop(inputFromServer);
        clientInput();
    }

    private void getInRoom() throws IOException {
        String fromServer = inputFromServer.readLine();
        System.out.println(fromServer);
        if (fromServer.startsWith("Rooms:")) {
            System.out.println(fromServer);
            readInLoop(inputFromServer);
        }
        clientInput();
    }

    private void sendMessage() throws IOException {
        String userInput;
        while ((userInput = consoleIn.readLine()) != null) {
            Message sms = new Message(name, userInput);
            if (userInput.equals("Exit")) {
                outToServer.println(gson.toJson(sms));
                inputFromServer.readLine();
                System.exit(0);
            }
            outToServer.println(gson.toJson(sms));
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
