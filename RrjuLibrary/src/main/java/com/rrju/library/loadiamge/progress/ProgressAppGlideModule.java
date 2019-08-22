package com.rrju.library.loadiamge.progress;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.rrju.library.utils.PathUtils;

import java.io.InputStream;

/**
 * @author by sunfusheng on 2017/6/14.
 */
@GlideModule
public class ProgressAppGlideModule extends AppGlideModule {


    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(ProgressManager.getOkHttpClient()));
    }

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        //  super.applyOptions(context, builder);
        int diskCacheSizeBytes = 1024 * 1024 * 1024; // 250 MB
        //手机app路径
        String appRootPath = PathUtils.getInstance().getFYTempPath();
        builder.setDiskCache(
                new DiskLruCacheFactory(appRootPath + "/GlideDisk", diskCacheSizeBytes)
        );

    }
}