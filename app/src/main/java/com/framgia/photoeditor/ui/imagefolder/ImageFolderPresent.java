package com.framgia.photoeditor.ui.imagefolder;

import android.content.Context;

import com.framgia.photoeditor.data.model.LocalImage;
import com.framgia.photoeditor.data.model.LocalImageFolder;
import com.framgia.photoeditor.util.LocalImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nhahv on 1/9/2017.
 * <></>
 */
public class ImageFolderPresent implements ImageFolderContract.Presenter {
    private final String DATA_ALL_IMAGE = "All image";
    private ImageFolderContract.View mView;
    private List<LocalImageFolder> mListImageFolder = new ArrayList<>();

    public ImageFolderPresent(ImageFolderContract.View view) {
        mView = view;
        mView.start();
    }

    @Override
    public void getListImageFolder(Context context) {
        List<LocalImage> listImage = LocalImageLoader.getListImage(context);
        LocalImageFolder folder = new LocalImageFolder(DATA_ALL_IMAGE);
        for (LocalImage image : listImage) {
            folder.getListImageOfFolder().add(image.getPathImage());
        }
        mListImageFolder.clear();
        mListImageFolder.add(folder);
        mListImageFolder.addAll(LocalImageLoader.getListImageFolder((ImageFolderActivity) mView));
        mView.updateListImageFolder(mListImageFolder);
    }
}
