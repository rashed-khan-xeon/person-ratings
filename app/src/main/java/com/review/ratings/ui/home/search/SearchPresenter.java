package com.review.ratings.ui.home.search;

import com.android.volley.VolleyError;
import com.review.ratings.core.RatingsApplication;
import com.review.ratings.core.ResponseListener;
import com.review.ratings.data.model.RatingSummary;
import com.review.ratings.data.model.User;
import com.review.ratings.data.repository.IHttpRepository;
import com.review.ratings.util.Util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arifk on 5.1.18.
 */

public class SearchPresenter implements SearchContract.SearchPresenter {
    private IHttpRepository repository;
    private SearchContract.SearchView view;

    public SearchPresenter(IHttpRepository repository, SearchContract.SearchView view) {
        this.repository = repository;
        this.view = view;
    }

    @Override
    public void searchUser(String url) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));

        repository.get(url, User[].class, header, new ResponseListener<User[]>() {
            @Override
            public void success(User[] response) {
                view.setUserListToView(Arrays.asList(response));
            }

            @Override
            public void error(Throwable error) {
                view.noUserFound("No user found");
            }
        });
    }

    @Override
    public void getUserAvgRatingByCategory(String url) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));
        repository.getAll(url, RatingSummary[].class, header, new ResponseListener<List<RatingSummary>>() {
            @Override
            public void success(List<RatingSummary> response) {
                view.setUserAvgRatingsToView(response);
            }

            @Override
            public void error(Throwable error) {
                view.noUserRatings(Util.get().getMessage((VolleyError) error));
            }
        });
    }
}
