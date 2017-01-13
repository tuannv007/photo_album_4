package com.framgia.photoeditor.data.model;

import java.io.Serializable;

/**
 * Created by tuanbg on 1/12/17.
 */
public class Image implements Serializable {
    private String mPath;
    private String mName;

    public Image(String path, String name) {
        this.mPath = path;
        this.mName = name;
    }

    public String getPath() {
        return mPath;
    }

    public String getName() {
        return mName;
    }
}
