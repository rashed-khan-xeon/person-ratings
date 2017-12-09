package com.review.test.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showProgress(boolean show, @Nullable String msg) {
        if (show) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(msg);
            progressDialog.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Horizontal);
            progressDialog.create();
            progressDialog.show();
        } else {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    }

    public void showToastMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void animateActivity() {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}
