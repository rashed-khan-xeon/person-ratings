package com.rashedkhan.ratings.config;

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

    private final String BASE_URL = "http://192.168.0.12/ratings/";
//    private final String BASE_URL = "http://ratings.rashedkhan.com/";

    public String getBASE_URL() {
        return BASE_URL;
    }

    public String getUserLoginUrl() {
        return BASE_URL + "api/v1/user-login";
    }

    public String getSendCodeUrl(int userId) {
        return BASE_URL + "api/v1/send-code?userId=" + userId;
    }

    public String getCheckCodeUrl() {
        return BASE_URL + "api/v1/check-code";
    }

    public String getChangePasswordUrl() {
        return BASE_URL + "api/v1/change-password";
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

    public String getAddCategoryUrl() {
        return BASE_URL + "api/v1/add-or-update-category";
    }

    public String getCategoriesByUserTypeIdUrl(int userTypeId) {
        return BASE_URL + "api/v1/categories-by-user-type-id?userTypeId=" + userTypeId;
    }

    public String getCategoriesByUserIdUrl(int userId) {
        return BASE_URL + "api/v1/categories-by-user-id?userId=" + userId;
    }

    public String getDefaultCategoriesByUrl(int userId) {
        return BASE_URL + "api/v1/default-categories?userId=" + userId;
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

    public String createFeature() {
        return BASE_URL + "api/v1/create-feature";
    }

    public String crateFeatureUser() {
        return BASE_URL + "api/v1/create-feature-user";
    }

    public String getFeatureList(int userId) {
        return BASE_URL + "api/v1/get-feature-list?userId=" + userId;
    }

    public String getFeatureListByTypeId(int typeId) {
        return BASE_URL + "api/v1/get-feature-list-by-type?featureTypeId=" + typeId;
    }

    public String getActiveFeatureList(int userId) {
        return BASE_URL + "api/v1/get-active-feature-list?userId=" + userId;
    }

    public String getUpdateFeatureUser() {
        return BASE_URL + "api/v1/update-feature-user";
    }

    public String getFeatureWiseAssignList(int featureId) {
        return BASE_URL + "api/v1/get-users-by-featureId?featureId=" + featureId;
    }

    public String getUserImageUploadUrl() {
        return BASE_URL + "api/v1/user-upload-image";
    }

    public String getUserImageUrl(String image) {
        return BASE_URL + "image/" + image;
    }

    public String getRatingsShareLink() {
        return "https://play.google.com/store/apps/details?id=com.rashedkhan.ratings";
    }


    public String getAllCategoriesByUserIdUrl(int userId) {
        return BASE_URL + "api/v1/all-categories-by-user-id?userId=" + userId;
    }

    public String getAllFeatureListForUser() {
        return BASE_URL + "api/v1/all-featurelist-for-user";
    }

    public String createFeatureType() {
        return BASE_URL + "api/v1/create-feature-type";

    }

    public String getFeatureTypes(int userId) {
        return BASE_URL + "api/v1/feature-type-list?userId=" + userId;
    }
}