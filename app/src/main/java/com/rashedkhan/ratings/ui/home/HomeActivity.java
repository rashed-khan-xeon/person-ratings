package com.rashedkhan.ratings.ui.home;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.gson.JsonObject;
import com.rashedkhan.ratings.R;
import com.rashedkhan.ratings.common.BaseActivity;
import com.rashedkhan.ratings.config.ApiUrl;
import com.rashedkhan.ratings.config.ConfigKeys;
import com.rashedkhan.ratings.core.RatingsApplication;
import com.rashedkhan.ratings.core.RtClients;
import com.rashedkhan.ratings.data.implementation.HttpRepository;
import com.rashedkhan.ratings.data.model.User;
import com.rashedkhan.ratings.ui.home.auth.LoginActivity;
import com.rashedkhan.ratings.ui.home.feature.FeatureActivity;
import com.rashedkhan.ratings.ui.home.feature.FeatureTypeFragment;
import com.rashedkhan.ratings.ui.home.history.HistoryActivity;
import com.rashedkhan.ratings.ui.home.profile.EditProfileFragment;
import com.rashedkhan.ratings.ui.home.profile.ProfileFragment;
import com.rashedkhan.ratings.ui.home.search.SearchFragment;
import com.rashedkhan.ratings.ui.home.setting.SettingFragment;
import com.rashedkhan.ratings.util.Util;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchFragment.Transfer,  EditProfileFragment.Update, HomeContract.HomeView {
    ActionBarDrawerToggle toggle;
    CircleImageView civEfProfilePicHeader;
    Dialog dialog;
    private HomePresenter presenter;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        presenter = new HomePresenter(this, new HttpRepository(this));
        configToolbar();
        goToHome();
        initViewComponents();
        if (!Util.get().isNetworkAvailable(this)) {
            Util.get().showToastMsg(this, "No Network Available !");
        }
    }

    private void configToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        navigationView = findViewById(R.id.nav_view);
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
        MenuItem setting = menu.findItem(R.id.settings);

        if (RatingsApplication.getInstant().getUser().getUserRole().getRoleId() == ConfigKeys.getInstant().ADMIN) {
            setting.setVisible(false);
            Menu navMenu = navigationView.getMenu();
            navMenu.getItem(1).setVisible(false);
            navMenu.getItem(2).setVisible(false);
            navMenu.getItem(3).setVisible(false);
            navMenu.getItem(4).setVisible(false);
        } else {
            Menu navMenu = navigationView.getMenu();
            navMenu.getItem(2).setVisible(false);
        }
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

    private void goToHome() {
        if (RatingsApplication.getInstant().getUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        if (RatingsApplication.getInstant().getUser().getUserRole() != null && RatingsApplication.getInstant().getUser().getUserRole().getRoleId() == ConfigKeys.getInstant().ADMIN) {
            addFragment(FeatureTypeFragment.class);
        } else {
            addFragment(SearchFragment.class);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.homeMenu:
                goToHome();
                break;
            case R.id.profile:
                addFragment(ProfileFragment.class);
                break;
            case R.id.feature:
                addFragment(FeatureActivity.class);
                break;
            case R.id.settingMenu:
                addFragment(SettingFragment.class);
                break;
            case R.id.historyMenu:
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
        // drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.changePassword) {
            changePassword();
        }else if (item.getItemId() == R.id.settings) {
            addFragment(SettingFragment.class);
        } else if (item.getItemId() == R.id.home) {
            goToHome();
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
            getSupportFragmentManager().getFragments().clear();
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
