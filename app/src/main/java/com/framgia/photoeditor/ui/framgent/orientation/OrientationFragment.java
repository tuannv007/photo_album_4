package com.framgia.photoeditor.ui.framgent.orientation;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.framgia.photoeditor.R;
import com.framgia.photoeditor.ui.base.FragmentView;
import com.framgia.photoeditor.ui.editimage.EditImageActivity;
import com.framgia.photoeditor.ui.framgent.HighlightFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nhahv on 1/17/2017.
 * <></>
 */
public class OrientationFragment extends Fragment
    implements OrientationContract.View, FragmentView {
    private OrientationPresenter mPresenter;
    private final int ANGLE_ROTATE = 90;
    private Bitmap mBitmap;
    @BindView(R.id.image_orientation)
    ImageView mImageEdit;
    private HighlightFragment.EventBackToActivity mEventBackToActivity;

    public static OrientationFragment newInstance() {
        return new OrientationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orientation, container, false);
        ButterKnife.bind(this, view);
        mPresenter = new OrientationPresenter(this);
        return view;
    }

    @Override
    public void start() {
        mBitmap = EditImageActivity.sBitmap;
        mImageEdit.setImageBitmap(mBitmap);
    }

    @Override
    public void setEventBackToActivity(HighlightFragment.EventBackToActivity event) {
        mEventBackToActivity = event;
    }

    @Override
    public void rotateLeft() {
        mImageEdit.setRotation(mImageEdit.getRotation() + ANGLE_ROTATE);
    }

    @Override
    public void rotateRight() {
        mImageEdit.setRotation(mImageEdit.getRotation() - ANGLE_ROTATE);
    }

    @Override
    public void flipHorizontal() {
        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);
        mBitmap =
            Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix,
                false);
        mBitmap.setDensity(DisplayMetrics.DENSITY_DEFAULT);
        mImageEdit.setImageBitmap(mBitmap);
    }

    @Override
    public void flipVertical() {
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);
        mBitmap =
            Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix,
                false);
        mBitmap.setDensity(DisplayMetrics.DENSITY_DEFAULT);
        mImageEdit.setImageBitmap(mBitmap);
    }

    @OnClick(R.id.image_flip_vertical)
    void clickFlipVertical() {
        flipVertical();
    }

    @OnClick(R.id.image_flip_horizontal)
    void clickFlipHorizontal() {
        flipHorizontal();
    }

    @OnClick(R.id.image_rotate_left)
    void clickRotateLeft() {
        rotateLeft();
    }

    @OnClick(R.id.image_rotate_right)
    void clickRotateRight() {
        rotateRight();
    }

    @Override
    public void saveBitmap() {
        EditImageActivity.sBitmap = ((BitmapDrawable) mImageEdit.getDrawable()).getBitmap();
        if (mEventBackToActivity != null) mEventBackToActivity.backToActivity();
    }
}
