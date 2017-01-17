package com.framgia.photoeditor.ui.framgent.orientation;

/**
 * Created by nhahv on 1/17/2017.
 * <></>
 */
public class OrientationPresenter implements OrientationContract.Presenter {
    private final OrientationContract.View mView;

    public OrientationPresenter(OrientationContract.View view) {
        mView = view;
        mView.start();
    }
}
