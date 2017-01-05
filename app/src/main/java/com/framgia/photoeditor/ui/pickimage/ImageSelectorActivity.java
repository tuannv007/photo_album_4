package com.framgia.photoeditor.ui.pickimage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.framgia.photoeditor.R;
import com.framgia.photoeditor.data.model.LocalImageFolder;
import com.framgia.photoeditor.ui.previewimage.PreviewImageActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.framgia.photoeditor.util.Constant.ImageSelector.BUNDLE_IMAGE_FOLDER;
import static com.framgia.photoeditor.util.Constant.ImageSelector.BUNDLE_LIST_IMAGE;
import static com.framgia.photoeditor.util.Constant.ImageSelector.BUNDLE_TYPE_PICK_IMAGE;
import static com.framgia.photoeditor.util.Constant.ImageSelector.SPAN_COUNT;

public class ImageSelectorActivity extends AppCompatActivity
    implements ImageSelectorContract.View, ImageSelectorAdapter.OnPickImageSelected {
    private ImageSelectorPresenter mPresenter;
    private ImageSelectorAdapter mAdapter;
    private LocalImageFolder mImageFolder;
    private int mTypePickImage;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.text_selected_image_done)
    TextView mTextSelectedImageDone;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    public static Intent getProfileIntent(Context context, LocalImageFolder folder, int type) {
        Intent intent = new Intent(context, ImageSelectorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_TYPE_PICK_IMAGE, type);
        bundle.putParcelable(BUNDLE_IMAGE_FOLDER, folder);
        intent.putExtras(bundle);
        return intent;
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mImageFolder = bundle.getParcelable(BUNDLE_IMAGE_FOLDER);
            mTypePickImage = bundle.getInt(BUNDLE_TYPE_PICK_IMAGE);
        }
        if (mImageFolder == null) mImageFolder = new LocalImageFolder();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_selector);
        ButterKnife.bind(this);
        getDataFromIntent();
        mPresenter = new ImageSelectorPresenter(this, mImageFolder);
        mPresenter.getListImage();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.text_selected_image_done)
    void onClickDoneSelectedImage() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(BUNDLE_LIST_IMAGE, mAdapter.getListImageSelected());
        bundle.putInt(BUNDLE_TYPE_PICK_IMAGE, mTypePickImage);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void updateListImage(List<String> pathImage) {
        mAdapter.setListImage(pathImage);
    }

    @Override
    public void onImageSelected(List<String> images) {
        mTextSelectedImageDone.setVisibility(images.size() > 0 ? View.VISIBLE : View.GONE);
        mTextSelectedImageDone.setText(getString(R.string.text_number, images.size()));
    }

    @Override
    public void start() {
        setSupportActionBar(mToolbar);
        setTitle(mImageFolder.getFolderName());
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTextSelectedImageDone.setVisibility(View.GONE);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, SPAN_COUNT));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(true);
        mAdapter = new ImageSelectorAdapter(this, this, mTypePickImage);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void pickSingleImage(String pathImage) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_LIST_IMAGE, pathImage);
        bundle.putInt(BUNDLE_TYPE_PICK_IMAGE, mTypePickImage);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void pickPreviewImage(ArrayList<String> pathImages, int position) {
        startActivity(PreviewImageActivity.getPreviewImageIntent(this, pathImages, position));
    }
}
