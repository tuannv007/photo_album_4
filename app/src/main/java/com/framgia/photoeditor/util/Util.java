package com.framgia.photoeditor.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
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
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        int A, R, G, B;
        int pixel;
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                R = (R + G + B) / 3;
                G = R;
                B = R;
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        return bmOut;
    }

    public static boolean saveImage(Bitmap finalBitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + Constant.FOLDER_NAME);
        myDir.mkdirs();
        String fname = getTime() + Constant.DATA_JPEG;
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Bitmap updateHUE(Bitmap src, float settingHue, float settingSat,
                                   float settingVal) {
        int w = src.getWidth();
        int h = src.getHeight();
        int[] mapSrcColor = new int[w * h];
        int[] mapDestColor = new int[w * h];
        float[] pixelHSV = new float[3];
        src.getPixels(mapSrcColor, 0, w, 0, 0, w, h);
        int index = 0;
        for (int y = 0; y < h; ++y) {
            for (int x = 0; x < w; ++x) {
                Color.colorToHSV(mapSrcColor[index], pixelHSV);
                pixelHSV[0] = pixelHSV[0] + settingHue;
                if (pixelHSV[0] < Constant.MIN_PIXEL_COLOR_HUE) {
                    pixelHSV[0] = Constant.MIN_PIXEL_COLOR_HUE;
                } else if (pixelHSV[0] > Constant.MAX_PIXEL_COLOR_HUE) {
                    pixelHSV[0] = Constant.MAX_PIXEL_COLOR_HUE;
                }
                pixelHSV[1] = pixelHSV[1] + settingSat;
                if (pixelHSV[1] < Constant.MIN_PIXEL_COLOR_HUE) {
                    pixelHSV[1] = Constant.MIN_PIXEL_COLOR_HUE;
                } else if (pixelHSV[1] > 1.0f) pixelHSV[1] = 1.0f;
                pixelHSV[2] = pixelHSV[2] + settingVal;
                if (pixelHSV[2] < Constant.MIN_PIXEL_COLOR_HUE) {
                    pixelHSV[2] = Constant.MIN_PIXEL_COLOR_HUE;
                } else if (pixelHSV[2] > 1.0f) pixelHSV[2] = 1.0f;
                mapDestColor[index] = Color.HSVToColor(pixelHSV);
                index++;
            }
        }
        return Bitmap.createBitmap(mapDestColor, w, h, Bitmap.Config.ARGB_8888);
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

    public static Bitmap decodeFromByte(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static byte[] convertBitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}


