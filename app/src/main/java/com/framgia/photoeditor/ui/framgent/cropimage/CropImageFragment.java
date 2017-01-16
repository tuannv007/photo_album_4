package com.framgia.photoeditor.ui.framgent.cropimage;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.framgia.photoeditor.R;
import com.framgia.photoeditor.ui.widget.crop.CropBorderView;
import com.framgia.photoeditor.ui.widget.crop.CropImageView;
import com.framgia.photoeditor.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.framgia.photoeditor.util.Constant.Bundle.BUNDLE_BITMAP;

/**
 * A simple {@link Fragment} subclass.
 */
public class CropImageFragment extends Fragment implements CropImageContract.View {
    private CropImagePresenter mPresenter;
    private Bitmap mBitmap;
    @BindView(R.id.image_crop)
    CropImageView mCropImageView;
    @BindView(R.id.view_border)
    CropBorderView mBorderView;

    public static CropImageFragment newInstance(Bitmap bitmap) {
        CropImageFragment fragment = new CropImageFragment();
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
        View view = inflater.inflate(R.layout.fragment_crop_image, container, false);
        ButterKnife.bind(this, view);
        getDataFromActivity();
        mPresenter = new CropImagePresenter(this, mBitmap);
        return view;
    }

    @Override
    public void start() {
        mCropImageView.setImageBitmap(mBitmap);
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
