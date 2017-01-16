package com.framgia.photoeditor.ui.mergeimage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;

import com.framgia.photoeditor.R;
import com.framgia.photoeditor.data.model.Control;
import com.framgia.photoeditor.data.model.CustomMergeImage;
import com.framgia.photoeditor.ui.editimage.EditImageActivity;
import com.framgia.photoeditor.ui.imagefolder.ImageFolderActivity;
import com.framgia.photoeditor.util.Constant;
import com.framgia.photoeditor.util.RequestPermissionUtils;
import com.framgia.photoeditor.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.framgia.photoeditor.util.Constant.Bundle.BUNDLE_PATH_IMAGE;
import static com.framgia.photoeditor.util.Constant.ImageSelector.BUNDLE_LIST_IMAGE;
import static com.framgia.photoeditor.util.Constant.ImageSelector.BUNDLE_TYPE_START_MERGE;
import static com.framgia.photoeditor.util.Constant.ImageSelector.DATA_PICK_SINGLE_IMAGE;
import static com.framgia.photoeditor.util.Constant.Request.REQUEST_SELECTOR_IMAGE;

/**
 * Created by tuanbg on 1/16/17.
 */
public class MergeImageActivity extends AppCompatActivity
    implements MergeImageAdapter.OnItemClickListener,
    MergeImageContract.View, CustomMergeImage.EventImageView {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.relative_root_imageview)
    RelativeLayout mRelativeRoot;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private List<Control> mLayoutItems = new ArrayList<>();
    private MergeImageAdapter mAdapter;
    private String mPathImage;
    private MergeImagePresenter mPresenter;
    private CustomMergeImage mCustomMergeImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collage);
        ButterKnife.bind(this);
        mPresenter = new MergeImagePresenter(this);
    }

    public static Intent getMegreImageIntent(Context context, String pathImage) {
        Intent intent = new Intent(context, EditImageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PATH_IMAGE, pathImage);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public void start() {
        setSupportActionBar(mToolbar);
        mAdapter = new MergeImageAdapter(this, this);
        initData();
        mAdapter.setListControl(mLayoutItems);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
            .HORIZONTAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mCustomMergeImage = new CustomMergeImage(this);
        mCustomMergeImage.setEvent(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECTOR_IMAGE && resultCode == RESULT_OK) {
            if (data == null) return;
            mPathImage = data.getStringExtra(BUNDLE_LIST_IMAGE);
            mCustomMergeImage.setBitmap(Util.convertPathToBitmap(mPathImage));
        }
    }

    @Override
    public void initData() {
        mLayoutItems.add(new Control(R.drawable.ic_collage1, getString(R.string.title_collage_1)));
        mLayoutItems.add(new Control(R.drawable.ic_collage2, getString(R.string.title_collage_2)));
        mLayoutItems.add(new Control(R.drawable.ic_collage3, getString(R.string.title_collage_3)));
        mLayoutItems.add(new Control(R.drawable.ic_collage4, getString(R.string.title_collage_4)));
        mLayoutItems.add(new Control(R.drawable.ic_collage5, getString(R.string.title_collage_5)));
    }

    @Override
    public void onClickImage(int position) {
        mRelativeRoot.removeAllViews();
        switch (position) {
            case Constant.LAYOUT_OLD:
                mCustomMergeImage.setNumberLayout(Constant.LAYOUT_OLD);
                break;
            case Constant.LAYOUT_ONE:
                mCustomMergeImage.setNumberLayout(Constant.LAYOUT_ONE);
                break;
            case Constant.LAYOUT_TWO:
                mCustomMergeImage.setNumberLayout(Constant.LAYOUT_TWO);
                break;
            case Constant.LAYOUT_THREE:
                mCustomMergeImage.setNumberLayout(Constant.LAYOUT_THREE);
                break;
            case Constant.LAYOUT_FOUR:
                mCustomMergeImage.setNumberLayout(Constant.LAYOUT_FOUR);
                break;
            default:
                break;
        }
        mRelativeRoot.addView(mCustomMergeImage);
    }

    @Override
    public void clickPickImage() {
        if (!RequestPermissionUtils.requestStorage(this)) {
            startActivityForResult(
                ImageFolderActivity.getImageFolderIntent(this, DATA_PICK_SINGLE_IMAGE,
                    BUNDLE_TYPE_START_MERGE), REQUEST_SELECTOR_IMAGE);
        }
    }
}
