package com.rashedkhan.ratings.ui.home.search;

import com.rashedkhan.ratings.data.model.Feature;
import com.rashedkhan.ratings.data.model.FeatureType;
import com.rashedkhan.ratings.data.model.RatingSummary;
import com.rashedkhan.ratings.data.model.User;
import com.rashedkhan.ratings.ui.home.common.BaseView;

import java.util.List;

/**
 * Created by arifk on 5.1.18.
 */

public interface SearchContract {
    interface SearchPresenter {
        void searchUser(String url);

        void searchFeatureUser(String url);

        void getUserAvgRatingByCategory(String url);

        void getFeatureTypeListForUser(String url);

        void getFeatureListForUser(String allFeatureListForUser);
    }

    interface SearchView extends BaseView {
        void setUserListToView(List<User> userList);

        void setFeatureUserListToView(List<User> userList);

        void setFeaturesToView(List<Feature> features);

        void setFeatureTypeToView(List<FeatureType> featureTypes);

        void setUserAvgRatingsToView(List<RatingSummary> avgRating);

        void noUserRatings(String msg);

        void noUserFound(String msg);
    }
}
