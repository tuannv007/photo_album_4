package com.framgia.photoeditor.ui.changecolor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.framgia.photoeditor.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tuanbg on 1/10/17.
 */
public class ChangeColorFragment extends Fragment implements ChangeColorContract.View {
    private static final String KEY_IMAGE = "KEY_IMAGE";
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
    private Bitmap mBaseBitmap;
    private ChangeColorPresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_color, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mPresenter = new ChangeColorPresenter(this);
    }

    public static ChangeColorFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(KEY_IMAGE, url);
        ChangeColorFragment fragment = new ChangeColorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private SeekBar.OnSeekBarChangeListener seekBarChange = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            changeColor();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        }
    };

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
        mCanvas.drawBitmap(mBaseBitmap, new Matrix(), mPaint);
        mImageChangeColor.setImageBitmap(mAfterBitmap);
    }

    @Override
    public void getBitmapImage() {
        Bundle bundle = getArguments();
        if (bundle == null) return;
        String uri = bundle.getString(KEY_IMAGE);
        mBaseBitmap = BitmapFactory.decodeFile(String.valueOf(new File(uri)));
        mImageChangeColor.setImageBitmap(mBaseBitmap);
    }

    @Override
    public void start() {
        mSbColorRed.setOnSeekBarChangeListener(seekBarChange);
        mSbColorGreen.setOnSeekBarChangeListener(seekBarChange);
        mSbColorBlue.setOnSeekBarChangeListener(seekBarChange);
        getBitmapImage();
        mAfterBitmap = Bitmap.createBitmap(mBaseBitmap.getWidth(),
            mBaseBitmap.getHeight(), mBaseBitmap.getConfig());
        mCanvas = new Canvas(mAfterBitmap);
        mPaint = new Paint();
    }
}

