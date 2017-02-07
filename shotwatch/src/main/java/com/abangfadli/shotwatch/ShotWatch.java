package com.abangfadli.shotwatch;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;

/**
 * Created by ahmadfadli on 1/31/17.
 */

public class ShotWatch {
    private final HandlerThread mHandlerThread;
    private final Handler mHandler;
    private final ContentResolver mContentResolver;
    private final ContentObserver mContentObserver;
    private final Listener mListener;

    public ShotWatch(ContentResolver contentResolver, Listener listener) {
        mHandlerThread = new HandlerThread("ShotWatch");
        mHandlerThread.start();

        mHandler = new Handler(mHandlerThread.getLooper());
        mContentResolver = contentResolver;
        mContentObserver = new ScreenShotObserver(mHandler, contentResolver, listener);
        mListener = listener;
    }

    public void register() {
        mContentResolver.registerContentObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                true,
                mContentObserver
        );
    }

    public void unregister() {
        mContentResolver.unregisterContentObserver(mContentObserver);
    }


    public interface Listener {
        void onScreenShotTaken(ScreenshotData screenshotData);
    }
}
