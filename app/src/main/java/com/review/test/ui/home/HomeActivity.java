package com.review.test.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.review.test.R;
import com.review.test.common.BaseActivity;
import com.review.test.core.RatingsApplication;
import com.review.test.data.model.User;
import com.review.test.ui.home.auth.LoginActivity;
import com.review.test.ui.home.history.HistoryFragment;
import com.review.test.ui.home.profile.ProfileFragment;
import com.review.test.ui.home.search.SearchFragment;
import com.review.test.ui.home.setting.SettingFragment;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchFragment.Transfer {
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        configToolbar();
        addFragment(SearchFragment.class);
    }

    private void configToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        configNavHeader(navigationView.getHeaderView(0));
    }

    private void configNavHeader(View headerView) {
        TextView tvUserName = headerView.findViewById(R.id.tvUserName);
        TextView tvUserEmail = headerView.findViewById(R.id.tvUserEmail);
        User user = RatingsApplication.getInstant().getRatingsPref().getUser();
        if (user != null) {
            tvUserName.setText(user.getFullName());
            tvUserEmail.setText(user.getEmail());
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
            // super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.homeMenu:
                addFragment(SearchFragment.class);
                break;
            case R.id.profile:
                addFragment(ProfileFragment.class);
                break;
            case R.id.settingMenu:
                addFragment(SettingFragment.class);
                break;
            case R.id.historyMenu:
                addFragment(HistoryFragment.class);
                break;
            case R.id.logoutMenu:
                finish();
                RatingsApplication.getInstant().removePreference();
                startActivity(new Intent(this, LoginActivity.class));
                addFragment(SettingFragment.class);
                break;
            default:
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawers();
        //    drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addFragment(Class targetFragment) {

        try {
            Fragment fragment = (Fragment) targetFragment.newInstance();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            ft.replace(R.id.homeContainer, fragment).commit();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void transferFragment(Fragment fragment) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        ft.replace(R.id.homeContainer, fragment).commit();

    }

}
