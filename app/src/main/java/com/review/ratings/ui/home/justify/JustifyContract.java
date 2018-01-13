package com.review.ratings.ui.home.justify;

import com.review.ratings.data.model.RatingsCategory;
import com.review.ratings.ui.home.common.BaseView;

import java.util.List;

/**
 * Created by arifk on 6.1.18.
 */

public interface JustifyContract {
    interface JustifyPresenter {
        void getUserSelectedCategory(String url);

        void submitRatings(String url, String body);

        void submitReview(String url, String body);
    }

    interface JustifyView extends BaseView {
        void setUserSelectedCategoryToView(List<RatingsCategory> ratingsCategories);

        void ratingsAdded(String msg);

        void reviewAdded(String msg);
    }
}
