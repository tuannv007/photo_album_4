package com.framgia.photoeditor.ui.framgent.orientation;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.framgia.photoeditor.R;
import com.framgia.photoeditor.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.framgia.photoeditor.util.Constant.Bundle.BUNDLE_BITMAP;

/**
 * Created by nhahv on 1/17/2017.
 * <></>
 */
public class OrientationFragment extends Fragment implements OrientationContract.View {
    private OrientationPresenter mPresenter;
    private final int ANGLE_ROTATE = 90;
    private Bitmap mBitmap;
    @BindView(R.id.image_orientation)
    ImageView mImageEdit;

    public static OrientationFragment newInstance(Bitmap bitmap) {
        OrientationFragment fragment = new OrientationFragment();
        byte[] bytes = Util.convertBitmapToByte(bitmap);
        Bundle bundle = new Bundle();
        bundle.putByteArray(BUNDLE_BITMAP, bytes);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void getDataFromActivity() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            byte[] bytes = bundle.getByteArray(BUNDLE_BITMAP);
            if (bytes == null) return;
            mBitmap = Util.decodeFromByte(bytes);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orientation, container, false);
        ButterKnife.bind(this, view);
        getDataFromActivity();
        mPresenter = new OrientationPresenter(this);
        return view;
    }

    @Override
    public void start() {
        mImageEdit.setImageBitmap(mBitmap);
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
}
