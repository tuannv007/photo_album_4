package com.framgia.photoeditor.ui.framgent.adjusment;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.framgia.photoeditor.R;
import com.framgia.photoeditor.ui.base.FragmentView;
import com.framgia.photoeditor.ui.editimage.EditImageActivity;
import com.framgia.photoeditor.ui.framgent.HighlightFragment;
import com.framgia.photoeditor.util.Constant;
import com.framgia.photoeditor.util.Util;
import com.framgia.photoeditor.util.UtilImage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.framgia.photoeditor.util.Constant.Size.DATA_SIZE_SEEKBAR;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdjustFragment extends Fragment implements AdjustContract.View, FragmentView {
    @BindView(R.id.image_edit)
    ImageView mImageEdit;
    @BindView(R.id.linear_feature)
    LinearLayout mLinearFeature;
    @BindView(R.id.linear_feature_brightness)
    LinearLayout mLinearHighlight;
    @BindView(R.id.linear_feature_black_white)
    LinearLayout mLinearBlackWhite;
    @BindView(R.id.seekbar_brightness)
    SeekBar mSeekBarBrightness;
    private AdjustPresenter mPresenter;
    private Bitmap mBitmap;
    private Bitmap mBitmapBlackWhite;
    private int mType;
    private ProgressDialog mProgressDialog;
    private HighlightFragment.EventBackToActivity mEventBackToActivity;

    public static AdjustFragment newInstance() {
        return new AdjustFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adjust, container, false);
        ButterKnife.bind(this, view);
        mPresenter = new AdjustPresenter(this);
        mPresenter.convertImgBlackWhite(mBitmap);
        return view;
    }

    @Override
    public void start() {
        mBitmap = EditImageActivity.sBitmap;
        mImageEdit.setImageBitmap(mBitmap);
        mSeekBarBrightness.setProgress(DATA_SIZE_SEEKBAR / 2);
        mSeekBarBrightness.setMax(DATA_SIZE_SEEKBAR);
        mSeekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int index, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                switch (mType) {
                    case Constant.TypeControl.TYPE_BRIGHTNESS:
                        mImageEdit.setImageBitmap(UtilImage
                            .brightness(mBitmap, seekBar.getProgress(), seekBar.getProgress()));
                        break;
                    case Constant.TypeControl.TYPE_CONTRAST:
                        mPresenter.setBitmapContrast(mBitmap, seekBar.getProgress());
                        break;
                    case Constant.TypeControl.TYPE_HUE:
                        mImageEdit.setImageBitmap(
                            Util.updateHUE(mBitmap, seekBar.getProgress(), 0.1f, 0.1f));
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void setEventBackToActivity(HighlightFragment.EventBackToActivity event) {
        mEventBackToActivity = event;
    }

    @Override
    public void updateImgBlackWhite(Bitmap bitmap) {
        mBitmapBlackWhite = bitmap;
    }

    @OnClick({R.id.image_brightness_cancel, R.id.image_brightness_done})
    void clickBrightness(View view) {
        hideButtonBlackWhite();
        switch (view.getId()) {
            case R.id.image_brightness_cancel:
                mImageEdit.setImageBitmap(mBitmap);
                break;
            case R.id.image_brightness_done:
                mBitmap = ((BitmapDrawable) mImageEdit.getDrawable()).getBitmap();
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.image_undo_black_white, R.id.image_redo_black_white})
    void clickBlackWhite(View view) {
        switch (view.getId()) {
            case R.id.image_redo_black_white:
                mImageEdit.setImageBitmap(mBitmapBlackWhite);
                break;
            case R.id.image_undo_black_white:
                mImageEdit.setImageBitmap(mBitmap);
                break;
            default:
                break;
        }
    }

    @Override
    public void hideButtonBlackWhite() {
        mLinearHighlight.setVisibility(View.GONE);
        mLinearFeature.setVisibility(View.VISIBLE);
        mLinearBlackWhite.setVisibility(View.GONE);
    }

    @Override
    public void updateImage(Bitmap bitmap) {
        hideProgressDialog();
        mImageEdit.setImageBitmap(bitmap);
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setIndeterminate(true);
        }
        if (!mProgressDialog.isShowing()) mProgressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) mProgressDialog.dismiss();
    }

    @OnClick(
        {R.id.linear_contrast, R.id.linear_hue, R.id.linear_brightness, R.id.linear_black_white})
    void onClick(View view) {
        mLinearFeature.setVisibility(View.GONE);
        switch (view.getId()) {
            case R.id.linear_brightness:
                mType = Constant.TypeControl.TYPE_BRIGHTNESS;
                mLinearHighlight.setVisibility(View.VISIBLE);
                break;
            case R.id.linear_contrast:
                mType = Constant.TypeControl.TYPE_CONTRAST;
                mLinearHighlight.setVisibility(View.VISIBLE);
                break;
            case R.id.linear_hue:
                mType = Constant.TypeControl.TYPE_HUE;
                mLinearHighlight.setVisibility(View.VISIBLE);
                break;
            case R.id.linear_black_white:
                mLinearHighlight.setVisibility(View.GONE);
                mLinearBlackWhite.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public void saveBitmap() {
        EditImageActivity.sBitmap = ((BitmapDrawable) mImageEdit.getDrawable()).getBitmap();
        if (mEventBackToActivity != null) mEventBackToActivity.backToActivity();
    }
}
