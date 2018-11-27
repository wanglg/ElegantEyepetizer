package com.shortvideo.android.leo.mvp.model.entity;


public class VideoBean {

    private String title;
    private String url;
    private String thumb;
    /**
     * 0、centerCrop  1、FIT_CENTER
     */
    public int thumbScaleType;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public VideoBean(String title, String thumb, String url) {
        this.title = title;
        this.url = url;
        this.thumb = thumb;

    }

    public VideoBean(String title, String thumb, String url, int thumbScaleType) {
        this.title = title;
        this.url = url;
        this.thumb = thumb;
        this.thumbScaleType = thumbScaleType;

    }
}
