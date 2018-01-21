package com.review.ratings.ui.home.auth;

import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.rashedkhan.ratings.R;
import com.review.ratings.common.BaseActivity;
import com.review.ratings.config.ApiUrl;
import com.review.ratings.core.RatingsApplication;
import com.review.ratings.core.RtClients;
import com.review.ratings.data.implementation.HttpRepository;
import com.review.ratings.data.model.RatingsPref;
import com.review.ratings.data.model.User;
import com.review.ratings.data.model.UserType;
import com.review.ratings.ui.home.HomeActivity;
import com.review.ratings.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class LoginActivity extends BaseActivity implements AuthContract.AuthView {
    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private AuthContract.AuthPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        presenter = new AuthPresenter(this, new HttpRepository(this));

        initViewComponents();

    }

    private void attemptLogin() {
        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            Util.get().showProgress(this, true, "Processing...");
            JSONObject job = new JSONObject();
            try {
                job.put("email", email);
                job.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("Login info", "Login data:" + job.toString());
            presenter.doLogin(ApiUrl.getInstance().getUserLoginUrl(), job.toString());
            hideKeyboard();
        }
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    public void gotoSignUp(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    @Override
    public void initViewComponents() {
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            }
            return false;
        });

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(view -> attemptLogin());
    }

    @Override
    public void showSuccessMessage(String msg) {
        Util.get().showToastMsg(this, msg);
    }

    @Override
    public void showErrorMessage(String msg) {
        Util.get().showProgress(this, false, null);
        Util.get().showToastMsg(this, msg);
    }

    @Override
    public void signUpSuccess(User user) {
//for signup activity
    }

    @Override
    public void loginSuccess(User user) {
        Log.e("User", "User after login:" + user.toString());
        Util.get().showProgress(this, false, null);
        showSuccessMessage("Login successful !");
        RatingsPref pref = new RatingsPref();
        pref.setUser(user);
        RatingsApplication.getInstant().setPreference(pref);
        startActivity(new Intent(this, HomeActivity.class));
    }

    @Override
    public void setUserTypesToView(List<UserType> userTypes) {

    }
}

