package com.review.ratings.ui.home.justify;

import com.android.volley.VolleyError;
import com.review.ratings.core.RatingsApplication;
import com.review.ratings.core.ResponseListener;
import com.review.ratings.data.implementation.HttpRepository;
import com.review.ratings.data.model.RatingsCategory;
import com.review.ratings.data.repository.IHttpRepository;
import com.review.ratings.util.Util;

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
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));
        repository.getAll(url, RatingsCategory[].class, header, new ResponseListener<List<RatingsCategory>>() {
            @Override
            public void success(List<RatingsCategory> response) {
                view.setUserSelectedCategoryToView(response);
            }

            @Override
            public void error(Throwable error) {
                view.showErrorMessage(Util.get().getMessage((VolleyError) error));
            }
        });
    }

    @Override
    public void submitRatings(String url, String body) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));
        repository.post(url, Object.class, body, header, new ResponseListener<Object>() {
            @Override
            public void success(Object response) {
                view.ratingsAdded(response.toString());
            }

            @Override
            public void error(Throwable error) {
                view.showErrorMessage(Util.get().getMessage((VolleyError) error));
            }
        });
    }

    @Override
    public void submitReview(String url, String body) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));
        repository.post(url, Object.class, body, header, new ResponseListener<Object>() {
            @Override
            public void success(Object response) {
                view.reviewAdded(response.toString());
            }

            @Override
            public void error(Throwable error) {
                view.showErrorMessage(Util.get().getMessage((VolleyError) error));
            }
        });
    }
}
