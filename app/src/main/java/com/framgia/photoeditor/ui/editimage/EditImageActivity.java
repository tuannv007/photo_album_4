package com.framgia.photoeditor.ui.editimage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.framgia.photoeditor.R;
import com.framgia.photoeditor.data.model.Control;
import com.framgia.photoeditor.ui.changecolor.ChangeColorFragment;
import com.framgia.photoeditor.util.Constant;
import com.framgia.photoeditor.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.framgia.photoeditor.util.Constant.Bundle.BUNDLE_PATH_IMAGE;

public class EditImageActivity extends AppCompatActivity implements EditImageContract.View,
    ControlImageAdapter.OnItemClickListener {
    @BindView(R.id.image_main_screen)
    ImageView mImageEdit;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private ControlImageAdapter mAdapter;
    private Bitmap mBitmapImage;
    private ControlImageAdapter mControlImageAdapter;
    private EditImagePresenter mEditImagePresenter;
    private List<Control> mListControls = new ArrayList<>();
    private String mPathImage;

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
        mEditImagePresenter = new EditImagePresenter(this);
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
        if (requestCode == Constant.REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get(Constant.DATA_CAMERA);
            mImageEdit.setImageBitmap(photo);
        }
    }

    @Override
    public void onItemClickListener(View v, int position) {
        switch (position) {
            case Constant.BLACK_WHITE_IMAGE:
                if (mImageEdit.getDrawable() == null) return;
                Bitmap bitmap = ((BitmapDrawable) mImageEdit.getDrawable()).getBitmap();
                if (mBitmapImage != null) mEditImagePresenter.convertImgBlackWhite(bitmap);
                break;
            case Constant.CHANGE_COLOR:
                ChangeColorFragment changeColorFragment;
                changeColorFragment = ChangeColorFragment.newInstance(mPathImage);
                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_edit_activity, changeColorFragment)
                    .addToBackStack(null).commit();
                break;
            default:
                break;
        }
    }

    @Override
    public void openCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, Constant.REQUEST_CODE_CAMERA);
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
    public void updateImgBlackWhite(Bitmap bitmap) {
        mImageEdit.setImageBitmap(bitmap);
    }

    @Override
    public void start() {
        setSupportActionBar(mToolbar);
        mAdapter = new ControlImageAdapter(getApplicationContext(), this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
            .HORIZONTAL, false));
        mRecyclerView.setAdapter(mAdapter);
        if (mPathImage != null) {
            Glide.with(this)
                .load(mPathImage)
                .into(mImageEdit);
        }
    }

    @Override
    public void updateDataControl() {
        mAdapter.setListControl(getListDataControl());
    }

    @Override
    public List<Control> getListDataControl() {
        mListControls.clear();
        Resources res = getResources();
        TypedArray resource = res.obtainTypedArray(R.array.image_control);
        String[] title =
            getResources().getStringArray(R.array.title_control);
        for (int i = 0; i < resource.length(); i++) {
            resource.recycle();
            mListControls.add(new Control(resource.getResourceId(i, 0), title[i]));
        }
        return mListControls;
    }
}
