package com.rashedkhan.ratings.core;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.rashedkhan.ratings.config.ConfigKeys;
import com.rashedkhan.ratings.data.model.RatingsPref;
import com.rashedkhan.ratings.data.model.User;

import java.util.Map;

/**
 * Created by arifk on 8.12.17.
 */

public class RatingsApplication extends Application {
    private static RatingsApplication application;
    private static final String SET_COOKIE_KEY = "accessToken";
    private static final String COOKIE_KEY = "Cookie";
    private static final String SESSION_COOKIE = "sessionid";
    private SharedPreferences _preferences;
    private String countryCode = "BD";

    public static RatingsApplication getInstant() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        _preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void setPreference(RatingsPref ratingsPref) {
        String pref = RtClients.getInstance().getGson().toJson(ratingsPref);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//getSharedPreferences(ConfigKeys.getInstant().PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(ConfigKeys.getInstant().PREF_NAME, pref);
        editor.commit();
    }

    public void removePreference() {
        String pref = "";
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//this.getSharedPreferences(ConfigKeys.getInstant().PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(ConfigKeys.getInstant().PREF_NAME, pref);
        editor.commit();
    }

    public User getUser() {
        if (getRatingsPref() != null)
            return getRatingsPref().getUser();
        return null;
    }

    public RatingsPref getRatingsPref() {
        String defaultValue = RtClients.getInstance().getGson().toJson(new RatingsPref());
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//this.getSharedPreferences(ConfigKeys.getInstant().PREF_FILE, Context.MODE_PRIVATE);
        String pref = sharedPref.getString(ConfigKeys.getInstant().PREF_NAME, defaultValue);
        Log.e("Ratings", "getRatingsPref: " + pref);
        return RtClients.getInstance().getGson().fromJson(pref, RatingsPref.class);
    }


    /**
     * Checks the response headers for session cookie and saves it
     * if it finds it.
     *
     * @param headers Response Headers.
     */
    public final void checkSessionCookie(Map<String, String> headers) {
        if (headers.containsKey(SET_COOKIE_KEY)
                && headers.get(SET_COOKIE_KEY).startsWith(SESSION_COOKIE)) {
            String cookie = headers.get(SET_COOKIE_KEY);
            if (cookie.length() > 0) {
                String[] splitCookie = cookie.split(";");
                String[] splitSessionId = splitCookie[0].split("=");
                cookie = splitSessionId[1];
                SharedPreferences.Editor prefEditor = _preferences.edit();
                prefEditor.putString(SESSION_COOKIE, cookie);
                prefEditor.apply();
            }
        }
    }

    /**
     * Adds session cookie to headers if exists.
     *
     * @param headers
     */
    public final void addSessionCookie(Map<String, String> headers) {
        String sessionId = _preferences.getString(SESSION_COOKIE, "");
        if (sessionId.length() > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(SESSION_COOKIE);
            builder.append("=");
            builder.append(sessionId);
            if (headers.containsKey(COOKIE_KEY)) {
                builder.append("; ");
                builder.append(headers.get(COOKIE_KEY));
            }
            headers.put(COOKIE_KEY, builder.toString());
        }
    }

    public boolean isLogin() {
        RatingsPref pref = getRatingsPref();
        return pref != null && pref.getUser() != null;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public String getCountryCode() {
        return countryCode;
    }

    public boolean isOTPAvailable() {
        if (countryCode != null && countryCode.equals("BD")) {
            return true;
        }
        return false;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
