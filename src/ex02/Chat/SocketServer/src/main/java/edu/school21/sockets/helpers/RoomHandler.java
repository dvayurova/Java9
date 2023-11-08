package edu.school21.sockets.helpers;

import edu.school21.sockets.models.ChatRoom;
import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.ChatRoomRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class RoomHandler {

    private PrintWriter out;
    private BufferedReader in;
    private ChatRoomRepository chatRoomRepository;

    public RoomHandler(PrintWriter out, BufferedReader in, ChatRoomRepository chatRoomRepository) {
        this.out = out;
        this.in = in;
        this.chatRoomRepository = chatRoomRepository;
    }

    public ChatRoom create(User user) throws IOException {
        out.println("Enter room name: ");
        String roomName = in.readLine();
        ChatRoom  room = new ChatRoom(roomName, user);
        chatRoomRepository.save(room);
        return room;
    }

    public ChatRoom chooseRoom(Long roomNumber) throws IOException {
        return chatRoomRepository.findById(roomNumber);
    }


}
