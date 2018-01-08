package com.review.test.ui.home.auth;

import com.review.test.core.ResponseListener;
import com.review.test.data.implementation.HttpRepository;
import com.review.test.data.model.User;
import com.review.test.data.repository.IHttpRepository;

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
        repository.post(url, Object.class, body, null, new ResponseListener<Object>() {
            @Override
            public void success(Object response) {
                view.signUpSuccess();
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
}
