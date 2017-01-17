package com.framgia.photoeditor.ui.imagefolder;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.framgia.photoeditor.R;
import com.framgia.photoeditor.data.model.LocalImageFolder;
import com.framgia.photoeditor.ui.editimage.EditImageActivity;
import com.framgia.photoeditor.ui.pickimage.ImageSelectorActivity;
import com.framgia.photoeditor.util.RequestPermissionUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.framgia.photoeditor.util.RequestPermissionUtils.PERMISSION_CALLBACK_CAMERA;
import static com.framgia.photoeditor.util.Constant.ImageSelector.BUNDLE_LIST_IMAGE;
import static com.framgia.photoeditor.util.Constant.ImageSelector.BUNDLE_TYPE_PICK_IMAGE;
import static com.framgia.photoeditor.util.Constant.ImageSelector.DATA_PICK_MULTIPLE_IMAGE;
import static com.framgia.photoeditor.util.Constant.ImageSelector.DATA_PICK_SINGLE_IMAGE;
import static com.framgia.photoeditor.util.Constant.Request.REQUEST_SELECTOR_IMAGE;

public class ImageFolderActivity extends AppCompatActivity
    implements ImageFolderAdapter.EventImageFolder, ImageFolderContract.View {
    private ImageFolderAdapter mAdapter;
    private ImageFolderPresent mPresenter;
    private int mTypePickImage;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    public static Intent getProfileIntent(Context context, int typePickImage) {
        Intent intent = new Intent(context, ImageFolderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_TYPE_PICK_IMAGE, typePickImage);
        intent.putExtras(bundle);
        return intent;
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) mTypePickImage = bundle.getInt(BUNDLE_TYPE_PICK_IMAGE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_folder);
        ButterKnife.bind(this);
        getDataFromIntent();
        mPresenter = new ImageFolderPresent(this);
        mPresenter.getListImageFolder(this);
    }

    @Override
    public void start() {
        setSupportActionBar(mToolbar);
        setTitle(R.string.title_image_folder);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setNestedScrollingEnabled(true);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new ImageFolderAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECTOR_IMAGE && resultCode == RESULT_OK) {
            if (data == null) return;
            Bundle bundle = data.getExtras();
            if (bundle == null) return;
            int typePickImage = bundle.getInt(BUNDLE_TYPE_PICK_IMAGE);
            switch (typePickImage) {
                case DATA_PICK_SINGLE_IMAGE:
                    pickSingleImage(bundle.getString(BUNDLE_LIST_IMAGE));
                    break;
                case DATA_PICK_MULTIPLE_IMAGE:
                    pickMultipleImage(bundle.getStringArrayList(BUNDLE_LIST_IMAGE));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab_camera)
    void clickOpenCamera() {
        if (checkCameraHardware(this) && !RequestPermissionUtils.requestCamera(this)) {
            // TODO: 1/17/2017  open camera to capture image
        }
    }

    @Override
    public void pickSingleImage(String string) {
        startActivity(EditImageActivity.getEditImageIntent(this, string));
    }

    @Override
    public void pickMultipleImage(ArrayList<String> stringArrayList) {
    }

    @Override
    public void updateListImageFolder(List<LocalImageFolder> imageFolders) {
        mAdapter.setListImageFolder(imageFolders);
    }

    @Override
    public boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    @Override
    public void pickImageFolder(LocalImageFolder imageFolder) {
        startActivityForResult(
            ImageSelectorActivity.getProfileIntent(this, imageFolder, mTypePickImage),
            REQUEST_SELECTOR_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CAMERA
            && grantResults.length > 0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // TODO: 1/17/2017  open camera to capture image
        }
    }
}
