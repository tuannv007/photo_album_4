package com.framgia.photoeditor.ui.imgtovideo;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.facebook.share.ShareApi;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.framgia.photoeditor.data.model.Image;
import com.framgia.photoeditor.util.Util;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import static com.framgia.photoeditor.util.Constant.HEIGHT_VIDEO;
import static com.framgia.photoeditor.util.Constant.RESIZE_IMAGE;
import static com.framgia.photoeditor.util.Constant.TYPE_MP4;
import static com.framgia.photoeditor.util.Constant.WIDTH_VIDEO;

/**
 * Created by tuanbg on 1/12/17.
 */
public class SlideEditorPresenter implements SlideEditorContract.Presenter {
    private static final String OUTPUT_MUSIC = "OUTPUT_MUSIC";
    private SlideEditorContract.View mView;
    private FFmpeg mFfmpeg;
    private Context mContext;

    public SlideEditorPresenter(Context context, SlideEditorContract.View view) {
        mContext = context;
        mView = view;
        mFfmpeg = FFmpeg.getInstance(mContext);
    }

    @Override
    public boolean saveImage(Bitmap bitmap) {
        return false;
        //// TODO: 1/13/17
    }

    @Override
    public void handleSave(Bitmap bitmap) {
        // TODO: 1/13/17 check status save image
    }

    @Override
    public String[] scaleImage(Image image, int width, int height) {
        List<String> listQuery = new ArrayList<>();
        String newFile = new File(image.getPath()).getParent() + RESIZE_IMAGE + image.getName();
        deleteTempFile(newFile);
        listQuery.add("-i");
        listQuery.add(image.getPath());
        listQuery.add("-vf");
        listQuery.add("scale=" + width + ":" + height + ",setsar=1:1");
        listQuery.add(newFile);
        return listQuery.toArray(new String[listQuery.size()]);
    }

    @Override
    public String[] filterComplexQuery(List<Image> images, int width, int height) {
        List<String> listQuery = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            listQuery.add("-loop");
            listQuery.add("1");
            listQuery.add("-t");
            listQuery.add("2");
            listQuery.add("-i");
            listQuery
                .add(new File(images.get(i).getPath()).getParent() + RESIZE_IMAGE + images.get(i)
                    .getName());
        }
        listQuery.add("-filter_complex");
        StringBuilder tmp = new StringBuilder();
        for (int i = 0; i < images.size() - 1; i++) {
            tmp.append("[")
                .append(i + 1)
                .append(":v][")
                .append(i)
                .append(
                    ":v]blend=all_expr='A*(if(gte(T,0.5),1,T/0.5))+B*(1-(if(gte(T,0.5),1,T/0.5)))'[b")
                .append(i + 1)
                .append("v];");
        }
        tmp.append("[0:v]");
        for (int i = 1; i < images.size(); i++) {
            tmp.append("[b")
                .append(i)
                .append("v]")
                .append("[")
                .append(i)
                .append(":v]");
        }
        tmp.append("concat=n=").append(images.size() * 2 - 1).append(":v=1:a=0,format=yuv420p[v]");
        listQuery.add(String.valueOf(tmp));
        listQuery.add("-map");
        listQuery.add("[v]");
        listQuery.add("-c:v");
        listQuery.add("libx264");
        listQuery.add("-preset");
        listQuery.add("ultrafast");
        listQuery.add("-crf");
        listQuery.add("32");
        listQuery.add("-profile:v");
        listQuery.add("baseline");
        listQuery.add("-movflags");
        listQuery.add("faststart");
        listQuery.add("-r");
        listQuery.add("10");
        listQuery
            .add(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +
                "/" + getNameFile() + TYPE_MP4);
        return listQuery.toArray(new String[listQuery.size()]);
    }

    @Override
    public void deleteTempFile(String path) {
        File fileOut = new File(path);
        if (fileOut.exists()) fileOut.delete();
    }

    @Override
    public String getNameFile() {
        return Util.getTime();
    }

    @Override
    public void deleteFileDowmload(String path) {
        File fileOut = new File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +
                "/" + path);
        if (fileOut.exists()) fileOut.delete();
    }

    @Override
    public String[] mergeAudio(String musicUri, String outputFile) {
        List<String> listQuery = new ArrayList<>();
        String path =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +
                "/" + getNameFile() + TYPE_MP4;
        listQuery.add("-i");
        listQuery.add(musicUri);
        listQuery.add("-i");
        listQuery.add(path);
        listQuery.add("-map");
        listQuery.add("0:a");
        listQuery.add("-map");
        listQuery.add("1:v");
        listQuery.add("-strict");
        listQuery.add("-2");
        listQuery.add("-shortest");
        listQuery.add("-profile:v");
        listQuery.add("baseline");
        listQuery.add("-movflags");
        listQuery.add("faststart");
        listQuery
            .add(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +
                "/" + outputFile);
        return listQuery.toArray(new String[listQuery.size()]);
    }

    @Override
    public void convertVideo(final List<Image> images) {
        mView.getName();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < images.size(); i++) {
                    executeFFMPEG(scaleImage(images.get(i), WIDTH_VIDEO, HEIGHT_VIDEO), false, "");
                }
                executeFFMPEG(filterComplexQuery(images, WIDTH_VIDEO, HEIGHT_VIDEO),
                    true,
                    getNameFile() + TYPE_MP4);
            }
        }).start();
    }

    @Override
    public void executeFFMPEG(String[] query, final boolean isShowDialog, final String output) {
        if (output.equals(getNameFile() + TYPE_MP4)) {
            deleteFileDowmload(getNameFile() + TYPE_MP4);
        }
        if (output.equals(OUTPUT_MUSIC)) deleteFileDowmload(OUTPUT_MUSIC);
        try {
            mFfmpeg.execute(query, new ExecuteBinaryResponseHandler() {
                @Override
                public void onStart() {
                    if (isShowDialog) {
                        mView.showDialog();
                    }
                }

                @Override
                public void onProgress(String message) {
                }

                @Override
                public void onFailure(String message) {
                }

                @Override
                public void onSuccess(String message) {
                    if (isShowDialog) {
                        mView.onExcuteFinish(output);
                        mView.removeResizeImage();
                    }
                }

                @Override
                public void onFinish() {
                    if (isShowDialog) {
                        mView.onExcuteFinish(output);
                        mView.dismisDialog();
                    }
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // Handle if FFmpeg is already running
            e.printStackTrace();
        }
    }

    @Override
    public void copy(File src, File dst) throws IOException {
        FileInputStream inStream = new FileInputStream(src);
        FileOutputStream outStream = new FileOutputStream(dst);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
    }

    @Override
    public void shareVideo() {
        ShareVideo shareVideo = new ShareVideo.Builder()
            .setLocalUrl(mView.getUriVideo())
            .build();
        ShareVideoContent content = new ShareVideoContent.Builder()
            .setVideo(shareVideo)
            .build();
        ShareApi.share(content, null);
    }
}
