package com.framgia.photoeditor.util;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.framgia.photoeditor.R;

/**
 * Created by nhahv on 1/16/2017.
 * <></>
 */
public class RequestPermissionUtils {
    public static final int PERMISSION_CALLBACK_STORAGE = 101;
    public static final int PERMISSION_CALLBACK_CAMERA = 102;

    public static boolean requestStorage(final Activity activity) {
        if (ActivityCompat
            .checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
            PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.READ_PHONE_STATE)) {
                new AlertDialog.Builder(activity)
                    .setTitle(activity.getString(R.string.title_permission_storage))
                    .setMessage(activity.getString(R.string.msg_permission_storage))
                    .setPositiveButton(activity.getString(R.string.action_grant),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat
                                    .requestPermissions(activity, new String[]{Manifest.permission
                                        .WRITE_EXTERNAL_STORAGE}, PERMISSION_CALLBACK_STORAGE);
                            }
                        })
                    .setNegativeButton(activity.getString(R.string.action_cancel), null)
                    .show();
            } else {
                ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_CALLBACK_STORAGE);
            }
            return true;
        }
        return false;
    }

    public static boolean requestCamera(final Activity activity) {
        if (ActivityCompat
            .checkSelfPermission(activity, Manifest.permission.CAMERA) !=
            PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.READ_PHONE_STATE)) {
                new AlertDialog.Builder(activity)
                    .setTitle(activity.getString(R.string.title_permission_camera))
                    .setMessage(activity.getString(R.string.msg_permission_camera))
                    .setPositiveButton(activity.getString(R.string.action_grant),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat
                                    .requestPermissions(activity, new String[]{Manifest.permission
                                        .CAMERA}, PERMISSION_CALLBACK_CAMERA);
                            }
                        })
                    .setNegativeButton(activity.getString(R.string.action_cancel), null)
                    .show();
            } else {
                ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CAMERA}, PERMISSION_CALLBACK_CAMERA);
            }
            return true;
        }
        return false;
    }
}
