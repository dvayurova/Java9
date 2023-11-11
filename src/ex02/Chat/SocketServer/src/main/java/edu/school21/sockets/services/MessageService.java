package edu.school21.sockets.services;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;

import java.util.List;

public interface MessageService {
    void send(String text, User user, Long chatRoomId);
    List<Message> findUsersLastRoomMessages(Long userId);
    Long findUsersLastRoomId(Long userId);
}
