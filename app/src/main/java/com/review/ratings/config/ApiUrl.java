package com.review.ratings.config;

/**
 * Created by arifk on 30.12.17.
 */

public class ApiUrl {
    private static ApiUrl ourInstance = null;

    public static ApiUrl getInstance() {
        if (ourInstance == null) {
            ourInstance = new ApiUrl();
        }
        return ourInstance;
    }

    private ApiUrl() {
    }

//    private final String BASE_URL = "http://ratings.rashedkhan.com/";
    private final String BASE_URL = "http://192.168.0.137/ratings_web/";

    public String getBASE_URL() {
        return BASE_URL;
    }

    public String getUserLoginUrl() {
        return BASE_URL + "api/v1/user-login";
    }

    public String getUserSignUpUrl() {
        return BASE_URL + "api/v1/user-signup";
    }

    public String getUserUpdateUrl() {
        return BASE_URL + "api/v1/user-add-or-update";
    }

    public String getUserTypesUrl() {
        return BASE_URL + "api/v1/user-types";
    }

    public String getUserDetailsUrl(int userId) {
        return BASE_URL + "api/v1/user-details?userId=" + userId;
    }

    public String getUpdateUserSettingsUrl() {
        return BASE_URL + "api/v1/user-settings-update";
    }

    public String getSearchUserUrl(String queryString) {
        return BASE_URL + "api/v1/search-users?keyWord=" + queryString;
    }

    public String getUserRatingsCategoryUrl(int userId) {
        return BASE_URL + "api/v1/user-ratings-category?userId=" + userId;
    }

    public String getAddUserRatingsUrl() {
        return BASE_URL + "api/v1/user-ratings-add-or-update";
    }

    public String getAddUserReviewUrl() {
        return BASE_URL + "api/v1/add-or-update-user-review";
    }

    public String getAvgRatingUrl(int userId) {
        return BASE_URL + "api/v1/user-average-ratings-by-cat?userId=" + userId;
    }

    public String getCategoriesByUserTypeIdUrl(int userTypeId) {
        return BASE_URL + "api/v1/categories-by-user-type-id?userTypeId=" + userTypeId;
    }

    public String getAddRatingsCategoriesUrl() {
        return BASE_URL + "api/v1/ratings-category-add-or-update";
    }

    public String getUserReviewList(int userId, int skip, int top) {
        return BASE_URL + "api/v1/user-reviews-by-user-id?userId=" + userId + "&skip=" + skip + "&top=" + top;
    }

    public String getUserRatingsList(int userId, int skip, int top) {
        return BASE_URL + "api/v1/user-all-ratings?userId=" + userId + "&skip=" + skip + "&top=" + top;
    }

    public String getUserImageUploadUrl() {
        return BASE_URL + "api/v1/user-upload-image";
    }

    public String getUserImageUrl(String image) {
        return BASE_URL + "image/" + image;
    }

}
