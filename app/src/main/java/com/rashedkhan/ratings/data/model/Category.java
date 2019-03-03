package com.rashedkhan.ratings.data.model;


public class Category {
    private int catId;
    private String name;
    private int userTypeId;
    private UserType userType;
    private int userId;
    private User user;
    private int active;
    private int isDefault;

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Category() {
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(int userTypeId) {
        this.userTypeId = userTypeId;
    }

    public boolean getActive() {
        return active == 1;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean getIsDefault() {
        return isDefault==1;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }
}
