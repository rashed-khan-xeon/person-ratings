package com.review.test.config;

/**
 * Created by arifk on 31.12.17.
 */

public class ConfigKeys {
    private static ConfigKeys configKeys = null;
    public final String PREF_FILE = "ratingsPref";
    public final String PREF_NAME = "userData";


    private ConfigKeys() {
    }

    public static ConfigKeys getInstant() {
        if (configKeys == null) {
            configKeys = new ConfigKeys();
        }
        return configKeys;
    }
}
