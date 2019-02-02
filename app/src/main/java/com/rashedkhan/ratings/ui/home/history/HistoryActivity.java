package com.rashedkhan.ratings.ui.home.history;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.rashedkhan.ratings.R;
import com.rashedkhan.ratings.data.implementation.HttpRepository;

public class HistoryActivity extends AppCompatActivity implements HistoryContract.HistoryView {

    private HistoryContract.HistoryPresenter presenter;
    private ViewPager vpHistoryPager;
    private TabLayout tbHistoryTab;
    HistoryTabPagerAdapter pagerAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        presenter = new HistoryPresenter(this, new HttpRepository(this));
        initViewComponents();
        organizePager();
    }


    @Override
    public void initViewComponents() {
        toolbar = findViewById(R.id.toolbar);
        vpHistoryPager = findViewById(R.id.vpHistoryPager);
        tbHistoryTab = findViewById(R.id.tbHistoryTab);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void showSuccessMessage(String msg) {

    }

    private void organizePager() {
        pagerAdapter = new HistoryTabPagerAdapter(getSupportFragmentManager());
        pagerAdapter.clear();
        pagerAdapter.addFragment(new ReviewHistoryFragment(), "Reviews");
        pagerAdapter.addFragment(new RatingsHistoryFragment(), "Ratings");
        vpHistoryPager.setAdapter(pagerAdapter);
        tbHistoryTab.setupWithViewPager(vpHistoryPager);
        // vpHistoryPager.setPageTransformer(true,new viewPagerAnimation)
    }

    @Override
    public void showErrorMessage(String msg) {

    }
}
