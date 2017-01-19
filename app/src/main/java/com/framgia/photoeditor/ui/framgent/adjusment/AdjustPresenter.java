package com.framgia.photoeditor.ui.framgent.adjusment;

import android.graphics.Bitmap;

import com.framgia.photoeditor.util.Util;
import com.framgia.photoeditor.util.UtilImage;

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
        Observable.just(Util.convertImageToBlackWhite(bitmap))
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<Bitmap>() {
                @Override
                public void onNext(Bitmap bitmap) {
                    if (bitmap == null) return;
                    mView.updateImgBlackWhite(bitmap);
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

    @Override
    public void setBitmapContrast(Bitmap bitmap, float index) {
        Observable.just(Util.changeContrast(bitmap, index, 1))
            .subscribeOn(Schedulers.newThread())
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

    @Override
    public void setBitmapHue(Bitmap mBitmap, int progress, float v, float v1) {
        Observable.just(UtilImage.changeHue(mBitmap, progress))
            .subscribeOn(Schedulers.newThread())
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
}
