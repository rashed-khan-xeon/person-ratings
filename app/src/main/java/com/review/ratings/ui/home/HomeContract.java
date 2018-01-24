package com.review.ratings.ui.home;

import com.review.ratings.ui.home.common.BaseView;

/**
 * Created by arifk on 25.1.18.
 */

public interface HomeContract {
    interface HomeView extends BaseView {
        void passwordChanged();
    }

    interface Presenter {
        void changePassword(String url, String body);
    }
}

