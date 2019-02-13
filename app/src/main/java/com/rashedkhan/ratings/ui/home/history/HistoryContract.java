package com.rashedkhan.ratings.ui.home.history;

import com.rashedkhan.ratings.data.model.UserRatings;
import com.rashedkhan.ratings.data.model.UserReview;
import com.rashedkhan.ratings.ui.home.common.BaseView;

import java.util.List;

/**
 * Created by arifk on 12.1.18.
 */

public interface HistoryContract {
    interface HistoryPresenter {

    }

    interface HistoryView extends BaseView {

    }

    interface RatingsPresenter {
        void getUserRatingsList(String url);
    }

    interface RatingsView extends BaseView {
        void setUserRatingsView(List<UserRatings> userRatings);
    }

    interface ReviewsPresenter {
        void getUserReviewList(String url);
    }

    interface ReviewView extends BaseView {
        void setUserReviewListToView(List<UserReview> userReviews);
    }
}