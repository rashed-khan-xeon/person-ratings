package com.review.test.ui.home.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.review.test.R;
import com.review.test.common.ViewInitializer;
import com.review.test.common.adapter.ExpandedListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProfileFragment extends Fragment implements ViewInitializer {
    private Context context;
    private ExpandedListView lvOverallRatings;

    public ProfileFragment() {
    }

    View profileView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        profileView = inflater.inflate(R.layout.fragment_profile, container, false);
        initViewComponent();
        configView();
        return profileView;
    }

    private void configView() {
        List<RateListItem> items = new ArrayList<>();
        int i = 0;
        for (String item : getResources().getStringArray(R.array.review_category)) {
            RateListItem rateListItem = new RateListItem();
            i++;
            rateListItem.itemId = i;
            rateListItem.itemName = item;
            rateListItem.ratings = new Random().nextInt(5) + 1;
            items.add(rateListItem);
        }

        RatingViewAdapter adapter = new RatingViewAdapter(items);
        lvOverallRatings.setAdapter(adapter);
        lvOverallRatings.setExpanded(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void initViewComponent() {
        lvOverallRatings = profileView.findViewById(R.id.lvOverallRatings);

    }

    private class RatingViewAdapter extends BaseAdapter {
        private List<RateListItem> rateListItems;

        public RatingViewAdapter(List<RateListItem> rateListItems) {
            this.rateListItems = rateListItems;
        }

        @Override
        public int getCount() {
            return rateListItems.size();
        }

        @Override
        public RateListItem getItem(int i) {
            return rateListItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return rateListItems.get(i).itemId;
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
            RatingBar itemRatings = vw.findViewById(R.id.itemRatings);
            tvRatItemTitle.setText(rateListItems.get(i).itemName);
            itemRatings.setRating(rateListItems.get(i).ratings);
            return vw;
        }
    }

    private class RateListItem {
        public int itemId;
        public String itemName;
        public float ratings;
    }
}
