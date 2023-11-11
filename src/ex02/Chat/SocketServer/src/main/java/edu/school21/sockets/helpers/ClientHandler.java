package edu.school21.sockets.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.school21.sockets.models.ChatRoom;
import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.ChatRoomRepository;
import edu.school21.sockets.services.MessageService;
import edu.school21.sockets.services.UsersService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientHandler extends Thread {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private static Map<Long, PrintWriter> clientWriters = new HashMap<>();
    private UsersService usersService;
    private MessageService messageService;
    private ChatRoomRepository chatRoomRepository;

    public ClientHandler(Socket socket, UsersService usersService, MessageService messageService, ChatRoomRepository chatRoomRepository) {
        this.socket = socket;
        this.usersService = usersService;
        this.messageService = messageService;
        this.chatRoomRepository = chatRoomRepository;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            User user = null;
            Authorization authorization = new Authorization(out, in, usersService);
            Gson gson = new Gson();
            Menu menu = new Menu(out, in, gson);
            RoomHandler roomHandler = new RoomHandler(out, in, chatRoomRepository);
            while (user == null) {
                String choice = menu.authorizationMenu();
                if(choice == null) continue;
                if (choice.equals("Exit")) {
                    out.println("You have left the chat.");
                    break;
                } else if (((user = authorization.authorise(choice)) == null) && choice.equals("signIn")) {
                    break;
                }
            }

            if (user == null) return;
            List<Message> lastMessagesInRoom =  messageService.findUsersLastRoomMessages(user.getId());
            if(lastMessagesInRoom != null){
                out.println("Last chat name: " + chatRoomRepository.findRoomNameById(messageService.findUsersLastRoomId(user.getId())));
                for(Message m : lastMessagesInRoom){
                    out.println(m);
                }
            } else{
                out.println("Start messaging");
            }

            String action =  menu.actionMenu();
            ChatRoom room = null;
            if(action.equals("Create room")) {
                room =  roomHandler.create(user);
            } else if(action.equals("Choose room")){
                Long chosenRoomNumber = menu.chooseRoom(chatRoomRepository);
                if(chosenRoomNumber.equals(0L)) return;
                room =  roomHandler.chooseRoom(chosenRoomNumber);
                out.println("Start messaging");
            }

            synchronized (clientWriters) {
                clientWriters.put(user.getId(), out);
            }

            String jsonString;
            while ((jsonString = in.readLine()) != null) {
                JsonMessage jsonMessage = gson.fromJson(jsonString, JsonMessage.class);
                String message = jsonMessage.getText();
                String name = jsonMessage.getSender();
                if (message.equals("Exit")) {
                    out.println("You have left the chat.");
                    clientWriters.remove(out);
                    socket.close();
                    break;
                }
                messageService.send(message, user, room.getId());
                synchronized (clientWriters) {
                    for (Long key : clientWriters.keySet()) {
                        PrintWriter writer = clientWriters.get(key);
                        writer.println(name + ": " + message);
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