package com.framgia.photoeditor.ui.previewimage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.framgia.photoeditor.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.framgia.photoeditor.util.Constant.Bundle.BUNDLE_PATH_IMAGE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreviewImageFragment extends Fragment {
    @BindView(R.id.image_edit)
    ImageView mImagePreview;
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
        View view = inflater.inflate(R.layout.fragment_highlight, container, false);
        ButterKnife.bind(this, view);
        getDataFromActivity();
        Glide.with(this)
            .load(mPathImage)
            .into(mImagePreview);
        return view;
    }
}
