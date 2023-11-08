package edu.school21.sockets.helpers;

import edu.school21.sockets.models.ChatRoom;
import edu.school21.sockets.models.Message;
import edu.school21.sockets.repositories.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Component("Menu")
public class Menu {
    private PrintWriter out;
    private BufferedReader in;

    @Autowired
    @Qualifier("ChatRoomRepositoryImpl")
    private ChatRoomRepository chatRoomRepository;

    public Menu(PrintWriter out, BufferedReader in) {
        this.out = out;
        this.in = in;
    }

    public int enterMenu() throws IOException {
        out.println("Hello from server!\n1. signIn\n" +
                "2. SignUp\n" +
                "3. Exit");
        int choice = 0;
        try{
             choice = Integer.parseInt(in.readLine());
        } catch (NumberFormatException e){
            out.println("Error, please enter a number 1, 2 or 3");
        }
        return choice;
    }

    public int actionMenu() throws IOException {
        out.println("1.\tCreate room\n" +
                "2.\tChoose room\n" +
                "3.\tExit");
        int choice = 0;
        try{
            choice = Integer.parseInt(in.readLine());
        } catch (NumberFormatException e){
            out.println("Error, please enter a number 1, 2 or 3");
        }
        return choice;
    }

    public int chooseRoom(Long id) throws IOException {
        List<ChatRoom>  chatRooms = chatRoomRepository.findAll();
        Long lastId = 1L;
        out.println("Rooms:");
        for(ChatRoom chatRoom : chatRooms){
            out.println(chatRoom.getId() + ". " + chatRoom.getName());
            lastId = chatRoom.getId() + 1;
        }
        out.println(lastId + ". " + "Exit");
        int choice = 0;
        try{
            choice = Integer.parseInt(in.readLine());
        } catch (NumberFormatException e){
            out.println("Error, please enter a number");
        }
        return choice;
    }

}
