package com.wehearyou.JournalUI;

import java.util.Date;

public class JournalModel implements Comparable<JournalModel>{

    String chatId, date, listenerName, topic;
    Date dateD;

    public JournalModel() {
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getListenerName() {
        return listenerName;
    }

    public void setListenerName(String listenerName) {
        this.listenerName = listenerName;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Date getDateD() {
        return dateD;
    }

    public void setDateD(Date dateD) {
        this.dateD = dateD;
    }

    @Override
    public int compareTo(JournalModel journalModel) {
        return this.getDateD().compareTo(journalModel.dateD);
    }
}
