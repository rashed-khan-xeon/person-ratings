package com.rashedkhan.ratings.data.model;


public class UserSetting {
    private int userId;
    private int emailVisible;
    private int phoneNumberVisible;
    private int addressVisible;
    private int hasRating;
    private int imageVisible;
    private int hasReview;

    public UserSetting() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean getEmailVisible() {
        return emailVisible == 1;
    }

    public void setEmailVisible(int emailVisible) {
        this.emailVisible = emailVisible;
    }

    public boolean getPhoneNumberVisible() {
        return phoneNumberVisible == 1;
    }

    public void setPhoneNumberVisible(int phoneNumberVisible) {
        this.phoneNumberVisible = phoneNumberVisible;
    }

    public boolean getAddressVisible() {
        return addressVisible == 1;
    }

    public void setAddressVisible(int addressVisible) {
        this.addressVisible = addressVisible;
    }


    public boolean getHasRating() {
        return hasRating == 1;
    }

    public void setHasRating(int hasRating) {
        this.hasRating = hasRating;
    }

    public boolean getImageVisible() {
        return imageVisible == 1;
    }

    public void setImageVisible(int imageVisible) {
        this.imageVisible = imageVisible;
    }

    public boolean getHasReview() {
        return hasReview == 1;
    }

    public void setHasReview(int hasReview) {
        this.hasReview = hasReview;
    }
}
