package com.framgia.photoeditor.ui.changecolor;

/**
 * Created by tuanbg on 1/10/17.
 */
public class ChangeColorPresenter {
    private ChangeColorContract.View mView;

    public ChangeColorPresenter(ChangeColorContract.View view) {
        mView = view;
        mView.start();
    }
}
