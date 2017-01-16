package com.framgia.photoeditor.ui.framgent.adjusment;

import android.graphics.Bitmap;

import com.framgia.photoeditor.ui.base.BasePresenter;
import com.framgia.photoeditor.ui.base.BaseView;

/**
 * Created by tuanbg on 1/13/17.
 */
public interface AdjustContract {
    interface View extends BaseView {
        void updateImgBlackWhite(Bitmap bitmap);
        void hideButtonBlackWhite();
    }

    interface Presenter extends BasePresenter {
        void convertImgBlackWhite(Bitmap bitmap);
    }
}
