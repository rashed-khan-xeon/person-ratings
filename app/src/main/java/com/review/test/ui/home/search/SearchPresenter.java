package com.review.test.ui.home.search;

import com.review.test.core.RatingsApplication;
import com.review.test.core.ResponseListener;
import com.review.test.data.model.User;
import com.review.test.data.repository.IHttpRepository;

import java.util.Arrays;
import java.util.HashMap;
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
}
