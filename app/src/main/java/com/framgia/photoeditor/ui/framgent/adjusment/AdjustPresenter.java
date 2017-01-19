package com.framgia.photoeditor.ui.framgent.adjusment;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;

import com.framgia.photoeditor.util.Util;
import com.framgia.photoeditor.util.UtilImage;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    public void setBitmapContrast(Bitmap bitmap, int index) {
        changeContrast(bitmap, index)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<Bitmap>() {
                @Override
                public void onNext(Bitmap bitmap) {
                    if (bitmap == null) return;
                    mView.updateImage(bitmap);
                }

                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }
            });
    }

    public Observable<Bitmap> changeContrast(Bitmap bitmap, int index) {
        return Observable.just(Util.createContrast(bitmap, index));
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
