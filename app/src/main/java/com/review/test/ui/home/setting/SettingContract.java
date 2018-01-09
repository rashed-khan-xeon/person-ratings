package com.review.test.ui.home.setting;

import com.review.test.data.model.Category;
import com.review.test.data.model.RatingsCategory;
import com.review.test.ui.home.common.BaseView;

import java.util.List;

/**
 * Created by arifk on 8.1.18.
 */

public interface SettingContract {

    interface SettingPresenter {
        void getUserRatingsCategory(String url);

        void getCategoriesByUserType(String url);

        void addRatingsCategory(String url, String body);

    }

    interface SettingView extends BaseView {
        void setUserRatingsCategories(List<RatingsCategory> ratingsCategories);

        void setCategories(List<Category> categories);

        void ratingsCategoryAdded(String msg);
    }

}

