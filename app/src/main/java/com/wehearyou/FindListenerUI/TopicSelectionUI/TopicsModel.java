package com.wehearyou.FindListenerUI.TopicSelectionUI;

import java.util.Comparator;

public class TopicsModel implements Comparator<TopicsModel> {

    String topicName, topicTalkingNumber;
    int number;

    public TopicsModel() {
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicTalkingNumber() {
        return topicTalkingNumber;
    }

    public void setTopicTalkingNumber(String topicTalkingNumber) {
        this.topicTalkingNumber = topicTalkingNumber;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public int compare(TopicsModel topicsModel, TopicsModel t1) {
        return topicsModel.getNumber() - t1.getNumber();
    }
}
