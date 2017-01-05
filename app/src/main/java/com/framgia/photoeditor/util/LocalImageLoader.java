package com.framgia.photoeditor.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.framgia.photoeditor.data.model.LocalImage;
import com.framgia.photoeditor.data.model.LocalImageFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dee on 15/11/19.
 * <></>
 */
public class LocalImageLoader {
    private static final String[] IMAGE_PROJECTION = {
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.DATE_ADDED,
        MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME
    };
    private static final String SELECTION =
        MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?";
    private static final String[] SELECTION_ARGS =
        new String[]{Constant.DATA_IMAGE_JPEG, Constant.DATA_IMAGE_PNG};

    public static List<LocalImage> getListImageExternal(Context context) {
        List<LocalImage> listLocalImage = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            IMAGE_PROJECTION,
            SELECTION,
            SELECTION_ARGS,
            IMAGE_PROJECTION[2] + Constant.DATA_DESC, null);
        if (cursor == null) return listLocalImage;
        if (cursor.moveToFirst()) {
            int indexPath = cursor.getColumnIndex(IMAGE_PROJECTION[0]);
            int indexName = cursor.getColumnIndex(IMAGE_PROJECTION[1]);
            int indexFolder = cursor.getColumnIndex(IMAGE_PROJECTION[3]);
            String pathImage, fileName, folderName, folderPath;
            while (!cursor.isAfterLast()) {
                pathImage = cursor.getString(indexPath);
                fileName = cursor.getString(indexName);
                folderName = cursor.getString(indexFolder);
                folderPath = new File(pathImage).getParent();
                listLocalImage.add(new LocalImage(fileName, folderName, folderPath, pathImage));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return listLocalImage;
    }

    public static List<LocalImageFolder> getListImageFolder(Context context) {
        List<LocalImageFolder> listImageFolder = new ArrayList<>();
        List<LocalImage> listImage = getListImageExternal(context);
        if (listImage.size() == 0) return listImageFolder;
        LocalImageFolder folder;
        for (LocalImage item : listImage) {
            if (!existsFolder(item.getFolderName(), listImageFolder)) {
                folder = new LocalImageFolder(item.getFolderName(), item.getFolderPath());
                folder.getListImageOfFolder()
                    .addAll(getListFiles(new File(folder.getFolderPath())));
            }
        }
        return listImageFolder;
    }

    private static List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) inFiles.addAll(getListFiles(file));
            else if (file.getPath().toLowerCase().endsWith(Constant.DATA_JPG) ||
                file.getPath().toLowerCase().endsWith(Constant.DATA_PNG) ||
                file.getPath().toLowerCase().endsWith(Constant.DATA_JPEG)) {
                inFiles.add(file);
            }
        }
        return inFiles;
    }

    private static boolean existsFolder(String nameFolder, List<LocalImageFolder> folders) {
        for (LocalImageFolder item : folders) {
            if (TextUtils.equals(item.getFolderName().toLowerCase(), nameFolder.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
