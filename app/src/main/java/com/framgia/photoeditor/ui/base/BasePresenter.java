package com.framgia.photoeditor.ui.base;

import android.graphics.Bitmap;

/**
 * Created by tuanbg on 1/10/17.
 */
public interface BasePresenter {
    boolean saveImage(Bitmap bitmap);
    void handleSave(Bitmap bitmap);
}
