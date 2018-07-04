package com.review.ratings.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.rashedkhan.ratings.R;
import com.review.ratings.common.BaseActivity;
import com.review.ratings.config.ApiUrl;
import com.review.ratings.core.RatingsApplication;
import com.review.ratings.core.ResponseListener;
import com.review.ratings.data.implementation.HttpRepository;
import com.review.ratings.data.model.RatingsPref;
import com.review.ratings.data.model.User;
import com.review.ratings.data.repository.IHttpRepository;
import com.review.ratings.ui.home.auth.LoginActivity;
import com.review.ratings.ui.home.auth.VerificationActivity;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SplashActivity extends BaseActivity {
    private TextView tvWelcomeTxt;
    private ImageView ivRatingsIcon;
    Animation animation, animation2, animation3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);
        init();
        processLocation();

    }

    private void processLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Please allow location service", Toast.LENGTH_SHORT).show();
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(SplashActivity.this, permissions, 1);
            return;
        } else {
            processAnimation();
            String locationProvider = LocationManager.NETWORK_PROVIDER;
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
            String code = getCountryName(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            RatingsApplication.getInstant().setCountryCode(code);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                processLocation();
                processAnimation();
            } else {
                Toast.makeText(getApplicationContext(), "To use this system you must grant your location permissions", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void init() {
        tvWelcomeTxt = findViewById(R.id.tvWelcomeTxt);
        ivRatingsIcon = findViewById(R.id.ivRatingsIcon);
        animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.bounce);
        animation3 = AnimationUtils.loadAnimation(this, R.anim.scale_with_fade);

    }

    private void processAnimation() {
        ivRatingsIcon.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tvWelcomeTxt.startAnimation(animation2);
                tvWelcomeTxt.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tvWelcomeTxt.startAnimation(animation3);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (RatingsApplication.getInstant().isLogin()) {
                    if (!RatingsApplication.getInstant().getUser().hasVerified()) {
                        startActivity(new Intent(SplashActivity.this, VerificationActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    } else {
                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        IHttpRepository repository = new HttpRepository(this);
        if (RatingsApplication.getInstant().getUser() != null) {
            String url = ApiUrl.getInstance().getUserDetailsUrl(RatingsApplication.getInstant().getUser().getUserId());
            Map<String, String> header = new HashMap<>();
            header.put("Content-Type", "application/json");
            header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));
            repository.get(url, User.class, header, new ResponseListener<User>() {
                @Override
                public void success(User response) {
                    RatingsPref pref = RatingsApplication.getInstant().getRatingsPref();
                    pref.setUser(response);
                    RatingsApplication.getInstant().setPreference(pref);
                }

                @Override
                public void error(Throwable error) {
                    RatingsApplication.getInstant().removePreference();
                }
            });

        }
    }

    public String getCountryName(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String countryCode = null;
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            countryCode = obj.getCountryCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return countryCode;
    }
}
