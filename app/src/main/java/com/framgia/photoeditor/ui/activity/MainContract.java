package com.framgia.photoeditor.ui.activity;

import android.graphics.Bitmap;

import com.framgia.photoeditor.BaseView;
import com.framgia.photoeditor.data.model.Control;

import java.util.List;

/**
 * Created by tuanbg on 1/9/17.
 */
public interface MainContract {
    interface View extends BaseView<Presenter> {
        void updateDataControl();
        List<Control> getListDataControl();
        void openCamera();
        void saveOnSuccess();
        void saveError();
        void updateImgBlackWhite(Bitmap bitmap);
    }

    interface Presenter {
        boolean saveImage(Bitmap bitmap);
        void convertImgBlackWhite(Bitmap bitmap);
        void handleSave(Bitmap bitmap);
    }
}
