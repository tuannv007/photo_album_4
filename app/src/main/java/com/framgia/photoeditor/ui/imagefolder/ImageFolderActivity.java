package com.framgia.photoeditor.ui.imagefolder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.framgia.photoeditor.R;
import com.framgia.photoeditor.data.model.LocalImageFolder;
import com.framgia.photoeditor.ui.pickimage.ImageSelectorActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.framgia.photoeditor.util.Constant.ImageFolder.REQUEST_SELECTOR_IMAGE;
import static com.framgia.photoeditor.util.Constant.ImageSelector.BUNDLE_LIST_IMAGE;
import static com.framgia.photoeditor.util.Constant.ImageSelector.BUNDLE_TYPE_PICK_IMAGE;
import static com.framgia.photoeditor.util.Constant.ImageSelector.DATA_PICK_MULTIPLE_IMAGE;
import static com.framgia.photoeditor.util.Constant.ImageSelector.DATA_PICK_SINGLE_IMAGE;

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
    public void pickSingleImage(String string) {
    }

    @Override
    public void pickMultipleImage(ArrayList<String> stringArrayList) {
    }

    @Override
    public void updateListImageFolder(List<LocalImageFolder> imageFolders) {
        mAdapter.setListImageFolder(imageFolders);
    }

    @Override
    public void pickImageFolder(LocalImageFolder imageFolder) {
        startActivityForResult(
            ImageSelectorActivity.getProfileIntent(this, imageFolder, mTypePickImage),
            REQUEST_SELECTOR_IMAGE);
    }
}
