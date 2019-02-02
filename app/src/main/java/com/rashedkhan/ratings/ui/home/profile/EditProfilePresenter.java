package com.rashedkhan.ratings.ui.home.profile;

import com.android.volley.VolleyError;
import com.rashedkhan.ratings.core.RatingsApplication;
import com.rashedkhan.ratings.core.ResponseListener;
import com.rashedkhan.ratings.data.implementation.HttpRepository;
import com.rashedkhan.ratings.data.model.User;
import com.rashedkhan.ratings.data.repository.IHttpRepository;
import com.rashedkhan.ratings.util.Util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by arifk on 12.1.18.
 */

public class EditProfilePresenter implements ProfileContract.EditProfilePresenter {
    private ProfileContract.EditProfileView view;
    private IHttpRepository repository;

    public EditProfilePresenter(ProfileContract.EditProfileView view, HttpRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void updateUserProfile(String url, String body) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));

        repository.post(url, User.class, body, header, new ResponseListener<User>() {
            @Override
            public void success(User response) {
                view.profileUpdated(response);
            }

            @Override
            public void error(Throwable error) {
                view.showErrorMessage(Util.get().getMessage((VolleyError) error));
            }
        });
    }

    @Override
    public void uploadPhoto(String url, String body) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));
        repository.post(url, Object.class, body, header, new ResponseListener<Object>() {
            @Override
            public void success(Object response) {
              view.imageUploaded();
            }

            @Override
            public void error(Throwable error) {
                view.showErrorMessage(Util.get().getMessage((VolleyError) error));
            }
        });
    }
}
