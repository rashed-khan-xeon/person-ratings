package com.review.test.ui.home.auth;

import android.content.Intent;

import android.os.AsyncTask;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.review.test.R;
import com.review.test.common.BaseActivity;
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


        if (RatingsApplication.getInstant().isLogin()) {
            startActivity(new Intent(this, HomeActivity.class));
        }
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
    public void signUpSuccess() {
//for signup activity
    }

    @Override
    public void loginSuccess(User user) {
        Util.get().showProgress(this, false, null);
        showSuccessMessage("Login successful !");
        RatingsPref pref = new RatingsPref();
        pref.setUser(user);
        RatingsApplication.getInstant().setPreference(pref);
        startActivity(new Intent(this, HomeActivity.class));
    }
}

