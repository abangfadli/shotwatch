package com.abangfadli.shotwatch;

/**
 * Created by ahmadfadli on 1/31/17.
 */

public class ScreenshotData {
    private long id;
    private String fileName;
    private String path;

    public ScreenshotData(long id, String fileName, String path) {
        this.id = id;
        this.fileName = fileName;
        this.path = path;
    }

    public long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getPath() {
        return path;
    }
}
