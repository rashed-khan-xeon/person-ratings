package com.review.ratings.ui.home.auth;

import android.util.Log;

import com.android.volley.VolleyError;
import com.review.ratings.core.ResponseListener;
import com.review.ratings.data.implementation.HttpRepository;
import com.review.ratings.data.model.User;
import com.review.ratings.data.model.UserType;
import com.review.ratings.data.repository.IHttpRepository;
import com.review.ratings.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arifk on 30.12.17.
 */

public class AuthPresenter implements AuthContract.AuthPresenter {
    private IHttpRepository repository;
    private AuthContract.AuthView view;

    public AuthPresenter(AuthContract.AuthView authView, HttpRepository repository) {
        this.repository = repository;
        view = authView;
    }

    @Override
    public void doSignUp(String url, String body) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        repository.post(url, User.class, body, header, new ResponseListener<User>() {
            @Override
            public void success(User response) {
                view.signUpSuccess(response);
            }

            @Override
            public void error(Throwable error) {
                view.showErrorMessage(Util.get().getMessage((VolleyError) error));
            }
        });
    }

    @Override
    public void doLogin(String url, String loginData) {
        Log.e("Login info", "Login data:" + loginData);
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        repository.post(url, User.class, loginData, header, new ResponseListener<User>() {
            @Override
            public void success(User response) {
                view.loginSuccess(response);
            }

            @Override
            public void error(Throwable error) {
                view.showErrorMessage(Util.get().getMessage((VolleyError) error));
            }
        });
    }

    @Override
    public void getUserTypes(String url) {

        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        repository.getAll(url, UserType[].class, header, new ResponseListener<List<UserType>>() {
            @Override
            public void success(List<UserType> response) {
                view.setUserTypesToView(response);
            }

            @Override
            public void error(Throwable error) {
                view.showErrorMessage(Util.get().getMessage((VolleyError) error));
            }
        });
    }
}
