package com.framgia.photoeditor.ui.mergeimage;

import android.graphics.Bitmap;

import com.framgia.photoeditor.ui.base.BaseView;

/**
 * Created by tuanbg on 1/17/17.
 */
public class MergeImageContract {
    interface View extends BaseView {
        void start();
        void initData();
        void saveSuccess();
        void saveError();
    }

    interface Presenter {
        void handleSave(Bitmap bitmap);
    }
}
