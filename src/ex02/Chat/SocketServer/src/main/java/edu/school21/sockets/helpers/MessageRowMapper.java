package edu.school21.sockets.helpers;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.repositories.UsersRepository;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageRowMapper implements RowMapper<Message> {

    private UsersRepository userRepository;

    public MessageRowMapper(UsersRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Message mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Message message = new Message();
        message.setId(resultSet.getLong("id"));
        message.setSenderId(resultSet.getLong("senderId"));
        message.setSender(userRepository.findById(message.getSenderId()));
        message.setRoomId(resultSet.getLong("roomid"));
        message.setText(resultSet.getString("text"));
        message.setTime(resultSet.getTimestamp("time").toLocalDateTime());
        return message;
    }
}
