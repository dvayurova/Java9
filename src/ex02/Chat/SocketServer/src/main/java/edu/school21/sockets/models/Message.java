package edu.school21.sockets.models;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Message {

    private Long id;
    private User sender;
    private Long senderId = 0L;
    private Long roomId;
    private String text;
    private LocalDateTime time;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");

    public Message() {
    }

    public Message(User sender, String text, LocalDateTime time) {
        this.sender = sender;
        this.text = text;
        this.time = time;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        if(this.senderId.equals(0L)) {
            this.senderId = senderId;
        } else{
            this.senderId = sender.getId();
        }
    }

    public User getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return time + "\t" + sender.getEmail() + ": " + text;
    }
}
