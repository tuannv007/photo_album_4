package com.framgia.photoeditor.ui.editimage;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.annotation.NonNull;

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
    public void bitmapFromFile() {
        getBitmapFromFile()
            .subscribeOn(Schedulers.newThread())
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
}
