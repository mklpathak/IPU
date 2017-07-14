package com.jesushghar.uss.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.jesushghar.uss.R;

import butterknife.Bind;
import butterknife.ButterKnife;



public class StartupActivity extends AppCompatActivity implements Animation.AnimationListener{

    Animation animation;
    @Bind(R.id.start_image) ImageView startImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        ButterKnife.bind(this);

        // Hide the status bar.
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start_anim);

        animation.setAnimationListener(this);

        startImage.setAnimation(animation);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Intent intent = new Intent(this, HomeActivity.class );
        startActivity(intent);
        finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }


}
