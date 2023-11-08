package edu.school21.sockets.services;

import edu.school21.sockets.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("MessageServiceImpl")
public class MessageServiceImpl implements MessageService{
    @Autowired
    @Qualifier("MessageRepositoryImpl")
    private MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void send(String message) {

    }
}
