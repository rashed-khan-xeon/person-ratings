package com.rashedkhan.ratings.data.model;

/**
 * Created by arifk on 8.1.18.
 */

public class RatingSummary {
    private float ratingsSummary;
    private String category;
    private int count;

    public float getAvgRatings() {
        return ratingsSummary;
    }

    public void setAvgRatings(float avgRatings) {
        this.ratingsSummary = avgRatings;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
