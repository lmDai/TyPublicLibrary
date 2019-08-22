package com.rrju.library.largeimage;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import android.support.annotation.DrawableRes;

import com.rrju.library.largeimage.factory.BitmapDecoderFactory;


/**
 * Created by Android Studio.
 * User: tanyan
 * Date: 2019-07-30
 * Time: 11:04
 */
public interface ILargeImageView {

    int getImageWidth();

    int getImageHeight();

    boolean hasLoad();

    void setOnImageLoadListener(BlockImageLoader.OnImageLoadListener onImageLoadListener);

    void setImage(BitmapDecoderFactory factory);

    void setImage(BitmapDecoderFactory factory, Drawable defaultDrawable);

    void setImage(Bitmap bm);

    void setImage(Drawable drawable);

    void setImage(@DrawableRes int resId);

    void setImageDrawable(Drawable drawable);

    void setScale(float scale);

    float getScale();

    BlockImageLoader.OnImageLoadListener getOnImageLoadListener();
}
