package com.framgia.photoeditor.ui.framgent.effect;

import com.framgia.photoeditor.ui.base.BaseView;

import java.util.List;

/**
 * <Created by Nhahv on 1/13/2017.
 */
public interface EffectContract {
    interface View extends BaseView {
        void updateAdapter(List<String> effect, List<Integer> images);
        List<String> listNameEffect();
        List<Integer> listImageEffect();
    }

    interface Presenter {
        void getListEffect();
    }
}
