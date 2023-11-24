package edu.school21.sockets.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import com.google.gson.Gson;
import edu.school21.sockets.models.Message;


public class Client {

    private Socket socket;
    private  BufferedReader inputFromServer;
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
        } catch(IOException e){
            System.err.println(e.getLocalizedMessage());
            close();
        }
    }

    private void close() {
        try{
            if(!socket.isClosed()){
                inputFromServer.close();
                outToServer.close();
                socket.close();
                consoleIn.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() throws IOException {
        try {
            String fromServer;
            while (true) {
                fromServer = inputFromServer.readLine();
                if(fromServer.startsWith("Hello from server!") || fromServer.startsWith("1.") || fromServer.startsWith("Rooms:")){
                    System.out.println(fromServer);
                    fromServer =  readInLoop(inputFromServer);
                    if(fromServer == null) continue;
                } else{
                    System.out.println(fromServer);
                }
                if (fromServer.equals("You have successfully logged in")) {
                    fromServer = inputFromServer.readLine();
                    if(fromServer.startsWith("Last chat name")){
                        readInLoop(inputFromServer);
                    }
                    continue;
                }
                if (fromServer.contains("Start messaging")) break;
                if (fromServer.equals("Incorrect username or password")) System.exit(0);
                String consoleInput = consoleIn.readLine();
                if (consoleInput.equals("Exit")) System.exit(0);
                outToServer.println(consoleInput);
                if (fromServer.equals("Enter username:")) {
                    name = consoleInput;
                }
            }

            new InputThread(name, inputFromServer).start();

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
