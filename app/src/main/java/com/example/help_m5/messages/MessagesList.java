package com.example.help_m5.messages;

public class MessagesList {

    private String name, id, lastMessage, profilePic, chatKey;

    private int unseenMessages;

    public MessagesList(String name, String id, String lastMessage, String profilePic, int unseenMessages, String chatKey) {
        this.name = name;
        this.id = id;
        this.lastMessage = lastMessage;
        this.profilePic = profilePic;
        this.unseenMessages = unseenMessages;
        this.chatKey = chatKey;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public int getUnseenMessages() {
        return unseenMessages;
    }

    public String getChatKey() {
        return chatKey;
    }
}
