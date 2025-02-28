package com.review.ratings.ui.home.setting;

import com.review.ratings.data.model.Category;
import com.review.ratings.data.model.RatingsCategory;
import com.review.ratings.data.model.UserSetting;
import com.review.ratings.ui.home.common.BaseView;

import java.util.List;

/**
 * Created by arifk on 8.1.18.
 */

public interface SettingContract {

    interface SettingPresenter {
        void addCategory(String url, String body);

        void updateUserSetting(String url, String body);

        void getUserRatingsCategory(String url);

        void getCategoriesByUserType(String url);

        void getCategoriesByUserId(String url);

        void addRatingsCategory(String url, String body);

    }

    interface SettingView extends BaseView {
        void setUserRatingsCategories(List<RatingsCategory> ratingsCategories);

        void setCategories(List<Category> categories);

        void categoryAdded(Category category);

        void ratingsCategoryAdded(String msg);

        void settingsUpdated(UserSetting setting);
    }

}

