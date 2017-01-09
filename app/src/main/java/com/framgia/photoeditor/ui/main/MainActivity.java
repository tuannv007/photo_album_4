package com.framgia.photoeditor.ui.main;

import android.app.Activity;
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
import android.widget.Toast;

import com.framgia.photoeditor.R;
import com.framgia.photoeditor.data.model.Control;
import com.framgia.photoeditor.util.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainContract.View,
    ControlImageAdapter.OnItemClickListener {
    @BindView(R.id.image_main_screen)
    ImageView mImageMainScreen;
    @BindView(R.id.recycler_control)
    RecyclerView mRecyclerControl;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private ControlImageAdapter mControlImageAdapter;
    private MainPresenter mMainPresenter;
    private List<Control> mListControls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mMainPresenter = new MainPresenter(this);
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
            mImageMainScreen.setImageBitmap(photo);
        }
    }

    @Override
    public void onItemClickListener(View v, int position) {
        switch (position) {
            case Constant.OPEN_CAMERA:
                openCamera();
                break;
            case Constant.BLACK_WHITE_IMAGE:
                if (mImageMainScreen.getDrawable() == null) return;
                Bitmap bitmap = ((BitmapDrawable) mImageMainScreen.getDrawable()).getBitmap();
                if (bitmap != null) mMainPresenter.convertImgBlackWhite(bitmap);
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
        Toast.makeText(getApplicationContext(),
            getResources().getString(R.string.save_sucsess),
            Toast.LENGTH_LONG).show();
    }

    @Override
    public void saveError() {
        Toast.makeText(getApplicationContext(),
            getString(R.string.save_error),
            Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateImgBlackWhite(Bitmap bitmap) {
        mImageMainScreen.setImageBitmap(bitmap);
    }

    @Override
    public void start() {
        setSupportActionBar(mToolbar);
        mControlImageAdapter = new ControlImageAdapter(getApplicationContext(), this);
        mRecyclerControl.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
            .HORIZONTAL, false));
        mRecyclerControl.setAdapter(mControlImageAdapter);
    }

    @Override
    public void updateDataControl() {
        mControlImageAdapter.setListControl(getListDataControl());
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
