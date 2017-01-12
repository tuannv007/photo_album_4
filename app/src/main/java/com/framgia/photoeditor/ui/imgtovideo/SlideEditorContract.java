package com.framgia.photoeditor.ui.imgtovideo;

import android.net.Uri;
import android.widget.VideoView;

import com.framgia.photoeditor.data.model.Image;
import com.framgia.photoeditor.ui.base.BasePresenter;
import com.framgia.photoeditor.ui.base.BaseView;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by tuanbg on 1/12/17.
 */
public interface SlideEditorContract {
    interface View extends BaseView {
        void initProgress();
        void initMediaPlayer();
        void onExcuteFinish(String output);
        void getName();
        void removeResizeImage();
        void showDialog();
        void dismisDialog();
        void initFacebook();
        Uri getUriVideo();
    }

    interface Presenter extends BasePresenter {
        String[] scaleImage(Image image, int width, int height);
        String[] filterComplexQuery(List<Image> images, int width, int height);
        void deleteTempFile(String path);
        String getNameFile();
        void deleteFileDowmload(String path);
        String[] mergeAudio(String musicUri, String output);
        void convertVideo(List<Image> images);
        void executeFFMPEG(String[] query, final boolean isShowDialog, final String output);
        void copy(File src, File dst) throws IOException;
        void shareVideo();
    }
}
