package com.framgia.photoeditor.ui.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.HashMap;

import static com.framgia.photoeditor.util.Constant.Font.FONT_BSC;

/**
 * Created by Nhahv on 1/10/2017.
 * <></>
 */
public class BscTextView extends TextView {
    private static HashMap<String, Typeface> sFontCache = new HashMap<>();

    public BscTextView(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public BscTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public BscTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = BscTextView.getTypeface(FONT_BSC, context);
        setTypeface(customFont);
    }

    public static Typeface getTypeface(String fontName, Context context) {
        Typeface typeface = sFontCache.get(fontName);
        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), fontName);
            } catch (Exception e) {
                return null;
            }
            sFontCache.put(fontName, typeface);
        }
        return typeface;
    }
}
