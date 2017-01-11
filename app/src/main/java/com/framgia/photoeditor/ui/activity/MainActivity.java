package com.framgia.photoeditor.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.framgia.photoeditor.R;
import com.framgia.photoeditor.ui.imagefolder.ImageFolderActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.framgia.photoeditor.util.Constant.ImageSelector.DATA_PICK_MULTIPLE_IMAGE;
import static com.framgia.photoeditor.util.Constant.ImageSelector.DATA_PICK_SINGLE_IMAGE;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.linear_photo)
    void onClickPhotoEditor() {
        startActivity(ImageFolderActivity.getProfileIntent(this, DATA_PICK_SINGLE_IMAGE));
    }

    @OnClick(R.id.linear_video)
    void onClickVideoMaker() {
        startActivity(ImageFolderActivity.getProfileIntent(this, DATA_PICK_MULTIPLE_IMAGE));
    }
}
