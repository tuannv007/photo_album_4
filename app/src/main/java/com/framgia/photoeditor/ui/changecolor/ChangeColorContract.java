package com.framgia.photoeditor.ui.changecolor;

import com.framgia.photoeditor.ui.base.BaseView;

/**
 * Created by tuanbg on 1/10/17.
 */
public interface ChangeColorContract {
    interface View extends BaseView {
        void changeColor();
        void getBitmapImage();
    }
}
