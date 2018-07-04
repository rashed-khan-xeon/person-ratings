package com.review.ratings.ui.home;

import com.android.volley.VolleyError;
import com.review.ratings.core.RatingsApplication;
import com.review.ratings.core.ResponseListener;
import com.review.ratings.data.implementation.HttpRepository;
import com.review.ratings.data.repository.IHttpRepository;
import com.review.ratings.util.Util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by arifk on 25.1.18.
 */

public class HomePresenter implements HomeContract.Presenter {
    private HomeContract.HomeView view;
    private IHttpRepository repository;

    public HomePresenter(HomeContract.HomeView view, HttpRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void changePassword(String url, String body) {    Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));

        repository.post(url, Object.class, body, header, new ResponseListener<Object>() {
            @Override
            public void success(Object response) {
                view.passwordChanged();
            }

            @Override
            public void error(Throwable error) {
                view.showErrorMessage(Util.get().getMessage((VolleyError) error));
            }
        });
    }
}
