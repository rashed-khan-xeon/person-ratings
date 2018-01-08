package com.review.test.ui.home.search;

import com.review.test.data.model.User;
import com.review.test.ui.home.common.BaseView;

import java.util.List;

/**
 * Created by arifk on 5.1.18.
 */

public interface SearchContract {
    interface SearchPresenter {
        void searchUser(String url);
    }

    interface SearchView extends BaseView {
        void setUserListToView(List<User> userList);

        void noUserFound(String msg);
    }
}
