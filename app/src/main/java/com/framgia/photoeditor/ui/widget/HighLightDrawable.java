package com.framgia.photoeditor.ui.widget;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;

import static com.framgia.photoeditor.util.Constant.Size.DATA_RADIUS;

public class HighLightDrawable extends BitmapDrawable {
    private static final int COLOR_PAINT = 0x99000000;
    private Rect mMaskArea;
    private Point mFingerPoint;
    private float mRadius = DATA_RADIUS;
    private int mWidth, mHeight;

    public HighLightDrawable(Resources resources, Bitmap bitmap, Point centerPoint) {
        super(resources, bitmap);
        mMaskArea = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        mFingerPoint = centerPoint;
        mWidth = bitmap.getWidth();
        mHeight = bitmap.getHeight();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        RadialGradient radialGradient =
            new RadialGradient(mFingerPoint.x, mFingerPoint.y, mRadius, 0x00000000, 0x99000000,
                Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setDither(true);
        paint.setShader(radialGradient);
        canvas.drawCircle(mFingerPoint.x, mFingerPoint.y, mRadius, paint);
        Path mHighLightArea = new Path();
        mHighLightArea.addCircle(mFingerPoint.x, mFingerPoint.y, mRadius, Path.Direction.CCW);
        canvas.clipPath(mHighLightArea, Region.Op.DIFFERENCE);
        Paint maskPaint = new Paint();
        maskPaint.setColor(COLOR_PAINT);
        canvas.drawRect(mMaskArea, maskPaint);
    }

    public void setFinger(Point point) {
        mFingerPoint = point;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }
}
