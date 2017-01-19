package com.framgia.photoeditor.ui.framgent.cropimage;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.framgia.photoeditor.R;
import com.framgia.photoeditor.ui.base.FragmentView;
import com.framgia.photoeditor.ui.editimage.EditImageActivity;
import com.framgia.photoeditor.ui.framgent.HighlightFragment;
import com.framgia.photoeditor.ui.widget.crop.CropBorderView;
import com.framgia.photoeditor.ui.widget.crop.CropImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CropImageFragment extends Fragment implements CropImageContract.View, FragmentView {
    private CropImagePresenter mPresenter;
    private Bitmap mBitmap;
    @BindView(R.id.image_crop)
    CropImageView mCropImageView;
    @BindView(R.id.view_border)
    CropBorderView mBorderView;
    private HighlightFragment.EventBackToActivity mEventBackToActivity;

    public static CropImageFragment newInstance() {
        return new CropImageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crop_image, container, false);
        ButterKnife.bind(this, view);
        mPresenter = new CropImagePresenter(this, mBitmap);
        return view;
    }

    @Override
    public void start() {
        mBitmap = EditImageActivity.sBitmap;
        mCropImageView.setImageBitmap(mBitmap);
    }

    @Override
    public void saveBitmap() {
        cropImage();
        EditImageActivity.sBitmap = ((BitmapDrawable) mCropImageView.getDrawable()).getBitmap();
        if (mEventBackToActivity != null) mEventBackToActivity.backToActivity();
    }

    @Override
    public void setEventBackToActivity(HighlightFragment.EventBackToActivity event) {
        mEventBackToActivity = event;
    }

    @Override
    public void cropImage() {
        RectF clipRectF = mBorderView.getClipRectF();
        Bitmap bitmap = mCropImageView.clip(clipRectF);
        mCropImageView.setImageBitmap(bitmap);
        RelativeLayout.LayoutParams layoutParams =
            new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins((int) clipRectF.left, (int) clipRectF.top, 0, 0);
        mCropImageView.setLayoutParams(layoutParams);
    }
}
