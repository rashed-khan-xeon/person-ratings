package com.review.test.ui.home.justify;

import com.review.test.core.RatingsApplication;
import com.review.test.core.ResponseListener;
import com.review.test.data.implementation.HttpRepository;
import com.review.test.data.model.RatingsCategory;
import com.review.test.data.repository.IHttpRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arifk on 6.1.18.
 */

public class JustifyPresenter implements JustifyContract.JustifyPresenter {
    private JustifyContract.JustifyView view;
    private IHttpRepository repository;

    public JustifyPresenter(JustifyContract.JustifyView view, HttpRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void getUserSelectedCategory(String url) {
        Map<String, String> header = new HashMap<>();
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));
        repository.getAll(url, RatingsCategory[].class, header, new ResponseListener<List<RatingsCategory>>() {
            @Override
            public void success(List<RatingsCategory> response) {
                view.setUserSelectedCategoryToView(response);
            }

            @Override
            public void error(Throwable error) {
                view.showErrorMessage(error.getMessage());
            }
        });
    }

    @Override
    public void submitRatings(String url, String body) {
        Map<String, String> header = new HashMap<>();
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));
        repository.post(url, Object.class, body, header, new ResponseListener<Object>() {
            @Override
            public void success(Object response) {
                view.ratingsAdded(response.toString());
            }

            @Override
            public void error(Throwable error) {
                view.showErrorMessage(error.getMessage());
            }
        });
    }

    @Override
    public void submitReview(String url, String body) {
        Map<String, String> header = new HashMap<>();
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));
        repository.post(url, Object.class, body, header, new ResponseListener<Object>() {
            @Override
            public void success(Object response) {
                view.reviewAdded(response.toString());
            }

            @Override
            public void error(Throwable error) {
                view.showErrorMessage(error.getMessage());
            }
        });
    }
}
