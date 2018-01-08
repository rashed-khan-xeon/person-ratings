package com.review.test.ui.home.setting;

import com.review.test.core.RatingsApplication;
import com.review.test.core.ResponseListener;
import com.review.test.data.implementation.HttpRepository;
import com.review.test.data.model.Category;
import com.review.test.data.repository.IHttpRepository;

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
