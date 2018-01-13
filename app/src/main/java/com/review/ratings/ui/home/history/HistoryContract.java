package com.review.ratings.ui.home.history;

import com.review.ratings.data.model.UserRatings;
import com.review.ratings.data.model.UserReview;
import com.review.ratings.ui.home.common.BaseView;

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
