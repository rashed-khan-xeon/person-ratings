package com.rashedkhan.ratings.ui.home.common;

/**
 * Created by arifk on 30.12.17.
 */

public interface BaseView {
    void initViewComponents();

    void showSuccessMessage(String msg);

    void showErrorMessage(String msg);
}
