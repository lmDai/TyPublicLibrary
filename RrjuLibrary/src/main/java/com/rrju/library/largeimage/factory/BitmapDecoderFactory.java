package com.rrju.library.largeimage.factory;

import android.graphics.BitmapRegionDecoder;

import java.io.IOException;

/**
 * Created by Android Studio.
 * User: tanyan
 * Date: 2019-07-30
 * Time: 11:04
 */
public interface BitmapDecoderFactory {
    BitmapRegionDecoder made() throws IOException;
    int[] getImageInfo();
}