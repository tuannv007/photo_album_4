package com.framgia.photoeditor.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.framgia.photoeditor.R;
import com.framgia.photoeditor.ui.imagefolder.ImageFolderActivity;
import com.framgia.photoeditor.ui.mergeimage.MergeImageActivity;
import com.framgia.photoeditor.util.RequestPermissionUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.framgia.photoeditor.util.Constant.ImageSelector.BUNDLE_TYPE_START_MAIN;
import static com.framgia.photoeditor.util.RequestPermissionUtils.PERMISSION_CALLBACK_STORAGE;
import static com.framgia.photoeditor.util.Constant.ImageSelector.DATA_PICK_MULTIPLE_IMAGE;
import static com.framgia.photoeditor.util.Constant.ImageSelector.DATA_PICK_SINGLE_IMAGE;
import static com.framgia.photoeditor.util.RequestPermissionUtils.PERMISSION_CALLBACK_STORAGE;

public class MainActivity extends AppCompatActivity {
    private final int TYPE_PHOTO = 1;
    private final int TYPE_VIDEO = 2;
    private int mTypeStartActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.linear_photo)
    void onClickPhotoEditor() {
        mTypeStartActivity = TYPE_PHOTO;
        if (!RequestPermissionUtils.requestStorage(this)) {
            startActivity(ImageFolderActivity.getImageFolderIntent(this, DATA_PICK_SINGLE_IMAGE,
                BUNDLE_TYPE_START_MAIN));
        }
    }

    @OnClick(R.id.linear_video)
    void onClickVideoMaker() {
        mTypeStartActivity = TYPE_VIDEO;
        if (!RequestPermissionUtils.requestStorage(this)) {
            startActivity(ImageFolderActivity
                .getImageFolderIntent(this, DATA_PICK_MULTIPLE_IMAGE, BUNDLE_TYPE_START_MAIN));
        }
    }

    @OnClick(R.id.linear_merge_photo)
    void clickMergeImage() {
        Intent intent = new Intent(this, MergeImageActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_STORAGE && grantResults.length > 0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (mTypeStartActivity) {
                case TYPE_PHOTO:
                    onClickPhotoEditor();
                    break;
                case TYPE_VIDEO:
                    onClickVideoMaker();
                    break;
                default:
                    break;
            }
        }
    }
}
