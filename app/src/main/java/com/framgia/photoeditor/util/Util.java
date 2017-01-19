package com.framgia.photoeditor.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by tuanbg on 1/6/17.
 * <></>
 */
public class Util {
    public static Bitmap convertImageToBlackWhite(Bitmap src) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(colorMatrix);
        Bitmap blackAndWhiteBitmap = src.copy(Bitmap.Config.ARGB_8888, true);
        Paint paint = new Paint();
        paint.setColorFilter(colorMatrixFilter);
        Canvas canvas = new Canvas(blackAndWhiteBitmap);
        canvas.drawBitmap(blackAndWhiteBitmap, 0, 0, paint);
        return blackAndWhiteBitmap;
    }

    public static boolean saveImage(Bitmap bitmap) {
        String nameFile = getTime() + Constant.DATA_JPEG;
        File file = new File(getFilePathImage(), nameFile);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Bitmap getBitmapFromCustomView(View view) {
        Bitmap returnedBitmap =
            Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

    private static File getFilePathImage() {
        String root = Environment.getExternalStorageDirectory().toString();
        File dirImageSaved = new File(root + Constant.FOLDER_NAME);
        if (!dirImageSaved.exists()) dirImageSaved.mkdirs();
        return dirImageSaved;
    }

    public static String saveImageUri(Bitmap bitmap) {
        String nameFile = getTime() + Constant.DATA_JPEG;
        File file = new File(getFilePathImage(), nameFile);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            return getFilePathImage() + "/" + nameFile;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Bitmap createContrast(Bitmap src, double value) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        int A, R, G, B;
        int pixel;
        double contrast = Math.pow((Constant.MAX_VALUE + value) / Constant.MAX_VALUE, 2);
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                R = (int) (((((R / Constant.MAX_PIXEL_COLOR) - 0.5) * contrast) + 0.5) *
                    Constant.MAX_PIXEL_COLOR);
                if (R < Constant.MIN_PIXEL_COLOR) R = Constant.MAX_PIXEL_INTERGER;
                else if (R > Constant.MAX_PIXEL_COLOR) R = Constant.MAX_PIXEL_INTERGER;
                G = Color.red(pixel);
                G = (int) (((((G / Constant.MAX_PIXEL_COLOR) - 0.5) * contrast) + 0.5) *
                    Constant.MAX_PIXEL_COLOR);
                if (G < Constant.MIN_PIXEL_COLOR) G = Constant.MAX_PIXEL_INTERGER;
                else if (G > Constant.MAX_PIXEL_COLOR) G = Constant.MAX_PIXEL_INTERGER;
                B = Color.red(pixel);
                B = (int) (((((B / Constant.MAX_PIXEL_COLOR) - 0.5) * contrast) + 0.5) *
                    Constant.MAX_PIXEL_COLOR);
                if (B < Constant.MIN_PIXEL_COLOR) B = Constant.MAX_PIXEL_INTERGER;
                else if (B > Constant.MAX_PIXEL_COLOR) B = Constant.MAX_PIXEL_INTERGER;
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        return bmOut;
    }

    public static Bitmap changeContrast(Bitmap bmp, float contrast, float brightness) {
        ColorMatrix cm = new ColorMatrix(new float[]{
            contrast, 0, 0, 0, brightness,
            0, contrast, 0, 0, brightness,
            0, 0, contrast, 0, brightness,
            0, 0, 0, 1, 0
        });
        Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        Canvas canvas = new Canvas(ret);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bmp, 0, 0, paint);
        return ret;
    }

    public static Bitmap convertPathToBitmap(String file) {
        return BitmapFactory.decodeFile(file);
    }

    public static String getTime() {
        Calendar cal = Calendar.getInstance();
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HHmmss", Locale.getDefault());
        return date.format(currentLocalTime);
    }

    public static void showToast(Context context, int id) {
        if (context != null) Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
    }
}


