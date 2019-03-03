package com.rashedkhan.ratings.data.model;

import java.util.Date;


public class UserRatings {
    private int userRatingsId;
    private int userId;
    private User user;
    private int ratedByUserId;
    private User ratedByUser;
    private int ratingsCatId;
    private RatingsCategory ratingsCategory;
    private float ratings;
    private Date ratingsDate;

    public UserRatings() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getRatedByUser() {
        return ratedByUser;
    }

    public void setRatedByUser(User ratedByUser) {
        this.ratedByUser = ratedByUser;
    }

    public RatingsCategory getRatingsCategory() {
        return ratingsCategory;
    }

    public void setRatingsCategory(RatingsCategory ratingsCategory) {
        this.ratingsCategory = ratingsCategory;
    }

    public int getUserRatingsId() {
        return userRatingsId;
    }

    public void setUserRatingsId(int userRatingsId) {
        this.userRatingsId = userRatingsId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRatedByUserId() {
        return ratedByUserId;
    }

    public void setRatedByUserId(int ratedByUserId) {
        this.ratedByUserId = ratedByUserId;
    }

    public int getRatingsCatId() {
        return ratingsCatId;
    }

    public void setRatingsCatId(int ratingsCatId) {
        this.ratingsCatId = ratingsCatId;
    }

    public float getRatings() {
        return ratings;
    }

    public void setRatings(float ratings) {
        this.ratings = ratings;
    }

    public Date getRatingsDate() {
        return ratingsDate;
    }

    public void setRatingsDate(Date ratingsDate) {
        this.ratingsDate = ratingsDate;
    }
}
