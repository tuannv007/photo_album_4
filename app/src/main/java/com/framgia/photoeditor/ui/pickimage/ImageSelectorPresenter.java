package com.framgia.photoeditor.ui.pickimage;

import com.framgia.photoeditor.data.model.LocalImageFolder;

/**
 * Created by Nhahv on 1/7/2017.
 * <></>
 */
public class ImageSelectorPresenter implements ImageSelectorContract.Presenter {
    private ImageSelectorContract.View mView;
    private LocalImageFolder mImageFolder;

    public ImageSelectorPresenter(ImageSelectorContract.View view, LocalImageFolder imageFolder) {
        mView = view;
        mImageFolder = imageFolder;
    }

    @Override
    public void getListImage() {
        mView.updateListImage(mImageFolder.getListImageOfFolder());
    }
}
