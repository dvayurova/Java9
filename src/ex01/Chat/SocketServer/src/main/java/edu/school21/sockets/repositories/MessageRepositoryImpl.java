package edu.school21.sockets.repositories;

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

@Component("MessageRepositoryImpl")
public class MessageRepositoryImpl implements MessageRepository{

    @Autowired
    @Qualifier("hikariBean")
    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;


    public MessageRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Message findById(Long id) {
        return (Message) jdbcTemplate.queryForObject("SELECT * FROM messages WHERE id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Message.class));

    }

    @Override
    public List<Message> findAll() {
        return jdbcTemplate.query("SELECT * FROM messages", new BeanPropertyRowMapper<>(Message.class));

    }

    @Override
    public void save(Message entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("insert into messages (sender, senderid, text) values (?, ?, ?)", new String[]{"id"});
            ps.setString(1, entity.getSender().getEmail());
            ps.setLong(2, entity.getSender().getId());
            ps.setString(3, entity.getText());
            return ps;
        }, keyHolder);
        Long id = keyHolder.getKey().longValue();
        entity.setId(id);
    }

    @Override
    public void update(Message entity) {
        jdbcTemplate.update("update messages set sender = ?, senderid = ?, text = ? where id = ?", entity.getSender().getEmail(), entity.getSender().getId(), entity.getText(), entity.getId());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("delete from messages where id = ?", id);
    }
}
