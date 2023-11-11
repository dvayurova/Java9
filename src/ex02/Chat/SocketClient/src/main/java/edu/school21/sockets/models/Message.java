package edu.school21.sockets.models;
import com.google.gson.annotations.Expose;


public class Message {

    @Expose
    private String sender;

    @Expose
    private String text;

    public Message() {
    }

    public Message(String sender, String text) {
        this.sender = sender;
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
