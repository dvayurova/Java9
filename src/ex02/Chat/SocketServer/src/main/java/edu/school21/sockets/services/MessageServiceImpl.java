package edu.school21.sockets.services;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component("MessageServiceImpl")
public class MessageServiceImpl implements MessageService{
    @Autowired
    @Qualifier("MessageRepositoryImpl")
    private MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void send(String text, User user, Long chatRoomId) {
        Message message = new Message();
        message.setSender(user);
        message.setText(text);
        message.setRoomId(chatRoomId);
        messageRepository.save(message);
    }

    @Override
    public List<Message> findUsersLastRoomMessages(Long userId){
        Optional<Long> lastRoomIdOptional = messageRepository.findUsersLastRoomId(userId);
        if(lastRoomIdOptional.isEmpty()) return null;
        return messageRepository.findLastMessagesInRoom(lastRoomIdOptional.get());
    }

    @Override
    public Long findUsersLastRoomId(Long userId){
        return messageRepository.findUsersLastRoomId(userId).get();
    }

}
