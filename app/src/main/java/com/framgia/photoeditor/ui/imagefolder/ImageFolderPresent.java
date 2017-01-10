package com.framgia.photoeditor.ui.imagefolder;

import android.content.Context;
import android.os.AsyncTask;

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
    private LocalImageFolder mFolder;
    private Context mContext;

    public ImageFolderPresent(ImageFolderContract.View view) {
        mView = view;
        mView.start();
    }

    @Override
    public void getListImageFolder(Context context) {
        mContext = context;
        new loadImageAsyncTask().execute();
    }

    private class loadImageAsyncTask extends AsyncTask<Void, Void, List<LocalImage>> {
        @Override
        protected List<LocalImage> doInBackground(Void... param) {
            return LocalImageLoader.getListImage(mContext);
        }

        @Override
        protected void onPostExecute(List<LocalImage> localImages) {
            super.onPostExecute(localImages);
            mFolder = new LocalImageFolder(DATA_ALL_IMAGE);
            for (LocalImage image : localImages) {
                mFolder.getListImageOfFolder().add(image.getPathImage());
            }
            mListImageFolder.clear();
            mListImageFolder.add(mFolder);
            mListImageFolder
                .addAll(LocalImageLoader.getListImageFolder((ImageFolderActivity) mView));
            mView.updateListImageFolder(mListImageFolder);
        }
    }
}
