package com.rashedkhan.ratings.ui.home.setting;

import com.android.volley.VolleyError;
import com.rashedkhan.ratings.core.RatingsApplication;
import com.rashedkhan.ratings.core.ResponseListener;
import com.rashedkhan.ratings.data.implementation.HttpRepository;
import com.rashedkhan.ratings.data.model.Category;
import com.rashedkhan.ratings.data.model.RatingsCategory;
import com.rashedkhan.ratings.data.model.UserSetting;
import com.rashedkhan.ratings.data.repository.IHttpRepository;
import com.rashedkhan.ratings.util.Util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



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
                view.showErrorMessage(Util.get().getMessage((VolleyError) error));
            }
        });
    }

    @Override
    public void addCategory(String url, String body) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));
        repository.post(url, Category.class, body, header, new ResponseListener<Category>() {
            @Override
            public void success(Category response) {
                view.categoryAdded(response);
            }

            @Override
            public void error(Throwable error) {
                view.showErrorMessage(Util.get().getMessage((VolleyError) error));
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
                view.showErrorMessage(Util.get().getMessage((VolleyError) error));
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
                //view.showErrorMessage(Util.get().getMessage((VolleyError) error));
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
                view.setCategories(new LinkedList<>(response));
            }

            @Override
            public void error(Throwable error) {
                view.showErrorMessage(Util.get().getMessage((VolleyError) error));
            }
        });
    }

    @Override
    public void getCategoriesByUserId(String url) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));

        repository.getAll(url, Category[].class, header, new ResponseListener<List<Category>>() {
            @Override
            public void success(List<Category> response) {
                view.setCategories(new LinkedList<>(response));
            }

            @Override
            public void error(Throwable error) {
            //    view.showErrorMessage(Util.get().getMessage((VolleyError) error));
            }
        });
    }
}
