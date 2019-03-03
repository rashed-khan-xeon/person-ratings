package com.rashedkhan.ratings.ui.home.auth;

import android.util.Log;

import com.android.volley.VolleyError;
import com.rashedkhan.ratings.core.RatingsApplication;
import com.rashedkhan.ratings.core.ResponseListener;
import com.rashedkhan.ratings.data.implementation.HttpRepository;
import com.rashedkhan.ratings.data.model.User;
import com.rashedkhan.ratings.data.model.UserType;
import com.rashedkhan.ratings.data.repository.IHttpRepository;
import com.rashedkhan.ratings.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthPresenter implements AuthContract.AuthPresenter, AuthContract.VerifyPresenter {
    private IHttpRepository repository;
    private AuthContract.AuthView view;
    private AuthContract.VerifyView verifyView;

    public AuthPresenter(AuthContract.AuthView authView, HttpRepository repository) {
        this.repository = repository;
        view = authView;
    }

    public AuthPresenter(AuthContract.VerifyView verifyView, HttpRepository repository) {
        this.repository = repository;
        this.verifyView = verifyView;
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

    @Override
    public void sendVerificationCode(String url) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getUser().getUserId()));
        repository.get(url, Object.class, header, new ResponseListener<Object>() {
            @Override
            public void success(Object response) {
                verifyView.msgSent(response);
            }

            @Override
            public void error(Throwable error) {
                verifyView.showErrorMessage(Util.get().getMessage((VolleyError) error));
            }
        });
    }

    @Override
    public void checkVerificationCode(String url, String body) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getUser().getUserId()));
        repository.post(url, Object.class, body, header, new ResponseListener<Object>() {
            @Override
            public void success(Object response) {
                verifyView.userVerified();
            }

            @Override
            public void error(Throwable error) {
                verifyView.userVerificationFailed(Util.get().getMessage((VolleyError) error));
            }
        });
    }
}
