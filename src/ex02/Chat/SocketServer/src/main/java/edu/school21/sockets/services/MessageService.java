package edu.school21.sockets.services;

import edu.school21.sockets.models.User;

public interface MessageService {
    void send(String text, User user, Long chatRoomId);
}
