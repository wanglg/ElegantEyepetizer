package com.android.leo.base.glide;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.security.MessageDigest;


public class CircleTransformation extends BitmapTransformation {
    private static final String ID = CircleTransformation.class.getName();
    private static final byte[] ID_BYTES = ID.getBytes(Key.CHARSET);

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return TransformationUtils.circleCrop(pool, toTransform, outWidth, outHeight);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof CircleTransformation;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }
}
