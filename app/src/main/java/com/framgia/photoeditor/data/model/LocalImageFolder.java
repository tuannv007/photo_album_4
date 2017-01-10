package com.framgia.photoeditor.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nhahv on 12/19/2016.
 * <.
 */
public class LocalImageFolder implements Parcelable {
    private String mFolderName;
    private String mFolderPath;
    private List<String> mListImageOfFolder = new ArrayList<>();

    public LocalImageFolder() {
    }

    public LocalImageFolder(String folderName, String folderPath) {
        mFolderName = folderName;
        mFolderPath = folderPath;
    }

    public LocalImageFolder(String folderName) {
        mFolderName = folderName;
    }

    protected LocalImageFolder(Parcel in) {
        mFolderName = in.readString();
        mFolderPath = in.readString();
        mListImageOfFolder = in.readArrayList(null);
    }

    public static final Creator<LocalImageFolder> CREATOR = new Creator<LocalImageFolder>() {
        @Override
        public LocalImageFolder createFromParcel(Parcel in) {
            return new LocalImageFolder(in);
        }

        @Override
        public LocalImageFolder[] newArray(int size) {
            return new LocalImageFolder[size];
        }
    };

    public String getFolderPath() {
        return mFolderPath;
    }

    public String getFolderName() {
        return mFolderName;
    }

    public List<String> getListImageOfFolder() {
        return mListImageOfFolder;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mFolderName);
        parcel.writeString(mFolderPath);
        parcel.writeList(mListImageOfFolder);
    }
}
