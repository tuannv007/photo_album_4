package com.framgia.photoeditor.util;

/**
 * Created by tuanbg on 1/5/17.
 * <></>
 */
public class Constant {
    public class Request {
        public static final int REQUEST_SELECTOR_IMAGE = 1;
        public static final int REQUEST_CODE_CAMERA = 1000;
    }

    public static final String DATA_IMAGE_JPEG = "image/jpeg";
    public static final String DATA_IMAGE_PNG = "image/png";
    public static final String DATA_DESC = " DESC";
    public static final long SPLASH_TIME_OUT = 2000;
    public static final String DATA_JPG = ".jpg";
    public static final String DATA_PNG = ".png";
    public static final String DATA_JPEG = ".jpeg";
    public static final String DATA_CAMERA = "data";
    public static final int OPEN_CAMERA = 0;
    public static final int BLACK_WHITE_IMAGE = 3;
    public static final String FOLDER_NAME = "/saved_images";

    public class Size {
        public static final int DATA_RADIUS = 200;
        public static final int DATA_SIZE_SEEKBAR = 100;
    }

    public class Bundle {
        public static final String BUNDLE_PATH_IMAGE = "BUNDLE_PATH_IMAGE";
        public static final String BUNDLE_BITMAP = "BUNDLE_BITMAP";
    }

    public class ImageSelector {
        public static final int SPAN_COUNT = 3;
        public static final String BUNDLE_TYPE_PICK_IMAGE = "BUNDLE_TYPE_PICK_IMAGE";
        public static final String BUNDLE_IMAGE_FOLDER = "BUNDLE_IMAGE_FOLDER";
        public static final String BUNDLE_LIST_IMAGE = "BUNDLE_LIST_IMAGE";
        public static final int DATA_PICK_MULTIPLE_IMAGE = 1;
        public static final int DATA_PICK_SINGLE_IMAGE = 2;
    }

    public class Font {
        public static final String FONT_BSC = "fonts/font_bsc.ttf";
    }

    public static final int CHANGE_COLOR = 1;

    public enum Feature {
        FEATURE_EFFECT(0),
        FEATURE_COLOR(1),
        FEATURE_ADJUST(2),
        FEATURE_CROP(3),
        FEATURE_HIGHLIGHT(4),
        FEATURE_ORIENTATION(5),
        FEATURE_GAMMA(6);
        private int mPosition;

        Feature(int i) {
            mPosition = i;
        }

        public void setPosition(int position) {
            this.mPosition = position;
        }
    }
}
