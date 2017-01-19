package com.framgia.photoeditor.ui.mergeimage;

import android.graphics.Bitmap;

import com.framgia.photoeditor.util.Util;

/**
 * Created by tuanbg on 1/17/17.
 */
public class MergeImagePresenter implements MergeImageContract.Presenter {
    private MergeImageContract.View mView;

    public MergeImagePresenter(MergeImageContract.View view) {
        mView = view;
        mView.start();
    }

    @Override
    public void handleSave(Bitmap bitmap) {
        if (Util.saveImage(bitmap)) mView.saveSuccess();
        else mView.saveError();
    }
}
