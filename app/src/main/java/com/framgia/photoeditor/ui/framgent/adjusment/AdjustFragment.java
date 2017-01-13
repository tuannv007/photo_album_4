package com.framgia.photoeditor.ui.framgent.adjusment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.framgia.photoeditor.R;
import com.framgia.photoeditor.util.Constant;
import com.framgia.photoeditor.util.Util;
import com.framgia.photoeditor.util.UtilImage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.framgia.photoeditor.util.Constant.Bundle.BUNDLE_BITMAP;
import static com.framgia.photoeditor.util.Constant.Size.DATA_SIZE_SEEKBAR;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdjustFragment extends Fragment implements AdjusmentContract.View {
    private Bitmap mBitmap;
    @BindView(R.id.image_edit)
    ImageView mImageEdit;
    @BindView(R.id.linear_feature)
    LinearLayout mLinearFeature;
    @BindView(R.id.linear_feature_brightness)
    LinearLayout mLinearControl;
    @BindView(R.id.seekbar_brightness)
    SeekBar mSeekBarBrightness;
    @BindView(R.id.linear_black_white)
    LinearLayout mLinearBlackWhite;
    private AdjustPresenter mPresenter;
    private int mType;

    public static AdjustFragment newInstance(Bitmap bitmap) {
        AdjustFragment fragment = new AdjustFragment();
        byte[] bytes = Util.convertBitmapToByte(bitmap);
        Bundle bundle = new Bundle();
        bundle.putByteArray(BUNDLE_BITMAP, bytes);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void getDataFromActivity() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            byte[] bytes = bundle.getByteArray(BUNDLE_BITMAP);
            if (bytes == null) return;
            mBitmap = Util.decodeFromByte(bytes);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adjust, container, false);
        ButterKnife.bind(this, view);
        getDataFromActivity();
        mPresenter = new AdjustPresenter(this);
        return view;
    }

    @Override
    public void start() {
        mImageEdit.setImageBitmap(mBitmap);
        mSeekBarBrightness.setProgress(DATA_SIZE_SEEKBAR / 2);
        mSeekBarBrightness.setMax(DATA_SIZE_SEEKBAR);
        mSeekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int index, boolean b) {
                if (mType == Constant.TypeControl.TYPE_BRIGHTNESS) {
                    mImageEdit.setImageBitmap(UtilImage.brightness(mBitmap, index, index));
                } else if (mType == Constant.TypeControl.TYPE_CONTRAST) {
                    mImageEdit.setImageBitmap(Util.createContrast(mBitmap, index));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mType == Constant.TypeControl.TYPE_HUE) {
                    mImageEdit.setImageBitmap(Util.updateHUE(mBitmap, seekBar.getProgress(), 0.1f,
                        0.1f));
                }
            }
        });
    }

    @Override
    public void updateImgBlackWhite(Bitmap bitmap) {
        mImageEdit.setImageBitmap(bitmap);
    }

    @OnClick(
        {R.id.linear_contrast, R.id.linear_hue, R.id.linear_brightness, R.id.linear_black_white})
    void onClick(View view) {
        mLinearFeature.setVisibility(View.GONE);
        switch (view.getId()) {
            case R.id.linear_brightness:
                mType = Constant.TypeControl.TYPE_BRIGHTNESS;
                mLinearControl.setVisibility(View.VISIBLE);
                break;
            case R.id.linear_contrast:
                mType = Constant.TypeControl.TYPE_CONTRAST;
                mLinearControl.setVisibility(View.VISIBLE);
                break;
            case R.id.linear_hue:
                mType = Constant.TypeControl.TYPE_HUE;
                mLinearControl.setVisibility(View.VISIBLE);
                break;
            case R.id.linear_black_white:
                mPresenter.convertImgBlackWhite(mBitmap);
                break;
            default:
                break;
        }
    }
}
