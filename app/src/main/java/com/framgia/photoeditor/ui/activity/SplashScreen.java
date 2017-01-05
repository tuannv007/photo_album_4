package com.framgia.photoeditor.ui.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.framgia.photoeditor.R;
import com.framgia.photoeditor.ui.util.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tuanbg on 1/5/17.
 */
public class SplashScreen extends AppCompatActivity {
    private final String FONT_BSC = "fonts/font_bsc.ttf";
    @BindView(R.id.text_flash_screen)
    TextView mTextFlashScreen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), FONT_BSC);
        mTextFlashScreen.setTypeface(typeface);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, Constant.SPLASH_TIME_OUT);
    }
}
