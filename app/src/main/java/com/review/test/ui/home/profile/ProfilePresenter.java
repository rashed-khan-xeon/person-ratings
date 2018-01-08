package com.review.test.ui.home.profile;

import com.review.test.core.RatingsApplication;
import com.review.test.core.ResponseListener;
import com.review.test.data.implementation.HttpRepository;
import com.review.test.data.model.RatingSummary;
import com.review.test.data.model.RatingsCategory;
import com.review.test.data.model.User;
import com.review.test.data.repository.IHttpRepository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arifk on 3.1.18.
 */

public class ProfilePresenter implements ProfileContract.ProfilePresenter {
    private ProfileContract.ProfileView view;
    private IHttpRepository repository;

    public ProfilePresenter(ProfileContract.ProfileView view, HttpRepository repository) {
        this.view = view;
        this.repository = repository;
    }


    @Override
    public void getUserSelectedCategory(String url) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));
        repository.get(url, RatingsCategory[].class, header, new ResponseListener<RatingsCategory[]>() {
            @Override
            public void success(RatingsCategory[] response) {
                view.setUserSelectedCategory(Arrays.asList(response));
            }

            @Override
            public void error(Throwable error) {
                view.showErrorMessage(error.getMessage());
            }
        });
    }

    @Override
    public void getUserDetails(String url) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));
        repository.get(url, User.class, header, new ResponseListener<User>() {
            @Override
            public void success(User response) {
                view.setUserDetailsToView(response);
            }

            @Override
            public void error(Throwable error) {
                view.showErrorMessage(error.getMessage());
            }
        });
    }

    @Override
    public void getUserAvgRatingByCategory(String url) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));
        repository.getAll(url, RatingSummary[].class, header, new ResponseListener<List<RatingSummary>>() {
            @Override
            public void success(List<RatingSummary> response) {
                view.setUserAvgRatingsToView(response);
            }

            @Override
            public void error(Throwable error) {

                view.showErrorMessage(error.getMessage());
            }
        });
    }
}
