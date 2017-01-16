package com.framgia.photoeditor.ui.framgent.cropimage;

import android.graphics.Bitmap;

/**
 * Created by nhahv on 1/16/2017.
 * <></>
 */
public class CropImagePresenter implements CropImageContract.Presenter {
    private final CropImageContract.View mView;
    private Bitmap mBitmap;

    public CropImagePresenter(CropImageContract.View view, Bitmap bitmap) {
        mView = view;
        mBitmap = bitmap;
        mView.start();
    }
}
