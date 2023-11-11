package edu.school21.sockets.repositories;

import edu.school21.sockets.models.ChatRoom;
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

@Component("ChatRoomRepositoryImpl")
public class ChatRoomRepositoryImpl implements ChatRoomRepository{

    @Autowired
    @Qualifier("hikariBean")
    private final DataSource dataSource;

    private final JdbcTemplate jdbcTemplate;



    public ChatRoomRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplate = new JdbcTemplate(dataSource);

    }

    @Override
    public ChatRoom findById(Long id) {
        return (ChatRoom) jdbcTemplate.queryForObject("SELECT * FROM chatroom WHERE id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(ChatRoom.class));
    }

    @Override
    public String findRoomNameById(Long id){
        return (String) jdbcTemplate.queryForObject("SELECT name FROM chatroom WHERE id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(String.class));
    }

    @Override
    public List<ChatRoom> findAll() {
        return jdbcTemplate.query("SELECT * FROM chatroom", new BeanPropertyRowMapper<>(ChatRoom.class));
    }

    @Override
    public void save(ChatRoom entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("insert into chatroom (name, ownerid) values (?, ?)", new String[]{"id"});
            ps.setString(1, entity.getName());
            ps.setLong(2, entity.getOwner().getId());
            return ps;
        }, keyHolder);
        Long id = keyHolder.getKey().longValue();
        entity.setId(id);
    }

    @Override
    public void update(ChatRoom entity) {
        jdbcTemplate.update("update chatroom set name = ?, ownerid = ? where id = ?", entity.getName(), entity.getOwner().getId(), entity.getId());

    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("delete from chatroom where id = ?", id);
    }
}
