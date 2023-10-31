package edu.school21.sockets.models;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Message {
    private User sender;
    private String text;
    private LocalDateTime time;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");

    public Message(User sender, String text, LocalDateTime time) {
        this.sender = sender;
        this.text = text;
        this.time = time;
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


}
