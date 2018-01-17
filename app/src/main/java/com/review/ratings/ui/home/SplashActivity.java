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
import com.review.ratings.core.RatingsApplication;
import com.review.ratings.ui.home.auth.LoginActivity;

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
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
