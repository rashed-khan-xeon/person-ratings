package com.review.ratings.ui.home.history;

import com.android.volley.VolleyError;
import com.review.ratings.core.RatingsApplication;
import com.review.ratings.core.ResponseListener;
import com.review.ratings.data.implementation.HttpRepository;
import com.review.ratings.data.model.UserReview;
import com.review.ratings.data.repository.IHttpRepository;
import com.review.ratings.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arifk on 12.1.18.
 */

public class ReviewPresenter implements HistoryContract.ReviewsPresenter {

    private HistoryContract.ReviewView view;
    private IHttpRepository repository;

    public ReviewPresenter(HistoryContract.ReviewView view, HttpRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void getUserReviewList(String url) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));

        repository.getAll(url, UserReview[].class, header, new ResponseListener<List<UserReview>>() {
            @Override
            public void success(List<UserReview> response) {
                view.setUserReviewListToView(response);
            }

            @Override
            public void error(Throwable error) {
                view.showErrorMessage(Util.get().getMessage((VolleyError) error));
            }
        });
    }
}
