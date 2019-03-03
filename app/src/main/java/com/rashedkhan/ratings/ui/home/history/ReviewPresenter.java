package com.rashedkhan.ratings.ui.home.history;

import com.android.volley.VolleyError;
import com.rashedkhan.ratings.core.RatingsApplication;
import com.rashedkhan.ratings.core.ResponseListener;
import com.rashedkhan.ratings.data.implementation.HttpRepository;
import com.rashedkhan.ratings.data.model.UserReview;
import com.rashedkhan.ratings.data.repository.IHttpRepository;
import com.rashedkhan.ratings.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
