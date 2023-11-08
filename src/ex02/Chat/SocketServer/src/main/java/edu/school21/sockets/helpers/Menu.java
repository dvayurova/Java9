package edu.school21.sockets.helpers;

import edu.school21.sockets.models.ChatRoom;
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



    public Menu(PrintWriter out, BufferedReader in) {
        this.out = out;
        this.in = in;
    }

    public String  authorizationMenu() throws IOException {
        menuItems = new HashMap<>();
        menuItems.put(1, "signIn");
        menuItems.put(2, "signUp");
        menuItems.put(3, "Exit");
        out.println("Hello from server! 1. signIn " +
                "2. signUp " +
                "3. Exit");
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
        out.println("1.\tCreate room" +
                "2.\tChoose room" +
                "3.\tExit");
        int choice = 0;
        try{
            String inp = in.readLine();
            System.out.printf(inp);
            choice = Integer.parseInt(inp);
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
            choice = Long.parseLong(in.readLine());
            if(choice.equals(lastId)) {
                return 0L;
            }
        } catch (NumberFormatException e) {
            out.println("Error, please enter a number");
        }
        return choice;
    }

}
