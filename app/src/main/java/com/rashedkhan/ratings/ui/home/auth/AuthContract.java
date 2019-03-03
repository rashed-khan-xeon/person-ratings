package com.rashedkhan.ratings.ui.home.auth;

import com.rashedkhan.ratings.data.model.User;
import com.rashedkhan.ratings.data.model.UserType;
import com.rashedkhan.ratings.ui.home.common.BaseView;

import java.util.List;

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

    interface VerifyView extends BaseView {
        void msgSent(Object obj);

        void userVerified();

        void userVerificationFailed(String msg);
    }

    interface VerifyPresenter {
        void sendVerificationCode(String url);

        void checkVerificationCode(String url, String body);
    }


}
