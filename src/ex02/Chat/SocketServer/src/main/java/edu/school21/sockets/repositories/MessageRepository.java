package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Message;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends CrudRepository<Message>{

    List<Message> findByChatRoomId(Long id);
    Optional<Long> findUsersLastRoomId(Long userId);
    List<Message> findLastMessagesInRoom(Long roomId);
}
