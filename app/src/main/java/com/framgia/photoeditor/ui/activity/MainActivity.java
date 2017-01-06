package com.framgia.photoeditor.ui.activity;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;

import com.framgia.photoeditor.R;
import com.framgia.photoeditor.data.model.Control;
import com.framgia.photoeditor.ui.adapter.ControlImageAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.image_main_screen)
    ImageView mImageMainScreen;
    @BindView(R.id.recycler_control)
    RecyclerView mRecyclerControl;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private ControlImageAdapter mControlImageAdapter;
    private List<Control> mListControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        mListControl = new ArrayList<>();
        initData();
        mControlImageAdapter = new ControlImageAdapter(this, mListControl);
        mRecyclerControl.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
            .HORIZONTAL, false));
        mRecyclerControl.setAdapter(mControlImageAdapter);
    }

    private void initData() {
        Resources res = getResources();
        TypedArray resource = res.obtainTypedArray(R.array.image_control);
        String[] title = getResources().getStringArray(R.array.title_control);
        for (int i = 0; i < resource.length(); i++) {
            resource.recycle();
            mListControl.add(new Control(resource.getResourceId(i, 0), title[i]));
        }
    }
}
