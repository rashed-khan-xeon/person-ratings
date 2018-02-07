package com.review.ratings.ui.home;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.api.Api;
import com.google.gson.JsonObject;
import com.rashedkhan.ratings.R;
import com.review.ratings.common.BaseActivity;
import com.review.ratings.config.ApiUrl;
import com.review.ratings.core.RatingsApplication;
import com.review.ratings.core.RtClients;
import com.review.ratings.data.implementation.HttpRepository;
import com.review.ratings.data.model.User;
import com.review.ratings.ui.home.auth.LoginActivity;
import com.review.ratings.ui.home.history.HistoryActivity;
import com.review.ratings.ui.home.profile.EditProfileFragment;
import com.review.ratings.ui.home.profile.ProfileFragment;
import com.review.ratings.ui.home.search.SearchFragment;
import com.review.ratings.ui.home.setting.SettingFragment;
import com.review.ratings.util.LruBitmapCache;
import com.review.ratings.util.Util;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchFragment.Transfer, EditProfileFragment.Update, HomeContract.HomeView {
    ActionBarDrawerToggle toggle;
    CircleImageView civEfProfilePicHeader;
    private InterstitialAd mInterstitialAd;
    Dialog dialog;
    private HomePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        presenter = new HomePresenter(this, new HttpRepository(this));
        configToolbar();
        addFragment(SearchFragment.class);
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId(getString(R.string.admob_ad_id));

        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        if (!Util.get().isNetworkAvailable(this)) {
            Util.get().showToastMsg(this, "No Network Available !");
        }

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }

    private void configToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        configNavHeader(navigationView.getHeaderView(0));
    }

    private void configNavHeader(View headerView) {
        TextView tvUserName = headerView.findViewById(R.id.tvUserName);
        TextView tvUserEmail = headerView.findViewById(R.id.tvUserEmail);
        User user = RatingsApplication.getInstant().getRatingsPref().getUser();
        civEfProfilePicHeader = headerView.findViewById(R.id.civEfProfilePicHeader);
        if (user != null) {
            tvUserName.setText(user.getFullName());
            tvUserEmail.setText(user.getEmail());

            if (user.getImage() != null) {
                RtClients.getInstance().getImageLoader(this).get(ApiUrl.getInstance().getUserImageUrl(RatingsApplication.getInstant().getRatingsPref().getUser().getImage()), new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        civEfProfilePicHeader.setImageBitmap(response.getBitmap());
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        civEfProfilePicHeader.setImageDrawable(getResources().getDrawable(R.drawable.avatar));
                        Log.d(getClass().getSimpleName(), Arrays.toString(error.getStackTrace()));
                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.home, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.homeMenu:
                addFragment(SearchFragment.class);
                break;
            case R.id.profile:
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                addFragment(ProfileFragment.class);
                break;
            case R.id.settingMenu:
                addFragment(SettingFragment.class);
                break;
            case R.id.historyMenu:
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                startActivity(new Intent(this, HistoryActivity.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.logoutMenu:
                finish();
                RatingsApplication.getInstant().removePreference();
                startActivity(new Intent(this, LoginActivity.class));
                break;
            default:
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawers();
        //    drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.changePassword) {
            changePassword();
        }
        return true;
    }

    private void changePassword() {
        dialog = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.change_password, null);
        dialog.setContentView(view);
        dialog.show();
        EditText etCurrentPassword = view.findViewById(R.id.etCurrentPassword);
        EditText etNewPassword = view.findViewById(R.id.etNewPassword);
        EditText etConfirmNewPassword = view.findViewById(R.id.etConfirmNewPassword);
        Button btnChangePass = view.findViewById(R.id.btnChangePass);
        btnChangePass.setOnClickListener(view1 -> {
            if (TextUtils.isEmpty(etCurrentPassword.getText())) {
                Util.get().showToastMsg(HomeActivity.this, "Please enter current password");
                return;
            }
            if (TextUtils.isEmpty(etNewPassword.getText())) {
                Util.get().showToastMsg(HomeActivity.this, "New Password is required");
                return;
            }
            if (TextUtils.isEmpty(etConfirmNewPassword.getText())) {
                Util.get().showToastMsg(HomeActivity.this, "Confirm New Password is required");
                return;
            }
            if (!etConfirmNewPassword.getText().toString().equals(etNewPassword.getText().toString())) {
                Util.get().showToastMsg(HomeActivity.this, "Password doesn't match !");
                return;
            }
            if (RatingsApplication.getInstant().getUser() == null) {
                return;
            }
            JsonObject jb = new JsonObject();
            jb.addProperty("userId", RatingsApplication.getInstant().getUser().getUserId());
            jb.addProperty("currentPassword", etCurrentPassword.getText().toString());
            jb.addProperty("newPassword", etNewPassword.getText().toString());
            presenter.changePassword(ApiUrl.getInstance().getChangePasswordUrl(), RtClients.getInstance().getGson().toJson(jb));
        });


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

    @Override
    public void updateProfilePicture(Bitmap bitmap) {
        civEfProfilePicHeader.setImageBitmap(bitmap);
    }

    @Override
    public void initViewComponents() {

    }

    @Override
    public void showSuccessMessage(String msg) {
        if (dialog != null) {
            dialog.dismiss();
        }
        Util.get().showToastMsg(this, msg);
    }

    @Override
    public void showErrorMessage(String msg) {
        Util.get().showToastMsg(this, msg);
    }

    @Override
    public void passwordChanged() {
        Util.get().showToastMsg(this, "Password updated !");
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
