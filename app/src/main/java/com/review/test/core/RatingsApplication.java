package com.review.test.core;

import android.app.Application;

/**
 * Created by arifk on 8.12.17.
 */

public class RatingsApplication extends Application {
    private static RatingsApplication application;

    public static RatingsApplication getInstant() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
