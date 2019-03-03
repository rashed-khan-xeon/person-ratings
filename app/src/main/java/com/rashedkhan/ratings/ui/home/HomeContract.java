package com.rashedkhan.ratings.ui.home;

import com.rashedkhan.ratings.ui.home.common.BaseView;

public interface HomeContract {
    interface HomeView extends BaseView {
        void passwordChanged();
    }

    interface Presenter {
        void changePassword(String url, String body);
    }
}

