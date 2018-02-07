package com.review.ratings.ui.home.auth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.Api;
import com.google.gson.JsonObject;
import com.rashedkhan.ratings.R;
import com.review.ratings.common.BaseActivity;
import com.review.ratings.common.adapter.RatingAdapter;
import com.review.ratings.config.ApiUrl;
import com.review.ratings.core.RatingsApplication;
import com.review.ratings.core.ResponseListener;
import com.review.ratings.core.RtClients;
import com.review.ratings.data.implementation.HttpRepository;
import com.review.ratings.data.model.RatingsPref;
import com.review.ratings.data.model.User;
import com.review.ratings.data.repository.IHttpRepository;
import com.review.ratings.ui.home.HomeActivity;
import com.review.ratings.util.Util;

import java.util.HashMap;
import java.util.Map;

public class VerificationActivity extends BaseActivity implements AuthContract.VerifyView {
    private Button btnSendCode, btnVerify;
    private EditText etUserVerifiedCode;
    private TextView tvUserPhoneNumber;
    private LinearLayout llVerifiedCode, llVerifyLayout;
    private AuthContract.VerifyPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        presenter = new AuthPresenter(this, new HttpRepository(this));
        initViewComponents();
        setDataToView();
    }

    private void setDataToView() {
        if (RatingsApplication.getInstant().getUser() != null) {
            tvUserPhoneNumber.setText(String.valueOf(RatingsApplication.getInstant().getUser().getPhoneNumber()));
        }
    }

    @Override
    public void initViewComponents() {
        llVerifyLayout = findViewById(R.id.llVerifyLayout);
        llVerifiedCode = findViewById(R.id.llVerifiedCode);
        etUserVerifiedCode = findViewById(R.id.etUserVerifiedCode);
        tvUserPhoneNumber = findViewById(R.id.tvUserPhoneNumber);
        btnSendCode = findViewById(R.id.btnSendCode);
        btnVerify = findViewById(R.id.btnVerify);
        btnSendCode.setOnClickListener(v -> presenter.sendVerificationCode(ApiUrl.getInstance().getSendCodeUrl(RatingsApplication.getInstant().getUser().getUserId())));
        btnVerify.setOnClickListener(v -> {
            if (TextUtils.isEmpty(etUserVerifiedCode.getText())) {
                Util.get().showToastMsg(this, "Verification code is required !");
                return;
            }
            JsonObject jo = new JsonObject();
            jo.addProperty("userId", String.valueOf(RatingsApplication.getInstant().getUser().getUserId()));
            jo.addProperty("code", etUserVerifiedCode.getText().toString());
            presenter.checkVerificationCode(ApiUrl.getInstance().getCheckCodeUrl(), RtClients.getInstance().getGson().toJson(jo));
        });
    }

    @Override
    public void userVerified() {
        Util.get().showToastMsg(this, "Verified !");
        User user = RatingsApplication.getInstant().getUser();
        user.setHasVerified(1);
        RatingsPref pref = RatingsApplication.getInstant().getRatingsPref();
        pref.setUser(user);
        RatingsApplication.getInstant().setPreference(pref);
        startActivity(new Intent(this, HomeActivity.class));
        animateActivity();
    }

    @Override
    public void userVerificationFailed(String msg) {
        Util.get().showToastMsg(this, msg);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void showSuccessMessage(String msg) {

    }

    @Override
    public void showErrorMessage(String msg) {
        Util.get().showToastMsg(this, msg);
    }

    @Override
    public void msgSent(Object obj) {
        Util.get().showToastMsg(this, "Please check your inbox !");
        llVerifyLayout.setVisibility(View.GONE);
        llVerifiedCode.setVisibility(View.VISIBLE);
    }
}
