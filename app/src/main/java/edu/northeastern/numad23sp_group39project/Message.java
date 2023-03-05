package edu.northeastern.numad23sp_group39project;

public class Message {
    public String from;
    public String to;
    public String sticker;
    public String time;
    public Message() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public Message(String from, String to, String sticker, String time) {
        this.from = from;
        this.to = to;
        this.sticker = sticker;
        this.time = time;
    }
}
