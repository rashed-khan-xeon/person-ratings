package com.rashedkhan.ratings.data.model;

public class FeatureType {
    private int featureTypeId;
    private int userId;
    private String title;
    private int active;

    public FeatureType(int featureTypeId, int userId, String title, int active) {
        this.featureTypeId = featureTypeId;
        this.userId = userId;
        this.title = title;
        this.active = active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getFeatureTypeId() {
        return featureTypeId;
    }

    public String getTitle() {
        return title;
    }

    public boolean getActive() {
        return active == 1;
    }

    public int getUserId() {
        return userId;
    }
}
