package com.rashedkhan.ratings.data.model;

import java.util.List;

public class FeatureType {
    private int featureTypeId;
    private int userId;
    private String title;
    private int active;
    private User user;
    private List<Feature> features;

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

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
