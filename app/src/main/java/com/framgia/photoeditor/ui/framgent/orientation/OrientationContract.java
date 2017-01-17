package com.framgia.photoeditor.ui.framgent.orientation;

import com.framgia.photoeditor.ui.base.BaseView;

/**
 * Created by nhahv on 1/17/2017.
 * <></>
 */
public interface OrientationContract {
    interface View extends BaseView {
        void rotateLeft();
        void rotateRight();
        void flipHorizontal();
        void flipVertical();
    }

    interface Presenter {
    }
}
