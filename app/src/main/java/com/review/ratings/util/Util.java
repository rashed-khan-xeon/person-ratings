package com.review.ratings.util;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by arifk on 9.12.17.
 */

public class Util {
    private ProgressDialog progressDialog;
    private static Util util = null;

    private Util() {
    }

    public static Util get() {
        if (util == null) {
            util = new Util();
        }
        return util;
    }

    public void showProgress(Context context, boolean show, @Nullable String msg) {
        if (show) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(msg);
            progressDialog.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Horizontal);
            //  progressDialog.create();
            progressDialog.show();
        } else {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    }


    public void showToastMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public String generateUserStatusFromRatings(float rating) {
        switch ((int) rating) {
            case 1:
                return "Worst";
            case 2:
                return "Very Bad";
            case 3:
                return "Bad";
            case 4:
                return "Poor";
            case 5:
                return "Fair";
            case 6:
                return "Good";
            case 7:
                return "Very Good";
            case 8:
                return "Super";
            case 9:
                return "Outstanding";
            case 10:
                return "Great";
            default:
                return "";
        }
    }

    public final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }

    public String getMessage(VolleyError error) {
        if (error.networkResponse == null) {
            return "Connection error !";
        }
        if (error.networkResponse.data == null) {
            return "Connection error !";
        }
        String msg = new String(error.networkResponse.data);
        return msg;
    }
}
