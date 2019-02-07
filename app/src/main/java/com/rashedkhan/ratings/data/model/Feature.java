package com.rashedkhan.ratings.data.model;

import java.util.List;

public class Feature {
    private int featureId;
    private String title;
    private int createdUserId;
    private int active;
    private List<User> users;

    public int getFeatureId() {
        return featureId;
    }

    public void setFeatureId(int featureId) {
        this.featureId = featureId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(int createdUserId) {
        this.createdUserId = createdUserId;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public boolean getActive() {
        return active == 1;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
