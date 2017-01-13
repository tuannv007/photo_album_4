package com.framgia.photoeditor.ui.framgent.adjusment;

import android.graphics.Bitmap;

import com.framgia.photoeditor.ui.base.BasePresenter;
import com.framgia.photoeditor.ui.base.BaseView;

/**
 * Created by tuanbg on 1/13/17.
 */
public interface AdjusmentContract {
    interface View extends BaseView {
        void updateImgBlackWhite(Bitmap bitmap);
    }

    interface Presenter extends BasePresenter {
        void convertImgBlackWhite(Bitmap bitmap);
    }
}
