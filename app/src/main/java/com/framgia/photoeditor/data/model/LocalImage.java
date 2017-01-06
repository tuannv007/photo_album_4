package com.framgia.photoeditor.data.model;

/**
 * Created by dee on 2015/8/5.
 * <></>
 */
public class LocalImage {
    private String mFileName;
    private String mFolderName;
    private String mFolderPath;
    private String mPathImage;

    public LocalImage(String name, String folderName, String folderPath, String path) {
        mFileName = name;
        mFolderName = folderName;
        mPathImage = path;
        mFolderPath = folderPath;
    }

    public String getName() {
        return mFileName;
    }

    public void setName(String name) {
        this.mFileName = name;
    }

    public String getFolderName() {
        return mFolderName;
    }

    public void setFolderName(String folderName) {
        this.mFolderName = folderName;
    }

    public String getFolderPath() {
        return mFolderPath;
    }

    public String getPathImage() {
        return mPathImage;
    }

    public void setPathImage(String path) {
        this.mPathImage = path;
    }
}
