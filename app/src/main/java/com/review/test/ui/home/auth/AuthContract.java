package com.review.test.ui.home.auth;

import com.review.test.data.model.User;
import com.review.test.ui.home.common.BaseView;

/**
 * Created by arifk on 30.12.17.
 */

public interface AuthContract {
    interface AuthPresenter {
        void doSignUp(String url, String body);

        void doLogin(String url, String loginData);
    }

    interface AuthView extends BaseView {
        void signUpSuccess();

        void loginSuccess(User user);
    }


}
