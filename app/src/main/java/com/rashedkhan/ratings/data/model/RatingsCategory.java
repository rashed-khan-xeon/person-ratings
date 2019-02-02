package com.rashedkhan.ratings.data.model;

/**
 * Created by arifk on 30.12.17.
 */

public class RatingsCategory {
    private int ratingsCatId;
    private int catId;
    private Category category;
    private int userId;
    private User user;
    private int active;

    public RatingsCategory() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getRatingsCatId() {
        return ratingsCatId;
    }

    public void setRatingsCatId(int ratingsCatId) {
        this.ratingsCatId = ratingsCatId;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
