package com.rashedkhan.ratings.ui.home.search;

import com.android.volley.VolleyError;
import com.rashedkhan.ratings.core.RatingsApplication;
import com.rashedkhan.ratings.core.ResponseListener;
import com.rashedkhan.ratings.data.model.Feature;
import com.rashedkhan.ratings.data.model.FeatureType;
import com.rashedkhan.ratings.data.model.RatingSummary;
import com.rashedkhan.ratings.data.model.User;
import com.rashedkhan.ratings.data.repository.IHttpRepository;
import com.rashedkhan.ratings.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    public void searchFeatureUser(String url) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));

        repository.get(url, User[].class, header, new ResponseListener<User[]>() {
            @Override
            public void success(User[] response) {
                view.setFeatureUserListToView(Arrays.asList(response));
            }

            @Override
            public void error(Throwable error) {
                view.noUserFound("Nothing found");
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

    @Override
    public void getFeatureTypeListForUser(String url) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));
        repository.getAll(url, FeatureType[].class, header, new ResponseListener<List<FeatureType>>() {
            @Override
            public void success(List<FeatureType> response) {
                view.setFeatureTypeToView(response);
            }

            @Override
            public void error(Throwable error) {
                view.noUserRatings(Util.get().getMessage((VolleyError) error));
            }
        });
    }

    @Override
    public void getFeatureListForUser(String url) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));
        repository.getAll(url, Feature[].class, header, new ResponseListener<List<Feature>>() {
            @Override
            public void success(List<Feature> response) {
                view.setFeaturesToView(response);
            }

            @Override
            public void error(Throwable error) {
                view.setFeaturesToView(new ArrayList<>());
            }
        });
    }
}
