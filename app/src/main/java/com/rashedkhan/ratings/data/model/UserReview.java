package com.rashedkhan.ratings.data.model;

import java.util.Date;



public class UserReview {
    private int userReviewId;
    private int userId;
    private int reviewedByUserId;
    private int ratingsCatId;;
    private String comments;
    private int isApproved;
    private Date reviewDate;
    private User user;
    private User reviewedByUser;
    private RatingsCategory ratingsCategory;

    public UserReview() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getReviewedByUser() {
        return reviewedByUser;
    }

    public void setReviewedByUser(User reviewedByUser) {
        this.reviewedByUser = reviewedByUser;
    }

    public RatingsCategory getRatingsCategory() {
        return ratingsCategory;
    }

    public void setRatingsCategory(RatingsCategory ratingsCategory) {
        this.ratingsCategory = ratingsCategory;
    }


    public int getUserReviewId() {
        return userReviewId;
    }

    public void setUserReviewId(int userReviewId) {
        this.userReviewId = userReviewId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getReviewedByUserId() {
        return reviewedByUserId;
    }

    public void setReviewedByUserId(int reviewedByUserId) {
        this.reviewedByUserId = reviewedByUserId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isApproved() {
        return isApproved == 1;
    }

    public void setIsApproved(int isApproved) {
        this.isApproved = isApproved;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public int getRatingsCatId() {
        return ratingsCatId;
    }

    public void setRatingsCatId(int ratingsCatId) {
        this.ratingsCatId = ratingsCatId;
    }
}