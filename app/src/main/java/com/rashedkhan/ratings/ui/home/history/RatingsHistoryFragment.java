package com.rashedkhan.ratings.ui.home.history;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.rashedkhan.ratings.R;
import com.rashedkhan.ratings.config.ApiUrl;
import com.rashedkhan.ratings.core.RatingsApplication;
import com.rashedkhan.ratings.data.implementation.HttpRepository;
import com.rashedkhan.ratings.data.model.RatingsPref;
import com.rashedkhan.ratings.data.model.UserRatings;
import com.rashedkhan.ratings.util.Util;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class RatingsHistoryFragment extends Fragment implements HistoryContract.RatingsView {

    private View contentView;
    private HistoryContract.RatingsPresenter presenter;
    private RecyclerView lvRatingsList;
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
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getActivity());
        lvRatingsList.setLayoutManager(lm);
        lvRatingsList.setItemAnimator(new DefaultItemAnimator());
        lvRatingsList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public class UserRatingsAdapter extends RecyclerView.Adapter<UserRatingsAdapter.BaseHolder> {
        List<UserRatings> userRatings;
        Context context;

        public UserRatingsAdapter(List<UserRatings> userRatings, Context context) {
            this.userRatings = userRatings;
            this.context = context;
        }


        @Override
        public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 1) {
                return new EmptyHolder(LayoutInflater.from(context).inflate(R.layout.rating_empty_layout, parent, false));
            } else
                return new Holder(LayoutInflater.from(context).inflate(R.layout.ratings_list_item, parent, false));
        }

        @Override
        public int getItemViewType(int position) {
            if (getItemCount() == 1) {
                return 1;
            } else
                return 2;
        }

        @Override
        public void onBindViewHolder(BaseHolder holder, int position) {
            if (holder instanceof Holder) {
                Holder row = (Holder) holder;
                row.rtHsRatings.setRating(userRatings.get(position).getRatings());
                if (userRatings.get(position).getRatingsDate() != null) {
//                    Calendar calendar = Calendar.getInstance();
//                    calendar.setTime(userRatings.get(position).getRatingsDate());
//                    String ampm = calendar.get(Calendar.AM_PM) == Calendar.AM ? " AM" : " PM";
                    //String txt = calendar.get(Calendar.DAY_OF_MONTH) + " - " + calendar.get(Calendar.MONTH) + 1 + " - " + calendar.get(Calendar.YEAR) + "  " + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + ampm;
                    SimpleDateFormat formatter = new SimpleDateFormat("dd - MMMM - yyyy", Locale.US);
                    String s = formatter.format(userRatings.get(position).getRatingsDate());
                    row.tvRatingsDate.setText(s);

                }
                if (userRatings.get(position).getRatingsCategory() != null) {
                    if (userRatings.get(position).getRatingsCategory().getCategory() != null) {
                        if (userRatings.get(position).getRatingsCategory().getCategory().getName() != null) {
                            row.tvRatingsCatTitle.setText(userRatings.get(position).getRatingsCategory().getCategory().getName());
                        }
                    }
                }
                String rt = Util.get().generateUserStatusFromRatings(userRatings.get(position).getRatings()) + " ( " + userRatings.get(position).getRatings() + " )";
                row.tvRatingsStatus.setText(rt);
                switch ((int) userRatings.get(position).getRatings()) {
                    case 1:
                        row.tvRatingsStatus.setTextColor(Color.RED);
                        break;
                    case 2:
                        row.tvRatingsStatus.setTextColor(Color.YELLOW);
                        break;
                    case 3:
                        row.tvRatingsStatus.setTextColor(Color.parseColor("#E57373"));
                        break;
                    case 4:
                        row.tvRatingsStatus.setTextColor(Color.parseColor("#B71C1C"));
                        break;
                    case 5:
                        row.tvRatingsStatus.setTextColor(Color.BLUE);
                        break;
                    case 6:
                        row.tvRatingsStatus.setTextColor(Color.MAGENTA);
                        break;
                    case 7:
                        row.tvRatingsStatus.setTextColor(Color.parseColor("#4527A0"));
                        break;
                    case 8:
                        row.tvRatingsStatus.setTextColor(Color.parseColor("#4CAF50"));
                        break;
                    case 9:
                        row.tvRatingsStatus.setTextColor(Color.parseColor("#00E676"));
                        break;
                    case 10:
                        row.tvRatingsStatus.setTextColor(Color.GREEN);
                        break;
                    default:
                        row.tvRatingsStatus.setTextColor(Color.WHITE);
                        break;
                }
            }
        }

        @Override
        public long getItemId(int i) {
            return userRatings.get(i).getUserRatingsId();
        }

        @Override
        public int getItemCount() {
            if (userRatings == null || userRatings.size() == 0) {
                return 1;
            }
            return userRatings.size();
        }


        public class Holder extends BaseHolder {
            RatingBar rtHsRatings;
            TextView tvRatingsDate, tvRatingsCatTitle, tvRatingsStatus;

            public Holder(View itemView) {
                super(itemView);
                rtHsRatings = itemView.findViewById(R.id.rtHsRatings);
                tvRatingsDate = itemView.findViewById(R.id.tvRatingsDate);
                tvRatingsCatTitle = itemView.findViewById(R.id.tvRatingsCatTitle);
                tvRatingsStatus = itemView.findViewById(R.id.tvRatingsStatus);
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
