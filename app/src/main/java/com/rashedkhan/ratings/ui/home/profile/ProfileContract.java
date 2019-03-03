package com.rashedkhan.ratings.ui.home.profile;

import com.rashedkhan.ratings.data.model.RatingSummary;
import com.rashedkhan.ratings.data.model.RatingsCategory;
import com.rashedkhan.ratings.data.model.User;
import com.rashedkhan.ratings.ui.home.common.BaseView;

import java.util.List;

public interface ProfileContract {
    interface ProfilePresenter {
        void getUserSelectedCategory(String url);

        void getUserDetails(String url);

        void getUserAvgRatingByCategory(String url);
    }

    interface ProfileView extends BaseView {
        void setUserDetailsToView(User user);

        void setUserSelectedCategory(List<RatingsCategory> ratingsCategories);

        void setUserAvgRatingsToView(List<RatingSummary> avgRating);
    }

    interface EditProfilePresenter {
        void updateUserProfile(String url, String body);

        void uploadPhoto(String url, String body);
    }

    interface EditProfileView extends BaseView {
        void profileUpdated(User user);
        void imageUploaded();

    }
}
