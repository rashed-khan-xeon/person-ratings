package com.review.ratings.ui.home.history;

import com.review.ratings.data.implementation.HttpRepository;
import com.review.ratings.data.repository.IHttpRepository;

/**
 * Created by arifk on 12.1.18.
 */

public class HistoryPresenter implements HistoryContract.HistoryPresenter {
    private HistoryContract.HistoryView view;
    private IHttpRepository repository;

    public HistoryPresenter(HistoryContract.HistoryView view, HttpRepository repository) {
        this.view = view;
        this.repository = repository;
    }
}
