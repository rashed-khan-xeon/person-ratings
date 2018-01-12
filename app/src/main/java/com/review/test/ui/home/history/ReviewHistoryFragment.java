package com.review.test.ui.home.history;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.review.test.R;
import com.review.test.config.ApiUrl;
import com.review.test.core.RatingsApplication;
import com.review.test.core.RtClients;
import com.review.test.data.implementation.HttpRepository;
import com.review.test.data.model.RatingsPref;
import com.review.test.data.model.UserRatings;
import com.review.test.data.model.UserReview;
import com.review.test.util.Util;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewHistoryFragment extends Fragment implements HistoryContract.ReviewView {
    private ListView lvUserReviewList;
    private View reviewContent;
    private HistoryContract.ReviewsPresenter presenter;

    public ReviewHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        reviewContent = inflater.inflate(R.layout.fragment_review_history, container, false);
        presenter = new ReviewPresenter(this, new HttpRepository(getActivity()));
        initViewComponents();
        return reviewContent;
    }

    @Override
    public void initViewComponents() {
        lvUserReviewList = reviewContent.findViewById(R.id.lvUserReviewList);
        RatingsPref pref = RatingsApplication.getInstant().getRatingsPref();
        if (pref != null) {
            if (pref.getUser() != null) {
                presenter.getUserReviewList(ApiUrl.getInstance().getUserReviewList(pref.getUser().getUserId()));
            }
        }

    }

    @Override
    public void showSuccessMessage(String msg) {
        Util.get().showToastMsg(getActivity(), msg);
    }

    @Override
    public void showErrorMessage(String msg) {
        Util.get().showToastMsg(getActivity(), msg);
    }

    @Override
    public void setUserReviewListToView(List<UserReview> userReviews) {
        if (userReviews != null) {
            UserReviewAdapter adapter = new UserReviewAdapter(userReviews, getActivity());
            lvUserReviewList.setAdapter(adapter);
        }
    }

    private class UserReviewAdapter extends BaseAdapter {
        List<UserReview> userReviews;
        Context context;

        public UserReviewAdapter(List<UserReview> userReviews, Context context) {
            this.userReviews = userReviews;
            this.context = context;
        }

        @Override
        public int getCount() {
            return userReviews.size();
        }

        @Override
        public UserReview getItem(int i) {
            return userReviews.get(i);
        }

        @Override
        public long getItemId(int i) {
            return getItem(i).getUserReviewId();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View row;
            if (view == null) {
                row = LayoutInflater.from(context).inflate(R.layout.reivew_list_item, null);
            } else {
                row = view;
            }
            TextView tvReviewTitle = row.findViewById(R.id.tvReviewTitle);
            TextView tvReviewDate = row.findViewById(R.id.tvReviewDate);

            if (getItem(i).getComments() != null) {
                tvReviewTitle.setText(getItem(i).getComments());
            }
            if (getItem(i).getReviewDate() != null) {
                tvReviewDate.setText(getItem(i).getReviewDate().toString());
            }

            return row;
        }
    }
}
