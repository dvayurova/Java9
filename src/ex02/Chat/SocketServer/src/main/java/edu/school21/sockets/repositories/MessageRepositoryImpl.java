package edu.school21.sockets.repositories;

import edu.school21.sockets.helpers.MessageRowMapper;
import edu.school21.sockets.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Component("MessageRepositoryImpl")
public class MessageRepositoryImpl implements MessageRepository{

    @Autowired
    @Qualifier("hikariBean")
    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    private UsersRepository usersRepository;


    public MessageRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplate = new JdbcTemplate(dataSource);
        usersRepository = new UsersRepositoryImpl(dataSource);
    }

    @Override
    public Message findById(Long id) {
        return (Message) jdbcTemplate.queryForObject("SELECT * FROM messages WHERE id = ?", new Object[]{id}, new MessageRowMapper(usersRepository));

    }

    @Override
    public List<Message> findAll() {
        return jdbcTemplate.query("SELECT * FROM messages", new MessageRowMapper(usersRepository));
    }

    @Override
    public void save(Message entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("insert into messages (sender, senderid, roomid, text) values (?, ?, ?, ?)", new String[]{"id"});
            ps.setString(1, entity.getSender().getEmail());
            ps.setLong(2, entity.getSender().getId());
            ps.setLong(3, entity.getRoomId());
            ps.setString(4, entity.getText());
            return ps;
        }, keyHolder);
        Long id = keyHolder.getKey().longValue();
        entity.setId(id);
    }

    @Override
    public void update(Message entity) {
        jdbcTemplate.update("update messages set sender = ?, senderid = ?, roomid = ?, text = ? where id = ?", entity.getSender().getEmail(), entity.getSender().getId(), entity.getRoomId(), entity.getText(), entity.getId());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("delete from messages where id = ?", id);
    }

    @Override
    public List<Message> findByChatRoomId(Long id){
        return jdbcTemplate.query("SELECT * FROM messages WHERE roomid = ?", new Object[]{id}, new MessageRowMapper(usersRepository));
    }

//    @Override
//    public Optional<Long> findUsersLastRoomId(Long userId){
//        return (Optional.ofNullable(jdbcTemplate.queryForObject("SELECT roomid FROM messages WHERE senderid = ? ORDER BY time DESC LIMIT 1", new Object[]{userId}, Long.class)));
//    }

    @Override
    public Optional<Long> findUsersLastRoomId(Long userId) {
        List<Long> result = jdbcTemplate.query(
                "SELECT roomid FROM messages WHERE senderid = ? ORDER BY time DESC LIMIT 1",
                new Object[]{userId},
                (resultSet, rowNum) -> resultSet.getLong("roomid")
        );

        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public List<Message> findLastMessagesInRoom(Long roomId){
        return jdbcTemplate.query("SELECT * FROM messages WHERE roomid = ? ORDER BY id ASC LIMIT 30", new Object[]{roomId}, new MessageRowMapper(usersRepository));
    }
}
