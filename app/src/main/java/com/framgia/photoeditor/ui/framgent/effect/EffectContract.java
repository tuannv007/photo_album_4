package com.framgia.photoeditor.ui.framgent.effect;

import android.graphics.Bitmap;

import com.framgia.photoeditor.ui.base.BaseView;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * <Created by Nhahv on 1/13/2017.
 */
public interface EffectContract {
    interface View extends BaseView {
        void updateAdapter(List<String> effect, List<Integer> images);
        List<String> listNameEffect();
        List<Integer> listImageEffect();
        Bitmap takeScreenshot(GL10 gl10);
    }

    interface Presenter {
        void getListEffect();
    }
}
