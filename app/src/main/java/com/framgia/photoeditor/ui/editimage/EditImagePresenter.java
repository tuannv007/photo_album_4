package com.framgia.photoeditor.ui.editimage;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.framgia.photoeditor.util.Util;
import com.framgia.photoeditor.util.UtilImage;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by tuanbg on 1/9/17.
 * <></>
 */
public class EditImagePresenter implements EditImageContract.Presenter {
    private EditImageContract.View mView;
    private String mPathImage;

    public EditImagePresenter(@NonNull EditImageContract.View mainView, String pathImage) {
        mView = mainView;
        mPathImage = pathImage;
        mView.start();
    }

    @Override
    public boolean saveImage(Bitmap bitmap) {
        return Util.saveImage(bitmap);
    }



    @Override
    public void handleSave(Bitmap bitmap) {
        if (saveImage(bitmap)) mView.saveOnSuccess();
        else mView.saveError();
    }



    @Override
    public void bitmapFromFile() {
        getBitmapFromFile()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<Bitmap>() {
                @Override
                public void onNext(Bitmap bitmap) {
                    if (bitmap != null) mView.updateImage(bitmap);
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
    public void getBitmapBrightness(Bitmap bitmap, int index) {
        changeBrightness(bitmap, index)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<Bitmap>() {
                @Override
                public void onNext(Bitmap bitmap) {
                    if (bitmap != null) mView.updateImage(bitmap);
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

    public Observable<Bitmap> getBitmapFromFile() {
        Point point = mView.getDisplaySize();
        Bitmap bitmap = null;
        try {
            bitmap = UtilImage.decodeBitmap(mPathImage, point.x, point.y);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Observable.just(bitmap);
    }

    public Observable<Bitmap> changeBrightness(Bitmap mBitmap, int index) {
        return Observable.just(UtilImage.brightness(mBitmap, index, index));
    }
}
