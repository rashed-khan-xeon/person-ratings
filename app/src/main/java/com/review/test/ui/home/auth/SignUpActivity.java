package com.review.test.ui.home.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.review.test.R;
import com.review.test.common.BaseActivity;
import com.review.test.common.ViewInitializer;

public class SignUpActivity extends BaseActivity implements ViewInitializer {
    private EditText etFullName, etEmail, etPhoneNumber, etPassword, etConPassword;
    private Spinner spnProfession;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initViewComponent();
        configToolbar();
        btnSignUp.setOnClickListener(view -> {
            signUp();
        });

    }

    private void signUp() {
        boolean isValid = validateInputData();
        if (isValid)
            showToastMsg("Sign up processing...");
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
        if (!etConPassword.getText().toString().equals(etConPassword.getText().toString())) {
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

    @Override
    public void initViewComponent() {
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etPassword = findViewById(R.id.etPassword);
        etConPassword = findViewById(R.id.etConPassword);
        spnProfession = findViewById(R.id.spnProfession);
        btnSignUp = findViewById(R.id.btnSignUp);
    }

    public void gotoLogin(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        animateActivity();
    }
}
