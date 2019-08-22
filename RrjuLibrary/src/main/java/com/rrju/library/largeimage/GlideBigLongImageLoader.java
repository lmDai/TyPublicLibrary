package com.rrju.library.largeimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.rrju.library.largeimage.factory.FileBitmapDecoderFactory;
import com.rrju.library.loadiamge.progress.GlideApp;
import com.rrju.library.loadiamge.progress.GlideRequest;
import com.rrju.library.loadiamge.progress.OnProgressListener;
import com.rrju.library.loadiamge.progress.ProgressManager;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by Android Studio.
 * User: tanyan
 * Date: 2019-07-30
 * Time: 11:04
 */
public class GlideBigLongImageLoader {

    protected static final String ANDROID_RESOURCE = "android.resource://";
    protected static final String FILE = "file://";
    protected static final String SEPARATOR = "/";

    private String url;
    private WeakReference<LargeImageView> imageViewWeakReference;
    private GlideRequest<File> glideRequest;

    public static GlideBigLongImageLoader create(LargeImageView imageView) {
        return new GlideBigLongImageLoader(imageView);
    }

    private GlideBigLongImageLoader(LargeImageView imageView) {
        imageViewWeakReference = new WeakReference<>(imageView);
        glideRequest = GlideApp.with(getContext().getApplicationContext()).asFile();
    }

    public LargeImageView getImageView() {
        if (imageViewWeakReference != null) {
            return imageViewWeakReference.get();
        }
        return null;
    }

    public Context getContext() {
        if (getImageView() != null) {
            return getImageView().getContext();
        }
        return null;
    }

    public String getUrl() {
        return url;
    }

    public GlideRequest getGlideRequest() {
        if (glideRequest == null) {
            glideRequest = GlideApp.with(getContext().getApplicationContext()).asFile();
        }
        return glideRequest;
    }

    protected Uri resId2Uri(@DrawableRes int resId) {
        return Uri.parse(ANDROID_RESOURCE + getContext().getPackageName() + SEPARATOR + resId);
    }

    public GlideBigLongImageLoader load(@DrawableRes int resId, @DrawableRes int placeholder, @NonNull Transformation<Bitmap> transformation) {
        return loadImage(resId2Uri(resId), placeholder, transformation);
    }

    protected GlideRequest<File> loadImage(Object obj) {
        if (obj instanceof String) {
            url = (String) obj;
        }
        return glideRequest.load(obj);
    }


    public GlideBigLongImageLoader loadImage(Object obj, @DrawableRes int placeholder, Transformation<Bitmap> transformation) {
        glideRequest = loadImage(obj);
        if (placeholder != 0) {
            glideRequest = glideRequest.placeholder(placeholder);
        }

        if (transformation != null) {
            glideRequest = glideRequest.transform(transformation);
        }

        glideRequest.into(new GlideImageViewTarget(getImageView()));
        return this;
    }

    public GlideBigLongImageLoader listener(Object obj, OnProgressListener onProgressListener) {
        if (obj instanceof String) {
            url = (String) obj;
        }
        ProgressManager.addListener(url, onProgressListener);
        return this;
    }

    private class GlideImageViewTarget extends ViewTarget<View, File> {
        private ILargeImageView largeImageView;
        public <V extends View & ILargeImageView> GlideImageViewTarget(V view) {
            super(view);
            this.largeImageView = view;
        }

        @Override
        public void onLoadStarted(Drawable placeholder) {
            super.onLoadStarted(placeholder);
            largeImageView.setImageDrawable(placeholder);
        }

        @Override
        public void onLoadFailed(@Nullable Drawable errorDrawable) {
            OnProgressListener onProgressListener = ProgressManager.getProgressListener(getUrl());
            if (onProgressListener != null) {
                onProgressListener.onProgress(true, 100, 0, 0);
                ProgressManager.removeListener(getUrl());
            }
//            super.onLoadFailed(errorDrawable);
            largeImageView.setImageDrawable(errorDrawable);
        }

        @Override
        public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
            OnProgressListener onProgressListener = ProgressManager.getProgressListener(getUrl());
            if (onProgressListener != null) {
                onProgressListener.onProgress(true, 100, 0, 0);
                ProgressManager.removeListener(getUrl());
            }
            largeImageView.setImage(new FileBitmapDecoderFactory(resource));
        }
    }
}
