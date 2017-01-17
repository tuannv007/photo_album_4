package com.framgia.photoeditor.ui.imagefolder;

import android.content.Context;

import com.framgia.photoeditor.data.model.LocalImageFolder;
import com.framgia.photoeditor.ui.base.BaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nhahv on 1/9/2017.
 * <></>
 */
public interface ImageFolderContract {
    interface View extends BaseView {
        void pickSingleImage(String string);
        void pickMultipleImage(ArrayList<String> stringArrayList);
        void updateListImageFolder(List<LocalImageFolder> imageFolders);
        boolean checkCameraHardware(Context context);
    }

    interface Presenter {
        void getListImageFolder(Context context);
    }
}
