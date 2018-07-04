package com.review.ratings.ui.home.history;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.rashedkhan.ratings.R;
import com.review.ratings.config.ApiUrl;
import com.review.ratings.core.RatingsApplication;
import com.review.ratings.data.implementation.HttpRepository;
import com.review.ratings.data.model.RatingsPref;
import com.review.ratings.data.model.UserReview;
import com.review.ratings.util.Util;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewHistoryFragment extends Fragment implements HistoryContract.ReviewView {
    private RecyclerView lvUserReviewList;
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
        Util.get().showProgress(getActivity(), true, "Processing...");
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
        btnNext.setOnClickListener(view -> {
            start += top + 1;
            if (pref != null) {
                presenter.getUserReviewList(ApiUrl.getInstance().getUserReviewList(pref.getUser().getUserId(), start, top));
            }
        });
        btnPrev.setOnClickListener(view -> {
            if (start != 0)
                start -= top + 1;
            if (pref != null) {
                presenter.getUserReviewList(ApiUrl.getInstance().getUserReviewList(pref.getUser().getUserId(), start, top));
            }
        });

    }

    @Override
    public void showSuccessMessage(String msg) {
        Util.get().showProgress(getActivity(), false, null);
        Util.get().showToastMsg(getActivity(), msg);
    }

    @Override
    public void showErrorMessage(String msg) {
        Util.get().showProgress(getActivity(), false, null);
        Util.get().showToastMsg(getActivity(), msg);
    }

    @Override
    public void setUserReviewListToView(List<UserReview> userReviews) {
        Util.get().showProgress(getActivity(), false, null);
        if (userReviews.size() == top) {
            llRevPaging.setVisibility(View.VISIBLE);
            return;
        }
        if (top > userReviews.size()) {
            top = userReviews.size();
        } else {
            top = 10;
        }

        UserReviewAdapter adapter = new UserReviewAdapter(userReviews, getActivity());
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getActivity());
        lvUserReviewList.setLayoutManager(lm);
        lvUserReviewList.setItemAnimator(new DefaultItemAnimator());
        lvUserReviewList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private class UserReviewAdapter extends RecyclerView.Adapter<UserReviewAdapter.BaseHolder> {
        List<UserReview> userReviews;
        Context context;

        public UserReviewAdapter(List<UserReview> userReviews, Context context) {
            this.userReviews = userReviews;
            this.context = context;
        }


        public UserReview getItem(int i) {
            return userReviews.get(i);
        }

        @Override
        public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 1) {
                return new EmptyHolder(LayoutInflater.from(context).inflate(R.layout.rating_empty_layout, parent, false));
            } else
                return new Holder(LayoutInflater.from(context).inflate(R.layout.reivew_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(BaseHolder holder, int position) {
            if (holder instanceof Holder) {
                Holder row = (Holder) holder;
                if (getItem(position).getComments() != null) {
                    row.tvReviewTitle.setText("Comments: ");
                    row.tvReviewTitle.append(getItem(position).getComments());
                }
                if (getItem(position).getRatingsCategory() != null) {
                    if (getItem(position).getRatingsCategory().getCategory() != null) {
                        row.tvReviewCat.setText(getItem(position).getRatingsCategory().getCategory().getName());
                    }
                }
                if (getItem(position).getReviewDate() != null) {
                 //   Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd - MMMM - yyyy", Locale.US);
                    String s = formatter.format(getItem(position).getReviewDate());
//                    calendar.setTime(getItem(position).getReviewDate());
//                    String ampm = calendar.get(Calendar.AM_PM) == Calendar.AM ? " AM" : " PM";
//                    String txt = calendar.get(Calendar.DAY_OF_MONTH) + " - " + calendar.get(Calendar.MONTH) + 1 + " - " + calendar.get(Calendar.YEAR) + "  " + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + ampm;
                    row.tvReviewDate.setText(s);
                }
            }
        }

        @Override
        public long getItemId(int i) {
            return getItem(i).getUserReviewId();
        }

        @Override
        public int getItemCount() {
            if (userReviews == null || userReviews.size() == 0) {
                return 1;
            }
            return userReviews.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (getItemCount() == 1) {
                return 1;
            } else
                return 2;
        }

        public class Holder extends BaseHolder {

            TextView tvReviewTitle, tvReviewCat, tvReviewDate;

            public Holder(View itemView) {
                super(itemView);
                tvReviewTitle = itemView.findViewById(R.id.tvReviewTitle);
                tvReviewCat = itemView.findViewById(R.id.tvReviewCat);
                tvReviewDate = itemView.findViewById(R.id.tvReviewDate);
            }
        }

        public class BaseHolder extends RecyclerView.ViewHolder {

            public BaseHolder(View itemView) {
                super(itemView);
            }
        }

        public class EmptyHolder extends BaseHolder {

            public EmptyHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
