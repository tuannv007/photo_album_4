package com.framgia.photoeditor.ui.editimage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.framgia.photoeditor.R;
import com.framgia.photoeditor.data.model.Control;
import com.framgia.photoeditor.ui.base.FragmentView;
import com.framgia.photoeditor.ui.changecolor.ChangeColorFragment;
import com.framgia.photoeditor.ui.framgent.HighlightFragment;
import com.framgia.photoeditor.ui.framgent.adjusment.AdjustFragment;
import com.framgia.photoeditor.ui.framgent.cropimage.CropImageFragment;
import com.framgia.photoeditor.ui.framgent.effect.EffectFragment;
import com.framgia.photoeditor.ui.framgent.orientation.OrientationFragment;
import com.framgia.photoeditor.util.Constant;
import com.framgia.photoeditor.util.Util;
import com.framgia.photoeditor.util.UtilImage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.framgia.photoeditor.util.Constant.Bundle.BUNDLE_PATH_IMAGE;
import static com.framgia.photoeditor.util.Constant.Request.REQUEST_CODE_CAMERA;

public class EditImageActivity extends AppCompatActivity implements EditImageContract.View,
    ControlImageAdapter.OnItemClickListener, HighlightFragment.EventBackToActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.image_edit)
    ImageView mImageEdit;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.linear_edit)
    LinearLayout mLinearEdit;
    public static Bitmap sBitmap;
    private ControlImageAdapter mAdapter;
    private EditImagePresenter mPresenter;
    private List<Control> mListControls = new ArrayList<>();
    private String mPathImage;
    private HighlightFragment mHighlightFragment;
    private AdjustFragment mAdjustFragment;
    private ChangeColorFragment mColorFragment;
    private CropImageFragment mCropImageFragment;
    private OrientationFragment mOrientationFragment;

    public static Intent getEditImageIntent(Context context, String pathImage) {
        Intent intent = new Intent(context, EditImageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PATH_IMAGE, pathImage);
        intent.putExtras(bundle);
        return intent;
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) mPathImage = bundle.getString(BUNDLE_PATH_IMAGE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);
        ButterKnife.bind(this);
        getDataFromIntent();
        mPresenter = new EditImagePresenter(this, mPathImage);
        mPresenter.bitmapFromFile();
        updateDataControl();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get(Constant.DATA_CAMERA);
            mImageEdit.setImageBitmap(photo);
        }
    }

    public void onItemClickListener(int position) {
        Constant.Feature feature = Constant.Feature.values()[position];
        feature.setPosition(position);
        mLinearEdit.setVisibility(View.GONE);
        switch (feature) {
            case FEATURE_EFFECT:
                EffectFragment fragment = EffectFragment.newInstance();
                fragment.setEventBackToActivity(this);
                setFragment(fragment);
                break;
            case FEATURE_COLOR:
                if (mColorFragment == null) {
                    mColorFragment = ChangeColorFragment.newInstance();
                    mColorFragment.setEventBackToActivity(this);
                }
                setFragment(mColorFragment);
                break;
            case FEATURE_ADJUST:
                if (mAdjustFragment == null) {
                    mAdjustFragment = AdjustFragment.newInstance();
                    mAdjustFragment.setEventBackToActivity(this);
                }
                setFragment(mAdjustFragment);
                break;
            case FEATURE_CROP:
                if (mCropImageFragment == null) {
                    mCropImageFragment = CropImageFragment.newInstance();
                    mCropImageFragment.setEventBackToActivity(this);
                }
                setFragment(mCropImageFragment);
                break;
            case FEATURE_HIGHLIGHT:
                if (mHighlightFragment == null) {
                    mHighlightFragment = HighlightFragment.newInstance();
                    mHighlightFragment.setEventBackToActivity(this);
                }
                setFragment(mHighlightFragment);
                break;
            case FEATURE_ORIENTATION:
                if (mOrientationFragment == null) {
                    mOrientationFragment = OrientationFragment.newInstance();
                    mOrientationFragment.setEventBackToActivity(this);
                }
                setFragment(mOrientationFragment);
                break;
            default:
                break;
        }
    }

    @Override
    public void openCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
    }

    @Override
    public void saveOnSuccess() {
        Util.showToast(getApplicationContext(), R.string.save_sucsess);
    }

    @Override
    public void saveError() {
        Util.showToast(getApplicationContext(), R.string.save_error);
    }

    @Override
    public void start() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAdapter = new ControlImageAdapter(getApplicationContext(), this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
            .HORIZONTAL, false));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_done:
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
                if (fragment != null && fragment instanceof FragmentView) {
                    ((FragmentView) fragment).saveBitmap();
                    return true;
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateDataControl() {
        mAdapter.setListControl(getListDataControl());
    }

    @Override
    public List<Control> getListDataControl() {
        mListControls.clear();
        TypedArray resource = getResources().obtainTypedArray(R.array.image_control);
        String[] title = getResources().getStringArray(R.array.title_control);
        int size = resource.length();
        for (int i = 0; i < size; i++) {
            mListControls.add(new Control(resource.getResourceId(i, -1), title[i]));
        }
        return mListControls;
    }

    @Override
    public Point getDisplaySize() {
        return UtilImage.getDisplaySize(this);
    }

    @Override
    public void updateImage(Bitmap bitmap) {
        sBitmap = bitmap;
        mImageEdit.setImageBitmap(bitmap);
    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            mLinearEdit.setVisibility(View.VISIBLE);
        } else super.onBackPressed();
    }

    @Override
    public void backToActivity() {
        mImageEdit.setImageBitmap(sBitmap);
        onBackPressed();
    }
}
