package com.framgia.photoeditor.ui.editimage;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.framgia.photoeditor.ui.base.BasePresenter;
import com.framgia.photoeditor.data.model.Control;
import com.framgia.photoeditor.ui.base.BaseView;

import java.util.List;

/**
 * Created by tuanbg on 1/9/17.
 * <></>
 */
public interface EditImageContract {
    interface View extends BaseView {
        void updateDataControl();
        List<Control> getListDataControl();
        void openCamera();
        void saveOnSuccess();
        void saveError();
        void updateImgBlackWhite(Bitmap bitmap);
        Point getDisplaySize();
        void updateImage(Bitmap bitmap);
    }

    interface Presenter extends BasePresenter {
        void convertImgBlackWhite(Bitmap bitmap);
        void handleSave(Bitmap bitmap);
        void bitmapFromFile();
        void getBitmapBrightness(Bitmap bitmap, int index);
    }
}
