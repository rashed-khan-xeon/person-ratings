package com.review.test.ui.home.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.JsonObject;
import com.review.test.R;
import com.review.test.common.BaseActivity;
import com.review.test.common.ViewInitializer;
import com.review.test.config.ApiUrl;
import com.review.test.core.RatingsApplication;
import com.review.test.core.RtClients;
import com.review.test.data.implementation.HttpRepository;
import com.review.test.data.model.RatingsPref;
import com.review.test.data.model.User;
import com.review.test.ui.home.HomeActivity;
import com.review.test.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends BaseActivity implements AuthContract.AuthView {
    private EditText etFullName, etEmail, etPhoneNumber, etPassword, etConPassword;
    private Spinner spnProfession;
    private Button btnSignUp;
    private AuthContract.AuthPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        presenter = new AuthPresenter(this, new HttpRepository(this));
        initViewComponents();
        configToolbar();
        btnSignUp.setOnClickListener(view -> {
            signUp();
        });

    }

    private void signUp() {
        boolean isValid = validateInputData();
        if (isValid) {
            JsonObject user = new JsonObject();
            user.addProperty("fullName", etFullName.getText().toString());
            user.addProperty("phoneNumber", etPhoneNumber.getText().toString());
            user.addProperty("email", etEmail.getText().toString());
            user.addProperty("password", Util.get().md5(etPassword.getText().toString()));
            String bodyData = RtClients.getInstance().getGson().toJson(user);
            presenter.doSignUp(ApiUrl.getInstance().getUserSignUpUrl(), bodyData);
            Util.get().showProgress(this, true, "Processing...");
        }

    }

    private boolean validateInputData() {
        if (TextUtils.isEmpty(etFullName.getText())) {
            etFullName.setError("Full name is required !");
            return false;
        }
        if (TextUtils.isEmpty(etEmail.getText())) {
            etEmail.setError("Email address is required !");
            return false;
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText()).matches()) {
                etEmail.setError("Invalid email address ! ");
                return false;
            }
        }
        if (TextUtils.isEmpty(etPhoneNumber.getText())) {
            etPhoneNumber.setError("Phone number is required !");
            return false;
        }
        if (TextUtils.isEmpty(etPassword.getText())) {
            etPassword.setError("Password is required !");
            return false;
        }
        if (TextUtils.isEmpty(etConPassword.getText())) {
            etConPassword.setError("Confirm your password ! ");
            return false;
        }
        if (!etPassword.getText().toString().equals(etConPassword.getText().toString())) {
            etConPassword.setError("Password do not match");
            return false;
        }
        return true;
    }

    private void configToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    public void gotoLogin(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        animateActivity();
    }

    @Override
    public void initViewComponents() {
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etPassword = findViewById(R.id.etPassword);
        etConPassword = findViewById(R.id.etConPassword);
        spnProfession = findViewById(R.id.spnProfession);
        btnSignUp = findViewById(R.id.btnSignUp);
    }

    @Override
    public void showSuccessMessage(String msg) {

    }

    @Override
    public void showErrorMessage(String msg) {
        Util.get().showProgress(this, false, null);
    }

    @Override
    public void signUpSuccess() {
        JSONObject job = new JSONObject();
        try {
            job.put("email", etEmail.getText().toString());
            job.put("password", etPassword.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        presenter.doLogin(ApiUrl.getInstance().getUserLoginUrl(), job.toString());

    }

    @Override
    public void loginSuccess(User user) {
        Util.get().showProgress(this, false, null);
        RatingsPref pref = new RatingsPref();
        pref.setUser(user);
        RatingsApplication.getInstant().setPreference(pref);
        startActivity(new Intent(this, HomeActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return true;
    }
}
