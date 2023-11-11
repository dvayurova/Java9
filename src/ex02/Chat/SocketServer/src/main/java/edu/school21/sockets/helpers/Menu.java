package edu.school21.sockets.helpers;

import com.google.gson.Gson;
import edu.school21.sockets.models.ChatRoom;
import edu.school21.sockets.models.Message;
import edu.school21.sockets.repositories.ChatRoomRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Menu {
    private PrintWriter out;
    private BufferedReader in;
    private Map<Integer, String> menuItems;
    private Gson gson;



    public Menu(PrintWriter out, BufferedReader in, Gson gson) {
        this.out = out;
        this.in = in;
        this.gson = gson;
    }

    public String  authorizationMenu() throws IOException {
        menuItems = new HashMap<>();
        menuItems.put(1, "signIn");
        menuItems.put(2, "signUp");
        menuItems.put(3, "Exit");
        out.println("Hello from server!\n1. signIn\n" +
                "2. signUp\n" +
                "3. Exit\n");
        int choice = 0;
        try{
             choice = Integer.parseInt(in.readLine());
        } catch (NumberFormatException e){
            out.println("Error, please enter a number 1, 2 or 3");
            return null;
        }
        return menuItems.get(choice);
    }

    public String actionMenu() throws IOException {
        menuItems = new HashMap<>();
        menuItems.put(1, "Create room");
        menuItems.put(2, "Choose room");
        menuItems.put(3, "Exit");
        out.println("1.\tCreate room\n" +
                "2.\tChoose room\n" +
                "3.\tExit");
        int choice = 0;
        try{
            String jsonString = in.readLine();
            JsonMessage jsonMessage = gson.fromJson(jsonString, JsonMessage.class);
            choice = Integer.parseInt(jsonMessage.getText());
        } catch (NumberFormatException e){
            out.println("Error, please enter a number 1, 2 or 3");
            return null;
        }
        return menuItems.get(choice);
    }

    public Long chooseRoom(ChatRoomRepository chatRoomRepository) throws IOException {
        List<ChatRoom>  chatRooms = chatRoomRepository.findAll();
        Long lastId = 1L;
        out.println("Rooms:");
        for(ChatRoom chatRoom : chatRooms){
            out.println(chatRoom.getId() + ". " + chatRoom.getName());
            lastId = chatRoom.getId() + 1;
        }
        out.println(lastId + ". " + "Exit");
        Long choice = 0L;
        try{
            String jsonString = in.readLine();
            JsonMessage jsonMessage = gson.fromJson(jsonString, JsonMessage.class);
            choice = Long.parseLong(jsonMessage.getText());
            if(choice.equals(lastId)) {
                return 0L;
            }
        } catch (NumberFormatException e) {
            out.println("Error, please enter a number");
        }
        return choice;
    }

}
