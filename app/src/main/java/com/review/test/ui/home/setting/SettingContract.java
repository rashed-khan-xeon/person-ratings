package com.review.test.ui.home.setting;

import com.review.test.data.model.Category;
import com.review.test.ui.home.common.BaseView;

import java.util.List;

/**
 * Created by arifk on 8.1.18.
 */

public interface SettingContract {

    interface SettingPresenter {
        void getCategoriesByUserType(String url);

    }

    interface SettingView extends BaseView {
        void setCategories(List<Category> categories);
    }

}

