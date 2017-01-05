package com.framgia.photoeditor.ui.framgent;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MotionEventCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.framgia.photoeditor.R;
import com.framgia.photoeditor.ui.widget.HighLightDrawable;
import com.framgia.photoeditor.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.framgia.photoeditor.util.Constant.Bundle.BUNDLE_BITMAP;
import static com.framgia.photoeditor.util.UtilImage.centerBitmap;

/**
 * A simple {@link Fragment} subclass.
 */
public class HighlightFragment extends Fragment implements View.OnTouchListener {
    private HighLightDrawable mHighLightDrawable;
    private Point mFingerPoint = new Point(200, 200);
    @BindView(R.id.image_edit)
    ImageView mImageEdit;
    private Bitmap mBitmap;

    public static HighlightFragment newInstance(Bitmap bitmap) {
        HighlightFragment fragment = new HighlightFragment();
        byte[] bytes = Util.convertBitmapToByte(bitmap);
        Bundle bundle = new Bundle();
        bundle.putByteArray(BUNDLE_BITMAP, bytes);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void getDataFromActivity() {
        Bundle bundle = getArguments();
        if (bundle == null) return;
        byte[] bytes = bundle.getByteArray(BUNDLE_BITMAP);
        if (bytes == null) return;
        mBitmap = Util.decodeFromByte(bytes);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_highlight, container, false);
        ButterKnife.bind(this, view);
        getDataFromActivity();
        start();
        return view;
    }

    public void start() {
        doHighlightImage();
        mImageEdit.setOnTouchListener(this);
    }

    public void doHighlightImage() {
        mImageEdit.post(new Runnable() {
            @Override
            public void run() {
                mBitmap = centerBitmap(mBitmap, mImageEdit.getWidth(),
                    mImageEdit.getHeight());
                mImageEdit.getLayoutParams().height = mImageEdit.getHeight();
                mImageEdit.getLayoutParams().width = mImageEdit.getWidth();
                mImageEdit.requestLayout();
                mHighLightDrawable =
                    new HighLightDrawable(getResources(), mBitmap, mFingerPoint);
                //set callback when drawable is invalidated
                mHighLightDrawable.setCallback(new Drawable.Callback() {
                    @Override
                    public void invalidateDrawable(@NonNull Drawable drawable) {
                        mImageEdit.setImageDrawable(drawable);
                    }

                    @Override
                    public void scheduleDrawable(@NonNull Drawable drawable,
                                                 @NonNull Runnable runnable, long l) {
                    }

                    @Override
                    public void unscheduleDrawable(@NonNull Drawable drawable,
                                                   @NonNull Runnable runnable) {
                    }
                });
                mImageEdit.setImageDrawable(mHighLightDrawable);
            }
        });
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int pointerIndex = MotionEventCompat.getActionIndex(event);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mFingerPoint.x = (int) MotionEventCompat.getX(event, pointerIndex);
                mFingerPoint.y = (int) MotionEventCompat.getY(event, pointerIndex);
                mHighLightDrawable.setFinger(mFingerPoint);
                mHighLightDrawable.invalidateSelf();
                break;
            default:
                break;
        }
        return true;
    }
}
