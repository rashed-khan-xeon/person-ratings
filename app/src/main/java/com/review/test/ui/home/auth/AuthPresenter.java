package com.review.test.ui.home.auth;

import com.review.test.core.RatingsApplication;
import com.review.test.core.ResponseListener;
import com.review.test.data.implementation.HttpRepository;
import com.review.test.data.model.User;
import com.review.test.data.model.UserType;
import com.review.test.data.repository.IHttpRepository;

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
        repository.post(url, User.class, body, null, new ResponseListener<User>() {
            @Override
            public void success(User response) {
                view.signUpSuccess(response);
            }

            @Override
            public void error(Throwable error) {
                view.showErrorMessage(error.getMessage());
            }
        });
    }

    @Override
    public void doLogin(String url, String loginData) {
        repository.post(url, User.class, loginData, null, new ResponseListener<User>() {
            @Override
            public void success(User response) {
                view.loginSuccess(response);
            }

            @Override
            public void error(Throwable error) {
                view.showErrorMessage(error.getMessage());
            }
        });
    }

    @Override
    public void getUserTypes(String url) {

        repository.getAll(url, UserType[].class, null, new ResponseListener<List<UserType>>() {
            @Override
            public void success(List<UserType> response) {
                view.setUserTypesToView(response);
            }

            @Override
            public void error(Throwable error) {
                view.showErrorMessage(error.getMessage());
            }
        });
    }
}
