package com.rrju.library.loadiamge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rrju.library.loadiamge.progress.OnProgressListener;
import com.rrju.library.loadiamge.transformation.CircleTransformation;
import com.rrju.library.loadiamge.transformation.RadiusTransformation;
import com.rrju.library.ui.NiceImageView;

/**
 * @author sunfusheng on 2017/11/10.
 */
@SuppressLint("CheckResult")
public class GlideNiceImageView extends NiceImageView {

    private boolean enableState = false;
    private float pressedAlpha = 0.4f;
    private float unableAlpha = 0.3f;
    private GlideImageLoader imageLoader;

    public GlideNiceImageView(Context context) {
        this(context, null);
        init();
    }

    public GlideNiceImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public GlideNiceImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        imageLoader = GlideImageLoader.create(this);
    }

    public GlideImageLoader getImageLoader() {
        if (imageLoader == null) {
            imageLoader = GlideImageLoader.create(this);
        }
        return imageLoader;
    }

    public GlideNiceImageView apply(RequestOptions options) {
        getImageLoader().getGlideRequest().apply(options);
        return this;
    }

    public GlideNiceImageView centerCrop() {
        getImageLoader().getGlideRequest().centerCrop();
        return this;
    }

    public GlideNiceImageView fitCenter() {
        getImageLoader().getGlideRequest().fitCenter();
        return this;
    }

    public GlideNiceImageView diskCacheStrategy(@NonNull DiskCacheStrategy strategy) {
        getImageLoader().getGlideRequest().diskCacheStrategy(strategy);
        return this;
    }
    public GlideNiceImageView skipMemoryCache(@NonNull boolean isSkipMemoryCache) {
        getImageLoader().getGlideRequest().skipMemoryCache(isSkipMemoryCache);
        return this;
    }
    public GlideNiceImageView placeholder(@DrawableRes int resId) {
        getImageLoader().getGlideRequest().placeholder(resId);
        return this;
    }

    public GlideNiceImageView error(@DrawableRes int resId) {
        getImageLoader().getGlideRequest().error(resId);
        return this;
    }

    public GlideNiceImageView fallback(@DrawableRes int resId) {
        getImageLoader().getGlideRequest().fallback(resId);
        return this;
    }

    public GlideNiceImageView dontAnimate() {
        getImageLoader().getGlideRequest().dontTransform();
        return this;
    }

    public GlideNiceImageView dontTransform() {
        getImageLoader().getGlideRequest().dontTransform();
        return this;
    }

    public void load(String url) {
        load(url, 0);
    }

    public void load(String url, @DrawableRes int placeholder) {
        load(url, placeholder, 0);
    }

    public void load(String url, @DrawableRes int placeholder, int radius) {
        load(url, placeholder, radius, null);
    }

    public void load(String url, @DrawableRes int placeholder, OnProgressListener onProgressListener) {
        load(url, placeholder, null, onProgressListener);
    }

    public void load(String url, @DrawableRes int placeholder, int radius, OnProgressListener onProgressListener) {
        load(url, placeholder, new RadiusTransformation(getContext(), radius), onProgressListener);
    }

    public void load(Object obj, @DrawableRes int placeholder, Transformation<Bitmap> transformation) {
        getImageLoader().loadImage(obj, placeholder, transformation);
    }

    public void load(Object obj, @DrawableRes int placeholder, Transformation<Bitmap> transformation, OnProgressListener onProgressListener) {
        getImageLoader().listener(obj, onProgressListener).loadImage(obj, placeholder, transformation);
    }

    public void loadCircle(String url) {
        loadCircle(url, 0);
    }

    public void loadCircle(String url, @DrawableRes int placeholder) {
        loadCircle(url, placeholder, null);
    }

    public void loadCircle(String url, @DrawableRes int placeholder, OnProgressListener onProgressListener) {
        load(url, placeholder, new CircleTransformation(), onProgressListener);
    }

    public void loadDrawable(@DrawableRes int resId) {
        loadDrawable(resId, 0);
    }

    public void loadDrawable(@DrawableRes int resId, @DrawableRes int placeholder) {
        loadDrawable(resId, placeholder, null);
    }

    public void loadDrawable(@DrawableRes int resId, @DrawableRes int placeholder, @NonNull Transformation<Bitmap> transformation) {
        getImageLoader().load(resId, placeholder, transformation);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (enableState) {
            if (isPressed()) {
                setAlpha(pressedAlpha);
            } else if (!isEnabled()) {
                setAlpha(unableAlpha);
            } else {
                setAlpha(1.0f);
            }
        }
    }

    public GlideNiceImageView enableState(boolean enableState) {
        this.enableState = enableState;
        return this;
    }

    public GlideNiceImageView pressedAlpha(float pressedAlpha) {
        this.pressedAlpha = pressedAlpha;
        return this;
    }

    public GlideNiceImageView unableAlpha(float unableAlpha) {
        this.unableAlpha = unableAlpha;
        return this;
    }
}
