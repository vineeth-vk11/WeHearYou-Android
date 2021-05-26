package com.wehearyou.TherapiesUI.TherapyBookingUI;

import java.util.ArrayList;

public class TherapyModel {

    String therapyName, therapyCost, description;
    ArrayList<String> therapyImages;

    public TherapyModel() {
    }

    public String getTherapyName() {
        return therapyName;
    }

    public void setTherapyName(String therapyName) {
        this.therapyName = therapyName;
    }

    public String getTherapyCost() {
        return therapyCost;
    }

    public void setTherapyCost(String therapyCost) {
        this.therapyCost = therapyCost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getTherapyImages() {
        return therapyImages;
    }

    public void setTherapyImages(ArrayList<String> therapyImages) {
        this.therapyImages = therapyImages;
    }
}
