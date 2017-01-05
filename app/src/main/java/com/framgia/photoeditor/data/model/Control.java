package com.framgia.photoeditor.data.model;

/**
 * Created by tuanbg on 1/5/17.
 */
public class Control {
    private int mImage;
    private String mTitle;

    public Control(int image, String title) {
        mImage = image;
        mTitle = title;
    }

    public int getImage() {
        return mImage;
    }

    public String getTitle() {
        return mTitle;
    }
}
