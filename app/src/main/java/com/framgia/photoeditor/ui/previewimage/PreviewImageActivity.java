package com.framgia.photoeditor.ui.previewimage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.framgia.photoeditor.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.framgia.photoeditor.util.Constant.Bundle.BUNDLE_POSITION;
import static com.framgia.photoeditor.util.Constant.ImageSelector.BUNDLE_LIST_IMAGE;

public class PreviewImageActivity extends AppCompatActivity implements PreviewImageContract.View {
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    private PreviewImagePresenter mPresenter;
    private PreviewPagerAdapter mAdapter;
    private List<String> mListPathImage = new ArrayList<>();
    private int mPosition;

    public static Intent getPreviewImageIntent(Context context, ArrayList<String> pathImage,
                                               int position) {
        Intent intent = new Intent(context, PreviewImageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(BUNDLE_LIST_IMAGE, pathImage);
        bundle.putInt(BUNDLE_POSITION, position);
        intent.putExtras(bundle);
        return intent;
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mListPathImage.addAll(bundle.getStringArrayList(BUNDLE_LIST_IMAGE));
            mPosition = bundle.getInt(BUNDLE_POSITION);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_image);
        ButterKnife.bind(this);
        getDataFromIntent();
        mPresenter = new PreviewImagePresenter(this, mListPathImage, mPosition);
        mPresenter.getListImage();
    }

    @Override
    public void start() {
        mAdapter = new PreviewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
    }

    @Override
    public void updateAdapter(List<String> pathImages, int position) {
        mAdapter.setListImage(pathImages);
        mViewPager.setCurrentItem(position);
    }

    private class PreviewPagerAdapter extends FragmentPagerAdapter {
        private List<String> mListImage = new ArrayList<>();

        public PreviewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PreviewImageFragment.newInstance(mListPathImage.get(position));
        }

        @Override
        public int getCount() {
            return mListImage == null ? 0 : mListImage.size();
        }

        private void setListImage(List<String> pathImages) {
            mListImage.clear();
            mListImage.addAll(pathImages);
            notifyDataSetChanged();
        }
    }
}
