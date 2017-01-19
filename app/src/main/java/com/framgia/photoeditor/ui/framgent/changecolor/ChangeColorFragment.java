package com.framgia.photoeditor.ui.framgent.changecolor;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.framgia.photoeditor.R;
import com.framgia.photoeditor.ui.base.FragmentView;
import com.framgia.photoeditor.ui.editimage.EditImageActivity;
import com.framgia.photoeditor.ui.framgent.HighlightFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tuanbg on 1/10/17.
 */
public class ChangeColorFragment extends Fragment
    implements ChangeColorContract.View, SeekBar.OnSeekBarChangeListener, FragmentView {
    @BindView(R.id.seek_bar_color_red)
    SeekBar mSbColorRed;
    @BindView(R.id.seek_bar_color_green)
    SeekBar mSbColorGreen;
    @BindView(R.id.seek_bar_color_blue)
    SeekBar mSbColorBlue;
    @BindView(R.id.image_change_color)
    ImageView mImageChangeColor;
    private Bitmap mAfterBitmap;
    private Paint mPaint;
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private ChangeColorPresenter mPresenter;
    private HighlightFragment.EventBackToActivity mEventBackToActivity;

    public static ChangeColorFragment newInstance() {
        return new ChangeColorFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_color, container, false);
        ButterKnife.bind(this, view);
        mPresenter = new ChangeColorPresenter(this);
        return view;
    }

    @Override
    public void changeColor() {
        float progressR = mSbColorRed.getProgress() / 128f;
        float progressG = mSbColorGreen.getProgress() / 128f;
        float progressB = mSbColorBlue.getProgress() / 128f;
        float[] src = new float[]{
            progressR, 0, 0, 0, 0,
            0, progressG, 0, 0, 0,
            0, 0, progressB, 0, 0,
            0, 0, 0, 128, 0};
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(src);
        mPaint.setColorFilter(new ColorMatrixColorFilter(src));
        mCanvas.drawBitmap(mBitmap, new Matrix(), mPaint);
        mImageChangeColor.setImageBitmap(mAfterBitmap);
    }

    @Override
    public void start() {
        mBitmap = EditImageActivity.sBitmap;
        mImageChangeColor.setImageBitmap(mBitmap);
        mSbColorRed.setOnSeekBarChangeListener(this);
        mSbColorGreen.setOnSeekBarChangeListener(this);
        mSbColorBlue.setOnSeekBarChangeListener(this);
        mAfterBitmap = Bitmap
            .createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), mBitmap.getConfig());
        mCanvas = new Canvas(mAfterBitmap);
        mPaint = new Paint();
    }

    @Override
    public void setEventBackToActivity(HighlightFragment.EventBackToActivity event) {
        mEventBackToActivity = event;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        changeColor();
    }

    @Override
    public void saveBitmap() {
        EditImageActivity.sBitmap = ((BitmapDrawable) mImageChangeColor.getDrawable()).getBitmap();
        if (mEventBackToActivity != null) mEventBackToActivity.backToActivity();
    }
}

