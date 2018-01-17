package com.review.ratings.ui.home.history;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.rashedkhan.ratings.R;
import com.review.ratings.config.ApiUrl;
import com.review.ratings.core.RatingsApplication;
import com.review.ratings.data.implementation.HttpRepository;
import com.review.ratings.data.model.RatingsPref;
import com.review.ratings.data.model.UserRatings;
import com.review.ratings.util.Util;

import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RatingsHistoryFragment extends Fragment implements HistoryContract.RatingsView {

    private View contentView;
    private HistoryContract.RatingsPresenter presenter;
    private ListView lvRatingsList;
    private Button btnRtPrev, btnRtNext;
    private int start = 0, top = 10;
    private LinearLayout llRatPaging;

    public RatingsHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_ratings_history, container, false);
        presenter = new RatingsPresenter(this, new HttpRepository(getActivity()));
        initViewComponents();

        return contentView;
    }

    @Override
    public void initViewComponents() {
        llRatPaging = contentView.findViewById(R.id.llRatPaging);
        btnRtPrev = contentView.findViewById(R.id.btnRtPrev);
        btnRtNext = contentView.findViewById(R.id.btnRtNext);
        lvRatingsList = contentView.findViewById(R.id.lvRatingsList);
        RatingsPref pref = RatingsApplication.getInstant().getRatingsPref();
        if (pref != null) {
            if (pref.getUser() != null) {
                presenter.getUserRatingsList(ApiUrl.getInstance().getUserRatingsList(pref.getUser().getUserId(), start, top));
            }
        }
        btnRtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start += top + 1;
                presenter.getUserRatingsList(ApiUrl.getInstance().getUserRatingsList(pref.getUser().getUserId(), start, top));
            }
        });
        btnRtPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (start != 0) {
                    start -= top + 1;
                }
                presenter.getUserRatingsList(ApiUrl.getInstance().getUserRatingsList(pref.getUser().getUserId(), start, top));
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
    public void setUserRatingsView(List<UserRatings> userRatings) {
        if (userRatings.size() == top) {
            llRatPaging.setVisibility(View.VISIBLE);
        }
        if (top > userRatings.size()) {
            top = userRatings.size();
        } else {
            top = 10;
        }

        UserRatingsAdapter adapter = new UserRatingsAdapter(userRatings, getActivity());
        lvRatingsList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private class UserRatingsAdapter extends BaseAdapter {
        List<UserRatings> userRatings;
        Context context;

        public UserRatingsAdapter(List<UserRatings> userRatings, Context context) {
            this.userRatings = userRatings;
            this.context = context;
        }

        @Override
        public int getCount() {
            return userRatings.size();
        }

        @Override
        public UserRatings getItem(int i) {
            return userRatings.get(i);
        }

        @Override
        public long getItemId(int i) {
            return getItem(i).getUserRatingsId();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View row;
            if (view == null) {
                row = LayoutInflater.from(context).inflate(R.layout.ratings_list_item, null);
            } else {
                row = view;
            }
            RatingBar rtHsRatings = row.findViewById(R.id.rtHsRatings);
            TextView tvRatingsDate = row.findViewById(R.id.tvRatingsDate);
            TextView tvRatingsCatTitle = row.findViewById(R.id.tvRatingsCatTitle);
            TextView tvRatingsStatus = row.findViewById(R.id.tvRatingsStatus);


            rtHsRatings.setRating(getItem(i).getRatings());

            if (getItem(i).getRatingsDate() != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(getItem(i).getRatingsDate());
                tvRatingsDate.setText(calendar.get(Calendar.DAY_OF_MONTH) + " - " + calendar.get(Calendar.MONTH) + 1 + " - " + calendar.get(Calendar.YEAR) + "  " + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE));
            }
            if (getItem(i).getRatingsCategory() != null) {
                if (getItem(i).getRatingsCategory().getCategory() != null) {
                    if (getItem(i).getRatingsCategory().getCategory().getName() != null) {
                        tvRatingsCatTitle.setText(getItem(i).getRatingsCategory().getCategory().getName());
                    }
                }
            }
            tvRatingsStatus.setText(Util.get().generateUserStatusFromRatings(getItem(i).getRatings()) + " ( " + getItem(i).getRatings() + " )");
            switch ((int) getItem(i).getRatings()) {
                case 1:
                    tvRatingsStatus.setTextColor(Color.RED);
                    break;
                case 2:
                    tvRatingsStatus.setTextColor(Color.YELLOW);
                    break;
                case 3:
                    tvRatingsStatus.setTextColor(Color.parseColor("#E57373"));
                    break;
                case 4:
                    tvRatingsStatus.setTextColor(Color.parseColor("#B71C1C"));
                    break;
                case 5:
                    tvRatingsStatus.setTextColor(Color.BLUE);
                    break;
                case 6:
                    tvRatingsStatus.setTextColor(Color.MAGENTA);
                    break;
                case 7:
                    tvRatingsStatus.setTextColor(Color.parseColor("#4527A0"));
                    break;
                case 8:
                    tvRatingsStatus.setTextColor(Color.parseColor("#4CAF50"));
                    break;
                case 9:
                    tvRatingsStatus.setTextColor(Color.parseColor("#00E676"));
                    break;
                case 10:
                    tvRatingsStatus.setTextColor(Color.GREEN);
                    break;
                default:
                    tvRatingsStatus.setTextColor(Color.WHITE);
                    break;
            }

            return row;
        }
    }
}
