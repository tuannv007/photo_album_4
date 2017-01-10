package com.framgia.photoeditor.ui.main;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.framgia.photoeditor.util.Util;

/**
 * Created by tuanbg on 1/9/17.
 */
public class MainPresenter implements MainContract.Presenter {
    private MainContract.View mView;

    public MainPresenter(@NonNull MainContract.View mainView) {
        mView = mainView;
        mainView.start();
    }

    @Override
    public boolean saveImage(Bitmap bitmap) {
        return Util.saveImage(bitmap);
    }

    @Override
    public void convertImgBlackWhite(Bitmap bitmap) {
        new ConvertImgToBlackWhite().execute(bitmap);
    }

    @Override
    public void handleSave(Bitmap bitmap) {
        if (saveImage(bitmap)) mView.saveOnSuccess();
        else mView.saveError();
    }

    public class ConvertImgToBlackWhite extends AsyncTask<Bitmap, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Bitmap... params) {
            return Util.convertImageToBlackWhite(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            mView.updateImgBlackWhite(bitmap);
        }
    }
}
