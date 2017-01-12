package com.framgia.photoeditor.ui.previewimage;

import com.framgia.photoeditor.ui.base.BaseView;

import java.util.List;

/**
 * Created by Nhahv on 1/12/2017.
 * <></>
 */
public interface PreviewImageContract {
    interface View extends BaseView {
        void updateAdapter(List<String> pathImages, int position);
    }

    interface Presenter {
        void getListImage();
    }
}
