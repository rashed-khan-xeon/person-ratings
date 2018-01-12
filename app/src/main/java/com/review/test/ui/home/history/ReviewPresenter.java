package com.review.test.ui.home.history;

import com.review.test.core.RatingsApplication;
import com.review.test.core.ResponseListener;
import com.review.test.data.implementation.HttpRepository;
import com.review.test.data.model.UserReview;
import com.review.test.data.repository.IHttpRepository;

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
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));

        repository.getAll(url, UserReview[].class, header, new ResponseListener<List<UserReview>>() {
            @Override
            public void success(List<UserReview> response) {
                view.setUserReviewListToView(response);
            }

            @Override
            public void error(Throwable error) {
                view.showErrorMessage(error.getMessage());
            }
        });
    }
}