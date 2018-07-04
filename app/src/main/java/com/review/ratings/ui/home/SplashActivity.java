package com.review.ratings.ui.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

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

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends BaseActivity {
    private TextView tvWelcomeTxt;
    private ImageView ivRatingsIcon;
    Animation animation, animation2, animation3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);
        tvWelcomeTxt = findViewById(R.id.tvWelcomeTxt);
        ivRatingsIcon = findViewById(R.id.ivRatingsIcon);
        animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.bounce);
        animation3 = AnimationUtils.loadAnimation(this, R.anim.scale_with_fade);
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
}
