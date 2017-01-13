package com.framgia.photoeditor.ui.imgtovideo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.framgia.photoeditor.R;
import com.framgia.photoeditor.data.model.Image;
import com.framgia.photoeditor.util.Constant;
import com.framgia.photoeditor.util.Util;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.framgia.photoeditor.util.Constant.ImageSelector.BUNDLE_PATH_IMAGE;
import static com.framgia.photoeditor.util.Constant.RESIZE_IMAGE;
import static com.framgia.photoeditor.util.Constant.Request.SELECT_IMAGE;
import static com.framgia.photoeditor.util.Constant.TYPE_AUDIO;

public class SlideEditorActivity extends AppCompatActivity implements SlideEditorContract.View {
    private static final String OUTPUT_MUSIC = "output_with_music.mp4";
    @BindView(R.id.image_button_music)
    ImageButton mImageButtonMusic;
    @BindView(R.id.video_view)
    VideoView mVideoView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private ProgressDialog mProgressDialog;
    private MediaController mMediaControls;
    private List<Image> mImages = new ArrayList<>();
    private List<String> mImageName = new ArrayList<>();
    private SlideEditorPresenter mPresenter;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_editor);
        FacebookSdk.sdkInitialize(getApplicationContext());
        ButterKnife.bind(this);
        mPresenter = new SlideEditorPresenter(this, this);
        start();
    }

    public static Intent getDataImageIntent(Context context, ArrayList<String> pathImage) {
        Intent intent = new Intent(context, SlideEditorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(BUNDLE_PATH_IMAGE, pathImage);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public void getName() {
        Intent intent = getIntent();
        List<String> imagePath = intent.getStringArrayListExtra(BUNDLE_PATH_IMAGE);
        for (int i = 0; i < imagePath.size(); i++) {
            File file = new File(imagePath.get(i));
            mImageName.add(file.getName());
            mImages.add(new Image(imagePath.get(i), mImageName.get(i)));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image_to_video, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share_fb) initFacebook();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void removeResizeImage() {
        for (Image image : mImages) {
            String newFile = new File(image.getPath()).getParent() + RESIZE_IMAGE + image.getName();
            mPresenter.deleteTempFile(newFile);
        }
    }

    @Override
    public void initProgress() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getResources().getString(R.string.loading));
    }

    @Override
    public void initMediaPlayer() {
        if (mMediaControls == null) mMediaControls = new MediaController(this);
        mVideoView.setMediaController(mMediaControls);
    }

    @Override
    public void onExcuteFinish(String output) {
        mVideoView.setVideoPath(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +
                "/" + output);
        mVideoView.start();
        mProgressDialog.dismiss();
    }

    @OnClick(R.id.image_button_music)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_button_music:
                Intent intent = new Intent();
                intent.setType(TYPE_AUDIO);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, SELECT_IMAGE);
                break;
            default:
                break;
        }
    }

    @Override
    public void dismisDialog() {
        mProgressDialog.dismiss();
        mProgressDialog.setCancelable(false);
        Util.showToast(getApplicationContext(), R.string.video_saved);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null && requestCode == SELECT_IMAGE) {
            String musicUri = data.getData().getPath();
            mPresenter.executeFFMPEG(mPresenter.mergeAudio(musicUri, OUTPUT_MUSIC), true,
                OUTPUT_MUSIC);
        }
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.show();
            }
        });
    }

    @Override
    public void start() {
        setSupportActionBar(mToolbar);
        initProgress();
        initMediaPlayer();
        mPresenter.convertVideo(mImages);
    }

    @Override
    public void initFacebook() {
        mCallbackManager = CallbackManager.Factory.create();
        List<String> permissionNeeds = Arrays.asList(Constant.PREMISTION_FACEBOOK);
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.logInWithPublishPermissions(this, permissionNeeds);
        loginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mPresenter.shareVideo();
                Util.showToast(getApplicationContext(), R.string.share_fb_success);
            }

            @Override
            public void onCancel() {
                Util.showToast(getApplicationContext(), R.string.video_share_error);
            }

            @Override
            public void onError(FacebookException exception) {
                Util.showToast(getApplicationContext(), R.string.connect_fb_error);
            }
        });
    }

    @Override
    public Uri getUriVideo() {
        String path;
        Uri uri = null;
        try {
            Field pathField = VideoView.class.getDeclaredField(Constant.PATH_FIELD_FB);
            pathField.setAccessible(true);
            path = String.valueOf(pathField.get(mVideoView));
            File file = new File(path);
            uri = Uri.fromFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Thread.currentThread().interrupt();
    }
}
