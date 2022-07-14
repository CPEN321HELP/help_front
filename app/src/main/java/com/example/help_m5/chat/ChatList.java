package com.example.help_m5.chat;

public class ChatList {

    private String id, name, message, date, time;

    public ChatList(String id, String name, String message, String date, String time) {
        this.id = id;
        this.name = name;
        this.message = message;
        this.date = date;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}

