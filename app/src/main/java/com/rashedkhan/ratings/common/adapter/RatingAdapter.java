package com.rashedkhan.ratings.common.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.rashedkhan.ratings.R;
import com.rashedkhan.ratings.data.model.RatingSummary;
import com.rashedkhan.ratings.util.Util;

import java.util.List;

/**
 * Created by arifk on 7.2.18.
 */

public class RatingAdapter extends BaseAdapter {
    private List<RatingSummary> ratingSummary;
    private Context context;

    public RatingAdapter(Context context, List<RatingSummary> ratingSummary) {
        this.ratingSummary = ratingSummary;
        this.context = context;
    }

    @Override
    public int getCount() {
        return ratingSummary.size();
    }

    @Override
    public RatingSummary getItem(int i) {
        return ratingSummary.get(i);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vw;
        if (view == null) {
            vw = LayoutInflater.from(context).inflate(R.layout.user_profile_rating_view_list_item, null);
        } else {
            vw = view;
        }
        TextView tvRatItemTitle = vw.findViewById(R.id.tvRatItemTitle);
        TextView tvUserCatStatus = vw.findViewById(R.id.tvUserCatStatus);
        TextView tvRatingsCount = vw.findViewById(R.id.tvRatingsCount);
        RatingBar itemRatings = vw.findViewById(R.id.itemRatings);
        tvRatItemTitle.setText(ratingSummary.get(i).getCategory() == null ? "" : ratingSummary.get(i).getCategory());
        itemRatings.setRating(ratingSummary.get(i).getAvgRatings());
        tvRatingsCount.setText(String.valueOf(ratingSummary.get(i).getAvgRatings()));
        if (ratingSummary.get(i).getCount() > 0) {
            tvRatingsCount.append(String.valueOf(ratingSummary.get(i).getCount()));
        } else {
            itemRatings.setVisibility(View.GONE);
            tvRatingsCount.setText(R.string.nobody_rated);
        }
        tvUserCatStatus.setText(Util.get().generateUserStatusFromRatings(ratingSummary.get(i).getAvgRatings()));
        switch ((int) ratingSummary.get(i).getAvgRatings()) {
            case 1:
                tvUserCatStatus.setTextColor(Color.RED);
                break;
            case 2:
                tvUserCatStatus.setTextColor(Color.YELLOW);
                break;
            case 3:
                tvUserCatStatus.setTextColor(Color.parseColor("#E57373"));
                break;
            case 4:
                tvUserCatStatus.setTextColor(Color.parseColor("#B71C1C"));
                break;
            case 5:
                tvUserCatStatus.setTextColor(Color.BLUE);
                break;
            case 6:
                tvUserCatStatus.setTextColor(Color.MAGENTA);
                break;
            case 7:
                tvUserCatStatus.setTextColor(Color.parseColor("#4527A0"));
                break;
            case 8:
                tvUserCatStatus.setTextColor(Color.parseColor("#4CAF50"));
                break;
            case 9:
                tvUserCatStatus.setTextColor(Color.parseColor("#00E676"));
                break;
            case 10:
                tvUserCatStatus.setTextColor(Color.GREEN);
                break;
            default:
                tvUserCatStatus.setTextColor(Color.WHITE);
                break;
        }
        return vw;
    }
}