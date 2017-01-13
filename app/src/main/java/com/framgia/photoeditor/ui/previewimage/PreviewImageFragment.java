package com.framgia.photoeditor.ui.previewimage;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.photoeditor.R;
import com.framgia.photoeditor.ui.widget.ZoomImageView;
import com.framgia.photoeditor.util.UtilImage;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.framgia.photoeditor.util.Constant.Bundle.BUNDLE_PATH_IMAGE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreviewImageFragment extends Fragment {
    @BindView(R.id.image_edit)
    ZoomImageView mImagePreview;
    private String mPathImage;

    public static PreviewImageFragment newInstance(String pathImage) {
        PreviewImageFragment fragment = new PreviewImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PATH_IMAGE, pathImage);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void getDataFromActivity() {
        Bundle bundle = getArguments();
        if (bundle != null) mPathImage = bundle.getString(BUNDLE_PATH_IMAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preview_image, container, false);
        ButterKnife.bind(this, view);
        getDataFromActivity();
        Point point = getDisplaySize();
        try {
            mImagePreview.setImageBitmap(UtilImage.decodeBitmap(mPathImage, point.x, point.y));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }

    public Point getDisplaySize() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return new Point(metrics.widthPixels, metrics.heightPixels);
    }
}
