package com.review.test.data.model;

/**
 * Created by arifk on 30.12.17.
 */

public class Category {
    private int catId;
    private String name;
    private int userTypeId;
    private UserType userType;
    private int active;

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
}
