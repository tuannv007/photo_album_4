package com.framgia.photoeditor.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;

import java.io.IOException;

/**
 * Created by Nhahv on 1/10/2017.
 * <></>
 */
public class UtilImage {
    private static final int ANGLE_0 = 0;
    private static final int ANGLE_90 = 90;
    private static final int ANGLE_180 = 100;
    private static final int ANGLE_270 = 270;
    private static final int HALF_PROGRESS = 50;
    private static final int MAX_LIGHT_250 = 255;

    public static Bitmap decodeBitmap(String path, int reqWidth, int reqHeight) throws IOException {
        int angle;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false;
        ExifInterface exifInterface = new ExifInterface(path);
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                angle = ANGLE_90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                angle = ANGLE_180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                angle = ANGLE_270;
                break;
            default:
                angle = ANGLE_0;
                break;
        }
        int outHeight = options.outHeight;
        int outWith = options.outWidth;
        if (angle == ANGLE_0 || angle == ANGLE_180) {
            outWith = options.outHeight;
            outHeight = options.outWidth;
        }
        options.inSampleSize =
            optionSize(outHeight, outWith, reqWidth, reqHeight);
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        bitmap = rotateImage(bitmap, angle);
        return bitmap;
    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap
            .createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private static int optionSize(int width, int height, int reqWidth, int reqHeight) {
        int inSampleSize = 1;
        if (reqHeight < height || reqWidth < width) {
            final int haftHeight = height / 2;
            final int haftWidth = width / 2;
            while ((haftHeight / inSampleSize) > reqHeight &&
                (haftWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap centerBitmap(Bitmap bmp, float width, float height) {
        int bitmapWidth = bmp.getWidth();
        int bitmapHeight = bmp.getHeight();
        float ratioWidth = bitmapWidth / width;
        float ratioHeight = bitmapHeight / height;
        if (ratioWidth > ratioHeight) {
            bitmapWidth = (int) width;
            bitmapHeight = (int) (bitmapHeight / ratioWidth);
        } else {
            bitmapHeight = (int) height;
            bitmapWidth = (int) (bitmapWidth / ratioHeight);
        }
        return Bitmap.createScaledBitmap(bmp, bitmapWidth, bitmapHeight, true);
    }

    public static Bitmap brightness(Bitmap bitmap, float value, float indexSeekbar) {
        /*contrast : 0 to 10 brightness : -255 to 255*/
        float contrast;
        if (value < HALF_PROGRESS) contrast = value / HALF_PROGRESS;
        else contrast = 1 + (value - HALF_PROGRESS) / 50f * 9;
        float brightness = (indexSeekbar - HALF_PROGRESS) * MAX_LIGHT_250 / HALF_PROGRESS;
        ColorMatrix cm = new ColorMatrix(new float[]
            {
                contrast, 0, 0, 0, brightness,
                0, contrast, 0, 0, brightness,
                0, 0, contrast, 0, brightness,
                0, 0, 0, 1, 0
            });
        Bitmap bitmapResult =
            Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap
                .getConfig());
        Canvas canvas = new Canvas(bitmapResult);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return bitmapResult;
    }
}
