package com.review.ratings.ui.home.auth;

import com.review.ratings.data.model.User;
import com.review.ratings.data.model.UserType;
import com.review.ratings.ui.home.common.BaseView;

import java.util.List;

/**
 * Created by arifk on 30.12.17.
 */

public interface AuthContract {
    interface AuthPresenter {
        void doSignUp(String url, String body);

        void doLogin(String url, String loginData);

        void getUserTypes(String url);
    }

    interface AuthView extends BaseView {
        void signUpSuccess(User user);

        void loginSuccess(User user);

        void setUserTypesToView(List<UserType> userTypes);
    }


}
