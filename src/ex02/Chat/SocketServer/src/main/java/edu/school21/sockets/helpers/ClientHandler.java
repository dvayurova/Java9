package edu.school21.sockets.helpers;

import com.google.gson.Gson;
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
import java.util.Objects;

public class ClientHandler extends Thread {
    private final Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private static Map<Client, PrintWriter> clientWriters = new HashMap<>();
    private final UsersService usersService;
    private final MessageService messageService;
    private final ChatRoomRepository chatRoomRepository;
    private User user = null;
    private Menu menu;
    private RoomHandler roomHandler;
    private ChatRoom room = null;
    private final Gson gson;

    public ClientHandler(Socket socket, UsersService usersService, MessageService messageService, ChatRoomRepository chatRoomRepository) {
        this.socket = socket;
        this.usersService = usersService;
        this.messageService = messageService;
        this.chatRoomRepository = chatRoomRepository;
        gson = new Gson();
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            menu = new Menu(out, in, gson);
            roomHandler = new RoomHandler(out, in, chatRoomRepository);
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
            close();
        }
    }

    public void run() {
        try {
            authorise();
            if (user == null) return;
            sendMessageList();
            chooseAction();
            synchronized (clientWriters) {
                clientWriters.put(new Client(user.getId(), room), out);
            }
            messageExchange();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
            synchronized (clientWriters) {
                clientWriters.remove(out);
            }
        }
    }

    private void close() {
        try {
            if (!socket.isClosed()) {
                in.close();
                out.close();
                socket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void sendMessageList() {
        List<Message> lastMessagesInRoom = messageService.findUsersLastRoomMessages(user.getId());
        if (lastMessagesInRoom != null) {
            out.println("Last chat name: " + chatRoomRepository.findRoomNameById(messageService.findUsersLastRoomId(user.getId())));
            int i = 0;
            for (Message m : lastMessagesInRoom) {
                out.println(m);
                i++;
                if(i == 30) break;
            }
        } else {
            out.println("Start messaging");
        }
    }

    private void authorise() throws IOException {
        Authorization authorization = new Authorization(out, in, usersService);
        while (user == null) {
            String choice = menu.authorizationMenu();
            if (choice == null) continue;
            if (choice.equals("Exit")) {
                out.println("You have left the chat.");
                break;
            } else if (((user = authorization.authorise(choice)) == null) && (choice.equals("signIn") || choice.equals("signUp"))) {
                break;
            }
        }
    }

    private void chooseAction() throws IOException {
        String action = menu.actionMenu();
        room = null;
        if (action.equals("Create room")) {
            room = roomHandler.create(user);
            out.println("Start messaging");
        } else if (action.equals("Choose room")) {
            Long chosenRoomNumber = menu.chooseRoom(chatRoomRepository);
            if (chosenRoomNumber.equals(0L)) return;
            room = roomHandler.chooseRoom(chosenRoomNumber);
            out.println("Start messaging");
        }
    }

    private void messageExchange() throws IOException {
        String jsonString;
        while ((jsonString = in.readLine()) != null) {
            JsonMessage jsonMessage = gson.fromJson(jsonString, JsonMessage.class);
            String message = jsonMessage.getText();
            String name = jsonMessage.getSender();
            if (message.equals("Exit")) {
                out.println("You have left the chat.");
                synchronized (clientWriters) {
                    clientWriters.remove(out);
                }
                socket.close();
                break;
            }
            messageService.send(message, user, room.getId());
            synchronized (clientWriters) {
                for (Client key : clientWriters.keySet()) {
                    PrintWriter writer = clientWriters.get(key);
                    if (room.getId().equals(key.getRoom().getId()))
                        writer.println(name + ": " + message);
                }
            }
        }
    }

    private class Client {
        private Long userId;
        private ChatRoom room;

        public Client(Long userId, ChatRoom room) {
            this.userId = userId;
            this.room = room;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Client client = (Client) o;
            return Objects.equals(userId, client.userId) && Objects.equals(room, client.room);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, room);
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public ChatRoom getRoom() {
            return room;
        }

        public void setRoom(ChatRoom room) {
            this.room = room;
        }
    }

}

