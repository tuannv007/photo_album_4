package com.framgia.photoeditor.ui.framgent.adjusment;

import android.graphics.Bitmap;

import com.framgia.photoeditor.ui.base.BaseView;

/**
 * Created by tuanbg on 1/13/17.
 */
public interface AdjustContract {
    interface View extends BaseView {
        void updateImgBlackWhite(Bitmap bitmap);
        void hideButtonBlackWhite();
        void updateImage(Bitmap bitmap);
        void hideProgressDialog();
    }

    interface Presenter {
        void convertImgBlackWhite(Bitmap bitmap);
        void setBitmapContrast(Bitmap bitmap, float index);
        void setBitmapHue(Bitmap mBitmap, int progress, float v, float v1);
    }
}
