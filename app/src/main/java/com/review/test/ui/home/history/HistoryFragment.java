package com.review.test.ui.home.history;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.review.test.R;
import com.review.test.data.implementation.HttpRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment implements HistoryContract.HistoryView {

    private View historyView;
    private HistoryContract.HistoryPresenter presenter;
    private ViewPager vpHistoryPager;
    private TabLayout tbHistoryTab;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        historyView = inflater.inflate(R.layout.fragment_history, container, false);
        presenter = new HistoryPresenter(this, new HttpRepository(getActivity()));
        return historyView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        initViewComponents();
        organizePager();
    }

    private void organizePager() {
        HistoryTabPagerAdapter pagerAdapter = new HistoryTabPagerAdapter(getActivity().getSupportFragmentManager());
        pagerAdapter.addFragment(new ReviewHistoryFragment(), "Reviews");
        pagerAdapter.addFragment(new RatingsHistoryFragment(), "Ratings");
        vpHistoryPager.setAdapter(pagerAdapter);
        pagerAdapter.notifyDataSetChanged();
        tbHistoryTab.setupWithViewPager(vpHistoryPager);
        // vpHistoryPager.setPageTransformer(true,new viewPagerAnimation)
    }

    @Override
    public void initViewComponents() {
        vpHistoryPager = historyView.findViewById(R.id.vpHistoryPager);
        tbHistoryTab = historyView.findViewById(R.id.tbHistoryTab);
    }

    @Override
    public void showSuccessMessage(String msg) {

    }

    @Override
    public void showErrorMessage(String msg) {

    }
}
