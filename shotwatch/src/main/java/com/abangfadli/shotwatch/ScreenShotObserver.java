package com.abangfadli.shotwatch;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ahmadfadli on 1/31/17.
 */

public class ScreenShotObserver extends ContentObserver {

    private final String[] PROJECTION = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA
    };

    private final String MEDIA_EXTERNAL_URI_STRING = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString();
    private final ArrayList<String> FILE_NAME_PREFIX = new ArrayList<String>(
            Arrays.asList("screenshot","screenshots", //e.g. SamSung
                    "capture", "capture+" //e.g. LG
            ));
    private final ArrayList<String> PATH_SCREENSHOT = new ArrayList<String>(
            Arrays.asList("screenshot/","screenshots/", //e.g. SamSung
                    "capture/","capture+/" //e.g. LG
            ));

    private ContentResolver mContentResolver;
    private final ShotWatch.Listener mListener;

    public ScreenShotObserver(Handler handler, ContentResolver contentResolver, ShotWatch.Listener listener) {
        super(handler);
        mContentResolver = contentResolver;
        mListener = listener;
    }

    @Override
    public boolean deliverSelfNotifications() {
        return super.deliverSelfNotifications();
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        if (isSingleImageFile(uri)) {
            handleItem(uri);
        }
    }

    private boolean isSingleImageFile(Uri uri) {
        return uri.toString().matches(MEDIA_EXTERNAL_URI_STRING + "/[0-9]+");
    }

    private void handleItem(Uri uri) {
        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(uri, PROJECTION, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                final ScreenshotData screenshotData = generateScreenshotDataFromCursor(cursor);
                if (screenshotData != null) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            mListener.onScreenShotTaken(screenshotData);
                        }
                    });
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private ScreenshotData generateScreenshotDataFromCursor(Cursor cursor) {
        final long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
        final String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
        final String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

        if (isPathScreenshot(path) && isFileScreenshot(fileName)) {
            return new ScreenshotData(id, fileName, path);
        } else {
            return null;
        }
    }

    private boolean isFileScreenshot(String fileName) {
        String fileNameLowerCase = fileName.toLowerCase();
        Log.d("fileNameLowerCase",fileNameLowerCase);
        for (String str : FILE_NAME_PREFIX) {
            if (fileNameLowerCase.startsWith(str)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPathScreenshot(String path) {
        String pathLowerCase = path.toLowerCase();
        Log.d("pathLowerCase",pathLowerCase);
        for (String sPath : PATH_SCREENSHOT) {
            if (pathLowerCase.contains(sPath)) {
                return true;
            }
        }
        return false;
    }
}
