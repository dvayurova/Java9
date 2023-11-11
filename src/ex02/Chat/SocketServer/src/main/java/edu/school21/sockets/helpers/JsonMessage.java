package edu.school21.sockets.helpers;

import com.google.gson.annotations.Expose;


public class JsonMessage {

    @Expose
    private String sender;

    @Expose
    private String text;

    public JsonMessage() {
    }

    public JsonMessage(String sender, String text) {
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
