package com.review.test.ui.home.history;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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

import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewHistoryFragment extends Fragment implements HistoryContract.ReviewView {
    private ListView lvUserReviewList;
    private View reviewContent;
    private HistoryContract.ReviewsPresenter presenter;
    private Button btnPrev, btnNext;
    private int start = 0, top = 10;
    private LinearLayout llRevPaging;

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
        llRevPaging = reviewContent.findViewById(R.id.llRevPaging);
        btnPrev = reviewContent.findViewById(R.id.btnPrev);
        btnNext = reviewContent.findViewById(R.id.btnNext);
        lvUserReviewList = reviewContent.findViewById(R.id.lvUserReviewList);
        RatingsPref pref = RatingsApplication.getInstant().getRatingsPref();
        if (pref != null) {
            if (pref.getUser() != null) {
                presenter.getUserReviewList(ApiUrl.getInstance().getUserReviewList(pref.getUser().getUserId(), start, top));
            }
        }
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start += top + 1;
                presenter.getUserReviewList(ApiUrl.getInstance().getUserReviewList(pref.getUser().getUserId(), start, top));
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (start != 0)
                    start -= top + 1;
                presenter.getUserReviewList(ApiUrl.getInstance().getUserReviewList(pref.getUser().getUserId(), start, top));
            }
        });

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
            if (top > userReviews.size()) {
                top = userReviews.size();
            } else {
                top = 10;
            }
            if (userReviews.size() > top) {
                llRevPaging.setVisibility(View.VISIBLE);
            } else {
                llRevPaging.setVisibility(View.GONE);
            }
            UserReviewAdapter adapter = new UserReviewAdapter(userReviews, getActivity());
            lvUserReviewList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
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
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(getItem(i).getReviewDate());
                tvReviewDate.setText(calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.MONTH) + 1 + " " + calendar.get(Calendar.YEAR));
            }

            return row;
        }
    }
}
