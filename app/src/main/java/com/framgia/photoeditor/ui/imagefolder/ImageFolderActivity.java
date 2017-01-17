package com.framgia.photoeditor.ui.imagefolder;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import com.framgia.photoeditor.ui.mergeimage.MergeImageActivity;
import com.framgia.photoeditor.ui.pickimage.ImageSelectorActivity;
import com.framgia.photoeditor.util.Constant;
import com.framgia.photoeditor.util.RequestPermissionUtils;
import com.framgia.photoeditor.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.framgia.photoeditor.util.Constant.ImageSelector.BUNDLE_LIST_IMAGE;
import static com.framgia.photoeditor.util.Constant.ImageSelector.BUNDLE_TYPE_PICK_IMAGE;
import static com.framgia.photoeditor.util.Constant.ImageSelector.BUNDLE_TYPE_START;
import static com.framgia.photoeditor.util.Constant.ImageSelector.BUNDLE_TYPE_START_MAIN;
import static com.framgia.photoeditor.util.Constant.ImageSelector.BUNDLE_TYPE_START_MERGE;
import static com.framgia.photoeditor.util.Constant.ImageSelector.DATA_PICK_MULTIPLE_IMAGE;
import static com.framgia.photoeditor.util.Constant.ImageSelector.DATA_PICK_SINGLE_IMAGE;
import static com.framgia.photoeditor.util.Constant.Request.REQUEST_CODE_CAMERA;
import static com.framgia.photoeditor.util.Constant.Request.REQUEST_SELECTOR_IMAGE;
import static com.framgia.photoeditor.util.RequestPermissionUtils.PERMISSION_CALLBACK_CAMERA;

public class ImageFolderActivity extends AppCompatActivity
    implements ImageFolderAdapter.EventImageFolder, ImageFolderContract.View {
    private ImageFolderAdapter mAdapter;
    private ImageFolderPresent mPresenter;
    private int mTypePickImage;
    private int mTypeStart;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    public static Intent getImageFolderIntent(Context context, int typePickImage, int typeStart) {
        Intent intent = new Intent(context, ImageFolderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_TYPE_PICK_IMAGE, typePickImage);
        bundle.putInt(BUNDLE_TYPE_START, typeStart);
        intent.putExtras(bundle);
        return intent;
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        mTypePickImage = bundle.getInt(BUNDLE_TYPE_PICK_IMAGE);
        mTypeStart = bundle.getInt(BUNDLE_TYPE_START);
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
        if (resultCode != RESULT_OK) return;
        if (data == null) return;
        switch (requestCode) {
            case REQUEST_SELECTOR_IMAGE:
                Bundle bundle = data.getExtras();
                if (bundle == null) return;
                int typePickImage = bundle.getInt(BUNDLE_TYPE_PICK_IMAGE);
                getTypeImage(bundle, typePickImage);
                break;
            case REQUEST_CODE_CAMERA:
                Bitmap photo = (Bitmap) data.getExtras().get(Constant.DATA_CAMERA);
                String path = Util.saveImageUri(photo);
                startActivity(EditImageActivity.getEditImageIntent(this, path));
                break;
            default:
                break;
        }
    }

    private void getTypeImage(Bundle bundle, int typePickImage) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab_camera)
    void clickOpenCamera() {
        if (checkCameraHardware(this) && !RequestPermissionUtils.requestCamera(this)) {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
        }
    }

    @Override
    public void pickSingleImage(String string) {
        switch (mTypeStart) {
            case BUNDLE_TYPE_START_MAIN:
                startActivity(EditImageActivity.getEditImageIntent(this, string));
                break;
            case BUNDLE_TYPE_START_MERGE:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString(BUNDLE_LIST_IMAGE, string);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void pickSingleImageMerge(String string) {
        startActivity(MergeImageActivity.getMegreImageIntent(this, string));
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
