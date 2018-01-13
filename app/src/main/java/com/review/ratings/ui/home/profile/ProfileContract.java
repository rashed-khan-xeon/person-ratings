package com.review.ratings.ui.home.profile;

import com.review.ratings.data.model.RatingSummary;
import com.review.ratings.data.model.RatingsCategory;
import com.review.ratings.data.model.User;
import com.review.ratings.ui.home.common.BaseView;

import java.util.List;

/**
 * Created by arifk on 3.1.18.
 */

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
    }

    interface EditProfileView extends BaseView {
        void profileUpdated(User user);
    }
}
