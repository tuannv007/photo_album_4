package com.framgia.photoeditor.ui.framgent.adjusment;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.framgia.photoeditor.util.Util;

/**
 * Created by tuanbg on 1/13/17.
 */
public class AdjustPresenter implements AdjustContract.Presenter {
    private AdjustContract.View mView;

    public AdjustPresenter(AdjustContract.View view) {
        mView = view;
        mView.start();
    }

    @Override
    public void convertImgBlackWhite(Bitmap bitmap) {
        new ImgToBlackWhiteAsync().execute(bitmap);
    }

    @Override
    public boolean saveImage(Bitmap bitmap) {
        return false;
        // TODO: 1/13/17  
    }

    @Override
    public void handleSave(Bitmap bitmap) {
        // TODO: 1/13/17
    }

    public class ImgToBlackWhiteAsync extends AsyncTask<Bitmap, Void, Bitmap> {
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
