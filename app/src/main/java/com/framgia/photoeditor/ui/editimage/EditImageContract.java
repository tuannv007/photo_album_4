package com.framgia.photoeditor.ui.editimage;

import android.graphics.Bitmap;
import android.graphics.Point;

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
        void saveOnSuccess();
        void saveError();
        Point getDisplaySize();
        void updateImage(Bitmap bitmap);
    }

    interface Presenter {
        void bitmapFromFile();
    }
}
