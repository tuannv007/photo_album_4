package com.framgia.photoeditor.data.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nhahv on 12/19/2016.
 * <.
 */
public class LocalImageFolder {
    private String mFolderName;
    private String mFolderPath;
    private List<File> mListImageOfFolder = new ArrayList<>();

    public LocalImageFolder(String folderName, String folderPath) {
        this.mFolderName = folderName;
        mFolderPath = folderPath;
    }

    public String getFolderPath() {
        return mFolderPath;
    }

    public String getFolderName() {
        return mFolderName;
    }

    public List<File> getListImageOfFolder() {
        return mListImageOfFolder;
    }

    public void setListImageOfFolder(List<File> images) {
        this.mListImageOfFolder = images;
    }
}
