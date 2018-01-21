package com.review.ratings.ui.home.history;

import com.review.ratings.core.RatingsApplication;
import com.review.ratings.core.ResponseListener;
import com.review.ratings.data.implementation.HttpRepository;
import com.review.ratings.data.model.UserRatings;
import com.review.ratings.data.repository.IHttpRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arifk on 12.1.18.
 */

public class RatingsPresenter implements HistoryContract.RatingsPresenter {
    private HistoryContract.RatingsView view;
    private IHttpRepository repository;

    public RatingsPresenter(HistoryContract.RatingsView view, HttpRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void getUserRatingsList(String url) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));
        repository.getAll(url, UserRatings[].class, header, new ResponseListener<List<UserRatings>>() {
            @Override
            public void success(List<UserRatings> response) {
                view.setUserRatingsView(response);
            }

            @Override
            public void error(Throwable error) {
                view.showErrorMessage(error.getMessage());
            }
        });
    }
}
