package com.framgia.photoeditor.ui.mergeimage;

import android.graphics.Bitmap;

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
    public boolean saveImage(Bitmap bitmap) {
        return false;
        //// TODO: 1/17/17  
    }

    @Override
    public void handleSave(Bitmap bitmap) {
        // TODO: 1/17/17
    }
}
