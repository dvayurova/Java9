package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Message;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message>{

    List<Message> findByChatRoomId(Long id);
}
