package com.review.ratings.ui.home.setting;

import com.review.ratings.core.RatingsApplication;
import com.review.ratings.core.ResponseListener;
import com.review.ratings.data.implementation.HttpRepository;
import com.review.ratings.data.model.Category;
import com.review.ratings.data.model.RatingsCategory;
import com.review.ratings.data.model.UserSetting;
import com.review.ratings.data.repository.IHttpRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arifk on 8.1.18.
 */

public class SettingPresenter implements SettingContract.SettingPresenter {

    private SettingContract.SettingView view;
    private IHttpRepository repository;

    public SettingPresenter(SettingContract.SettingView view, HttpRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void addRatingsCategory(String url, String body) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));
        repository.post(url, Object.class, body, header, new ResponseListener<Object>() {
            @Override
            public void success(Object response) {
                view.ratingsCategoryAdded(response.toString());
            }

            @Override
            public void error(Throwable error) {
                view.showErrorMessage(error.getMessage());
            }
        });
    }

    @Override
    public void updateUserSetting(String url, String body) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));

        repository.post(url, UserSetting.class, body, header, new ResponseListener<UserSetting>() {
            @Override
            public void success(UserSetting response) {
                view.settingsUpdated(response);
            }

            @Override
            public void error(Throwable error) {
                view.showErrorMessage(error.getMessage());
            }
        });
    }

    @Override
    public void getUserRatingsCategory(String url) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));

        repository.getAll(url, RatingsCategory[].class, header, new ResponseListener<List<RatingsCategory>>() {
            @Override
            public void success(List<RatingsCategory> response) {
                view.setUserRatingsCategories(response);
            }

            @Override
            public void error(Throwable error) {
                view.showErrorMessage(error.getMessage());
            }
        });
    }

    @Override
    public void getCategoriesByUserType(String url) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));

        repository.getAll(url, Category[].class, header, new ResponseListener<List<Category>>() {
            @Override
            public void success(List<Category> response) {
                view.setCategories(response);
            }

            @Override
            public void error(Throwable error) {
                view.showErrorMessage(error.getMessage());
            }
        });
    }
}
