package com.framgia.photoeditor.ui.pickimage;

import com.framgia.photoeditor.ui.base.BaseView;

import java.util.List;

/**
 * Created by Nhahv on 1/7/2017.
 * <></>
 */
public interface ImageSelectorContract {
    interface View extends BaseView {
        void updateListImage(List<String> pathImage);
    }

    interface Presenter {
        void getListImage();
    }
}
