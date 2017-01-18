package com.framgia.photoeditor.ui.framgent.effect;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.media.effect.EffectFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.photoeditor.R;
import com.framgia.photoeditor.data.model.Effects;
import com.framgia.photoeditor.ui.base.FragmentView;
import com.framgia.photoeditor.ui.editimage.EditImageActivity;
import com.framgia.photoeditor.ui.framgent.HighlightFragment;
import com.framgia.photoeditor.util.GLToolbox;
import com.framgia.photoeditor.util.TextureRenderer;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nhahv on 1/13/2017.
 * <></>
 */
public class EffectFragment extends Fragment
    implements GLSurfaceView.Renderer, EffectContract.View, EffectAdapter.EventEffect,
    FragmentView {
    public static final String SCALE = "scale";
    public static final String BLACK = "black";
    public static final String WHITE = "white";
    public static final String BRIGHTNESS = "brightness";
    public static final String CONTRAST = "contrast";
    public static final String FIRST_COLOR = "first_color";
    public static final String SECOND_COLOR = "second_color";
    public static final String STRENGTH = "strength";
    public static final String VERTICAL = "vertical";
    public static final String HORIZONTAL = "horizontal";
    public static final String ANGLE = "angle";
    public static final String TINT = "tint";
    private static final float PARAM_EFFECT_14 = 1.4f;
    private static final float PARAM_EFFECT_9 = .9f;
    private static final float PARAM_EFFECT_8 = .8f;
    @BindView(R.id.surface_view)
    GLSurfaceView mEffectView;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private Bitmap mBitmap;
    private Bitmap mBitmapResult;
    private EffectPresenter mPresenter;
    private EffectAdapter mAdapter;
    private int[] mTextures = new int[2];
    private Effect mEffect;
    private TextureRenderer mTexRenderer = new TextureRenderer();
    private int mImageWidth;
    private int mImageHeight;
    private boolean mInitialized;
    private Effects mCurrentEffects;
    private EffectContext mEffectContext;
    private HighlightFragment.EventBackToActivity mEventBackToActivity;

    public static EffectFragment newInstance() {
        return new EffectFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_effect, container, false);
        ButterKnife.bind(this, view);
        mPresenter = new EffectPresenter(this);
        mPresenter.getListEffect();
        return view;
    }

    public void setCurrentEffect(Effects effects) {
        mCurrentEffects = effects;
    }

    @Override
    public void setEventBackToActivity(HighlightFragment.EventBackToActivity event) {
        mEventBackToActivity = event;
    }

    private void loadTextures() {
        mEffectContext = EffectContext.createWithCurrentGlContext();
        GLES20.glGenTextures(2, mTextures, 0);
        mImageWidth = mBitmap.getWidth();
        mImageHeight = mBitmap.getHeight();
        mTexRenderer.updateTextureSize(mImageWidth, mImageHeight);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0);
        GLToolbox.initTexParams();
    }

    private void initEffect() {
        EffectFactory effectFactory = mEffectContext.getFactory();
        if (mEffect != null) {
            mEffect.release();
        }
        switch (mCurrentEffects) {
            case EFFECT_NODE:
                break;
            case EFFECT_CONTRAST:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_CONTRAST);
                mEffect.setParameter(CONTRAST, PARAM_EFFECT_14);
                break;
            case EFFECT_CROSSPROCESS:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_CROSSPROCESS);
                break;
            case EFFECT_DOCUMENTARY:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_DOCUMENTARY);
                break;
            case EFFECT_FILLLIGHT:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_FILLLIGHT);
                mEffect.setParameter(STRENGTH, PARAM_EFFECT_8);
                break;
            case EFFECT_GRAYSCALE:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_GRAYSCALE);
                break;
            case EFFECT_LOMOISH:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_LOMOISH);
                break;
            case EFFECT_SEPIA:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_SEPIA);
                break;
            case EFFECT_SHARPEN:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_SHARPEN);
                break;
            case EFFECT_TEMPERATURE:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_TEMPERATURE);
                mEffect.setParameter(SCALE, PARAM_EFFECT_9);
                break;
            default:
                break;
        }
    }

    private void applyEffect() {
        mEffect.apply(mTextures[0], mImageWidth, mImageHeight, mTextures[1]);
    }

    private void renderResult() {
        if (mCurrentEffects != Effects.EFFECT_NODE) {
            mTexRenderer.renderTexture(mTextures[1]);
        } else mTexRenderer.renderTexture(mTextures[0]);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (!mInitialized) {
            mTexRenderer.init();
            loadTextures();
            mInitialized = true;
        }
        if (mCurrentEffects != Effects.EFFECT_NODE) {
            initEffect();
            applyEffect();
        }
        renderResult();
        mBitmapResult = takeScreenshot(gl);
    }

    @Override
    public Bitmap takeScreenshot(GL10 gl10) {
        final int width = mEffectView.getWidth();
        final int height = mEffectView.getHeight();
        IntBuffer ib = IntBuffer.allocate(width * height);
        IntBuffer ibt = IntBuffer.allocate(width * height);
        gl10.glReadPixels(0, 0, width, height, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, ib);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                ibt.put((height - i - 1) * width + j, ib.get(i * width + j));
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(ibt);
        return bitmap;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (mTexRenderer != null) mTexRenderer.updateViewSize(width, height);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    }

    @Override
    public void start() {
        mBitmap = EditImageActivity.sBitmap;
        mEffectView.setEGLContextClientVersion(2);
        mEffectView.setRenderer(this);
        mEffectView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        mRecyclerView.setLayoutManager(
            new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(true);
        mAdapter = new EffectAdapter(getActivity(), this);
        mRecyclerView.setAdapter(mAdapter);
        mCurrentEffects = Effects.EFFECT_NODE;
    }

    @Override
    public void pickEffect(int position) {
        setCurrentEffect(Effects.values()[position]);
        mEffectView.requestRender();
    }

    @Override
    public void updateAdapter(List<String> nameEffect, List<Integer> imageEffect) {
        if (mAdapter != null) mAdapter.setEffect(nameEffect, imageEffect);
    }

    @Override
    public List<String> listNameEffect() {
        return Arrays.asList(getResources().getStringArray(R.array.effects));
    }

    @Override
    public List<Integer> listImageEffect() {
        Resources res = getResources();
        TypedArray resource = res.obtainTypedArray(R.array.image_effect);
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < resource.length(); i++) {
            list.add(resource.getResourceId(i, -1));
        }
        return list;
    }

    @Override
    public void saveBitmap() {
        EditImageActivity.sBitmap = mBitmapResult;
        if (mEventBackToActivity != null) mEventBackToActivity.backToActivity();
    }
}
