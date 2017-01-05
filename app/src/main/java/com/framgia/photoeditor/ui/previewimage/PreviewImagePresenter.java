package com.framgia.photoeditor.ui.previewimage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nhahv on 1/12/2017.
 * <></>
 */
public class PreviewImagePresenter implements PreviewImageContract.Presenter {
    private final PreviewImageContract.View mView;
    private List<String> mListImage = new ArrayList<>();
    private int mPosition;

    public PreviewImagePresenter(PreviewImageContract.View view, List<String> pathImages,
                                 int position) {
        mView = view;
        mListImage.addAll(pathImages);
        mPosition = position;
        mView.start();
    }

    @Override
    public void getListImage() {
        mView.updateAdapter(mListImage, mPosition);
    }
}
