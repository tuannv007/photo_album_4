package com.framgia.photoeditor.ui.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Scroller;

public class ZoomImageView extends ImageView implements ScaleGestureDetector.OnScaleGestureListener,
    View.OnTouchListener, ViewTreeObserver.OnGlobalLayoutListener {
    private static final long TIME_DELAY_IMAGE = 16;
    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector mGestureDetector;
    private Matrix mScaleMatrix;
    private boolean mFirst;
    private float mInitScale;
    private float mMaxScale;
    private float mMidScale;
    private float mMinScale;
    private float mMaxOverScale;
    private boolean mIsAutoScale;
    private int mLastPointerCount;
    private boolean mIsCanDrag;
    private float mLastX;
    private float mLastY;
    private int mTouchSlop;
    private boolean mIsCheckLeftAndRight;
    private boolean mIsCheckTopAndBottom;
    private VelocityTracker mVelocityTracker;
    private FlingRunnable mFlingRunnable;

    public ZoomImageView(Context context) {
        this(context, null, 0);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setScaleType(ScaleType.MATRIX);
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        mScaleMatrix = new Matrix();
        setOnTouchListener(this);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mGestureDetector =
            new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    if (mIsAutoScale) return true;
                    float x = e.getX();
                    float y = e.getY();
                    if (getScale() < mMidScale) post(new AutoScaleRunnable(mMidScale, x, y));
                    else post(new AutoScaleRunnable(mInitScale, x, y));
                    return true;
                }
            });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        if (!mFirst) {
            mFirst = true;
            int width = getWidth();
            int height = getHeight();
            Drawable d = getDrawable();
            if (d == null) return;
            int dw = d.getIntrinsicWidth();
            int dh = d.getIntrinsicHeight();
            float scale = 1.0f;
            if (dw > width && dh < height) scale = width * 1.0f / dw;
            if (dw < width && dh > height) scale = height * 1.0f / dh;
            if ((dw < width && dh < height) || (dw > width && dh > height)) {
                scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
            }
            int dx = width / 2 - dw / 2;
            int dy = height / 2 - dh / 2;
            mScaleMatrix.postTranslate(dx, dy);
            mScaleMatrix.postScale(scale, scale, width / 2, height / 2);
            setImageMatrix(mScaleMatrix);
            mInitScale = scale;
            mMaxScale = mInitScale * 4;
            mMidScale = mInitScale * 2;
            mMinScale = mInitScale / 4;
            mMaxOverScale = mMaxScale * 5;
        }
    }

    private float getScale() {
        float[] values = new float[9];
        mScaleMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    private RectF getMatrixRectF() {
        Matrix matrix = mScaleMatrix;
        RectF rectF = new RectF();
        Drawable d = getDrawable();
        if (d != null) {
            rectF.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rectF);
        }
        return rectF;
    }

    private void checkBorderAndCenterWhenScale() {
        if (getDrawable() == null) return;
        float deltaX = 0.0f;
        float deltaY = 0.0f;
        int width = getWidth();
        int height = getHeight();
        RectF rectF = getMatrixRectF();
        if (rectF.width() >= width) {
            if (rectF.left > 0) deltaX = -rectF.left;
            if (rectF.right < width) deltaX = width - rectF.right;
        }
        if (rectF.height() >= height) {
            if (rectF.top > 0) deltaY = -rectF.top;
            if (rectF.bottom < height) deltaY = height - rectF.bottom;
        }
        if (rectF.width() < width) deltaX = width / 2f - rectF.right + rectF.width() / 2f;
        if (rectF.height() < height) deltaY = height / 2f - rectF.bottom + rectF.height() / 2f;
        mScaleMatrix.postTranslate(deltaX, deltaY);
    }

    private void checkBorderWhenTranslate() {
        RectF rectF = getMatrixRectF();
        float deltaX = 0.0f;
        float deltaY = 0.0f;
        int width = getWidth();
        int height = getHeight();
        if (mIsCheckLeftAndRight) {
            if (rectF.left > 0) deltaX = -rectF.left;
            if (rectF.right < width) deltaX = width - rectF.right;
        }
        if (mIsCheckTopAndBottom) {
            if (rectF.top > 0) deltaY = -rectF.top;
            if (rectF.bottom < height) deltaY = height - rectF.bottom;
        }
        mScaleMatrix.postTranslate(deltaX, deltaY);
    }

    private class AutoScaleRunnable implements Runnable {
        private float mTargetScale;
        private float tempScale;
        private float x;
        private float y;
        private final float BIGGER = 1.07f;
        private final float SMALLER = 0.93f;

        public AutoScaleRunnable(float targetScale, float x, float y) {
            this.mTargetScale = targetScale;
            this.x = x;
            this.y = y;
            if (getScale() < mTargetScale) tempScale = BIGGER;
            if (getScale() > mTargetScale) tempScale = SMALLER;
        }

        @Override
        public void run() {
            mScaleMatrix.postScale(tempScale, tempScale, x, y);
            checkBorderAndCenterWhenScale();
            setImageMatrix(mScaleMatrix);
            float currentScale = getScale();
            if ((tempScale > 1.0f) && currentScale < mTargetScale
                || (tempScale < 1.0f) && currentScale > mTargetScale) {
                postDelayed(this, TIME_DELAY_IMAGE);
            } else {
                float scale = mTargetScale / currentScale;
                mScaleMatrix.postScale(scale, scale, x, y);
                checkBorderAndCenterWhenScale();
                setImageMatrix(mScaleMatrix);
                mIsAutoScale = false;
            }
        }
    }

    private class FlingRunnable implements Runnable {
        private Scroller mScroller;
        private int mCurrentX, mCurrentY;

        public FlingRunnable(Context context) {
            mScroller = new Scroller(context);
        }

        public void cancelFling() {
            mScroller.forceFinished(true);
        }

        public void fling(int viewWidth, int viewHeight, int velocityX,
                          int velocityY) {
            RectF rectF = getMatrixRectF();
            if (rectF == null) return;
            final int startX = Math.round(-rectF.left);
            final int minX, maxX, minY, maxY;
            if (rectF.width() > viewWidth) {
                minX = 0;
                maxX = Math.round(rectF.width() - viewWidth);
            } else minX = maxX = startX;
            final int startY = Math.round(-rectF.top);
            if (rectF.height() > viewHeight) {
                minY = 0;
                maxY = Math.round(rectF.height() - viewHeight);
            } else minY = maxY = startY;
            mCurrentX = startX;
            mCurrentY = startY;
            if (startX != maxX || startY != maxY) {
                mScroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY);
            }
        }

        @Override
        public void run() {
            if (mScroller.isFinished()) return;
            if (mScroller.computeScrollOffset()) {
                final int newX = mScroller.getCurrX();
                final int newY = mScroller.getCurrY();
                mScaleMatrix.postTranslate(mCurrentX - newX, mCurrentY - newY);
                checkBorderWhenTranslate();
                setImageMatrix(mScaleMatrix);
                mCurrentX = newX;
                mCurrentY = newY;
                postDelayed(this, TIME_DELAY_IMAGE);
            }
        }
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scaleFactor = detector.getScaleFactor();
        float scale = getScale();
        if (getDrawable() == null) return true;
        if ((scaleFactor > 1.0f && scale * scaleFactor < mMaxOverScale)
            || scaleFactor < 1.0f && scale * scaleFactor > mMinScale) {
            if (scale * scaleFactor > mMaxOverScale + 0.01f) scaleFactor = mMaxOverScale / scale;
            if (scale * scaleFactor < mMinScale + 0.01f) scaleFactor = mMinScale / scale;
            mScaleMatrix
                .postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            checkBorderAndCenterWhenScale();
            setImageMatrix(mScaleMatrix);
        }
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)) return true;
        mScaleGestureDetector.onTouchEvent(event);
        float x = 0.0f;
        float y = 0.0f;
        int pointerCount = event.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }
        x /= pointerCount;
        y /= pointerCount;
        if (mLastPointerCount != pointerCount) {
            mIsCanDrag = false;
            mLastX = x;
            mLastY = y;
        }
        mLastPointerCount = pointerCount;
        RectF rectF = getMatrixRectF();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mVelocityTracker = VelocityTracker.obtain();
                if (mVelocityTracker != null) mVelocityTracker.addMovement(event);
                if (mFlingRunnable != null) {
                    mFlingRunnable.cancelFling();
                    mFlingRunnable = null;
                }
                mIsCanDrag = false;
                if (rectF.width() > getWidth() + 0.01f || rectF.height() > getHeight() + 0.01f) {
                    if (getParent() instanceof ViewPager) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (rectF.width() > getWidth() + 0.01f || rectF.height() > getHeight() + 0.01f) {
                    if (getParent() instanceof ViewPager) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                float dx = x - mLastX;
                float dy = y - mLastY;
                if (!mIsCanDrag) mIsCanDrag = isMoveAction(dx, dy);
                if (mIsCanDrag) {
                    if (getDrawable() != null) {
                        if (mVelocityTracker != null) mVelocityTracker.addMovement(event);
                        mIsCheckLeftAndRight = true;
                        mIsCheckTopAndBottom = true;
                        if (rectF.width() < getWidth()) {
                            dx = 0;
                            mIsCheckLeftAndRight = false;
                        }
                        if (rectF.height() < getHeight()) {
                            dy = 0;
                            mIsCheckTopAndBottom = false;
                        }
                    }
                    mScaleMatrix.postTranslate(dx, dy);
                    checkBorderWhenTranslate();
                    setImageMatrix(mScaleMatrix);
                }
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                mLastPointerCount = 0;
                if (getScale() < mInitScale) {
                    post(new AutoScaleRunnable(mInitScale, getWidth() / 2, getHeight() / 2));
                }
                if (getScale() > mMaxScale) {
                    post(new AutoScaleRunnable(mMaxScale, getWidth() / 2, getHeight() / 2));
                }
                if (mIsCanDrag) {
                    if (mVelocityTracker != null) {
                        mVelocityTracker.addMovement(event);
                        mVelocityTracker.computeCurrentVelocity(1000);
                        final float vX = mVelocityTracker.getXVelocity();
                        final float vY = mVelocityTracker.getYVelocity();
                        mFlingRunnable = new FlingRunnable(getContext());
                        mFlingRunnable.fling(getWidth(), getHeight(), (int) -vX, (int) -vY);
                        post(mFlingRunnable);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
            default:
                break;
        }
        return true;
    }

    private boolean isMoveAction(float dx, float dy) {
        return Math.sqrt(dx * dx + dy * dy) > mTouchSlop;
    }
}
