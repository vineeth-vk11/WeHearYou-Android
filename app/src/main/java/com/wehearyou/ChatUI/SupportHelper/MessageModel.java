package com.wehearyou.ChatUI.SupportHelper;

public class MessageModel {
    String message;
    String sentUser;
    String receivedUser;

    public MessageModel() {
    }

    public MessageModel(String message, String sentUser, String receivedUser) {
        this.message = message;
        this.sentUser = sentUser;
        this.receivedUser = receivedUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSentUser() {
        return sentUser;
    }

    public void setSentUser(String sentUser) {
        this.sentUser = sentUser;
    }

    public String getReceivedUser() {
        return receivedUser;
    }

    public void setReceivedUser(String receivedUser) {
        this.receivedUser = receivedUser;
    }
}