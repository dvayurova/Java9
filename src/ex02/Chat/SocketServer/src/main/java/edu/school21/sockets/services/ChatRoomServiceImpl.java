//package edu.school21.sockets.services;
//
//import edu.school21.sockets.models.ChatRoom;
//import edu.school21.sockets.models.Message;
//import edu.school21.sockets.models.User;
//import edu.school21.sockets.repositories.ChatRoomRepository;
//import edu.school21.sockets.repositories.MessageRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//
//import java.util.List;
//
//public class ChatRoomServiceImpl implements ChatRoomService {
//
//    @Autowired
//    @Qualifier("ChatRoomRepositoryImpl")
//    private ChatRoomRepository chatRoomRepository;
//
//    @Autowired
//    @Qualifier("MessageRepositoryImpl")
//    private MessageRepository messageRepository;
//
//    @Autowired
//    @Qualifier("MessageServiceImpl")
//    private MessageService messageService;
//
//    public ChatRoomServiceImpl(ChatRoomRepository chatRoomRepository, MessageRepository messageRepository, MessageService messageService) {
//        this.chatRoomRepository = chatRoomRepository;
//        this.messageRepository = messageRepository;
//        this.messageService = messageService;
//    }
//
//    @Override
//    public void create(String name, User owner) {
//        chatRoomRepository.save(new ChatRoom(name, owner));
//    }
//
//    @Override
//    public List<Message> getChatRoomsMessages(Long id) {
//        return messageRepository.findByChatRoomId(id);
//    }
//
//    @Override
//    public void sendMessage(String text, Long userId, Long roomId) {
//
//    }
//}
