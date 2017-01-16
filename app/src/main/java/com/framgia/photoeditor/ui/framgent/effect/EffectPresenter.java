package com.framgia.photoeditor.ui.framgent.effect;

import java.util.ArrayList;
import java.util.List;

/**
 * <Created by Nhahv on 1/13/2017.
 */
public class EffectPresenter implements EffectContract.Presenter {
    private EffectContract.View mView;
    private List<String> mListNameEffect = new ArrayList<>();
    private List<Integer> mListImageEffect = new ArrayList<>();

    public EffectPresenter(EffectContract.View view) {
        mView = view;
        mView.start();
    }

    @Override
    public void getListEffect() {
        mListNameEffect.addAll(mView.listNameEffect());
        mListImageEffect.addAll(mView.listImageEffect());
        mView.updateAdapter(mListNameEffect, mListImageEffect);
    }
}
