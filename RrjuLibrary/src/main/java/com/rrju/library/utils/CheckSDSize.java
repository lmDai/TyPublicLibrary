package com.rrju.library.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;


import java.io.File;

/**
 * 检测SD的状态和大小的类
 */
public class CheckSDSize {

    /**
     * 判断SD是否存在
     *
     * @return
     */
    public static boolean ExistSDCard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }

    /**
     * 查看SD卡总容量（单位MB）
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static long getSDAllSize(String rootPath) {
        if (TextUtils.isEmpty(rootPath)) {
            // 取得SD卡文件路径
            File path = Environment.getExternalStorageDirectory();
            rootPath = path.getPath();
        }
        StatFs sf = new StatFs(rootPath);
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 获取所有数据块数
        long allBlocks = sf.getBlockCount();
        // 返回SD卡大小
        // return allBlocks * blockSize; //单位Byte
        // return (allBlocks * blockSize)/1024; //单位KB
        return (allBlocks * blockSize) / 1024 / 1024; // 单位MB
    }

    /**
     * 查看SD卡剩余容量（单位MB）
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static long getSDFreeSize(String rootPath) {
        if (TextUtils.isEmpty(rootPath)) {
            // 取得SD卡文件路径
            File path = Environment.getExternalStorageDirectory();
            rootPath = path.getPath();
        }
        StatFs sf = new StatFs(rootPath);
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        // 返回SD卡空闲大小
        // return freeBlocks * blockSize; //单位Byte
        // return (freeBlocks * blockSize)/1024; //单位KB
        return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
    }

    /**
     * 获取SD剩余容量是否不下于当前比例
     *
     * @param size 当前小于容量值（单位MB）
     * @return
     */
    public static boolean getSDIsThanCurrentSize(String rootPath, int size) {
        if (getSDFreeSize(rootPath) > size) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取SD剩余容量是否不下于200MB
     *
     * @return
     */
    public static boolean getSDIsThanCurrentSize(String rootPath) {
        if (getSDFreeSize(rootPath) > 200) {
            return true;
        } else {
            return false;
        }
    }

//    /**
//     * 获取缓存文件大小
//     */
//    public static String getCacheFileSize(Context context) {
//        long blockSize = 0;
//        if (context.getCacheDir().exists()) {
//            blockSize = DataCleanManager.getFolderSize(context.getCacheDir());
//        }
//        if (context.getFilesDir().exists()) {
//            blockSize += DataCleanManager.getFolderSize(context
//                    .getExternalCacheDir());
//        }
//        File file = new File(PathUtils.getFYTempPath());
//        if (file.exists()) {
//            blockSize += DataCleanManager.getFolderSize(file);
//        }
//        file = new File(PathUtils.getFYImagePath());
//        if (file.exists()) {
//            blockSize += DataCleanManager.getFolderSize(file);
//        }
//        file = ImageLoader.getInstance().getDiskCache().getDirectory();
//        if (file.exists()) {
//            blockSize += DataCleanManager.getFolderSize(file);
//        }
//        return DataCleanManager.getFormatSize(blockSize);
//    }

    /**
     * 获取缓存文件大小
     */
    public static String getCacheFileSize(Context context) {
        long blockSize = 0;
//        if (context.getCacheDir().exists()) {
//            blockSize = DataCleanManager.getFolderSize(context.getCacheDir());
//        }
//        if (context.getFilesDir().exists()) {
////            blockSize += DataCleanManager.getFolderSize(context
////                    .getExternalCacheDir());
//            blockSize += DataCleanManager.getFolderSize(context
//                   .getFilesDir());
//        }
        File file ;
        file = new File(PathUtils.getInstance().getFYTempPath());
        if (file.exists()) {
            blockSize += DataCleanManager.getFolderSize(file);
        }
        file = new File(PathUtils.getInstance().getFYImagePath());
        if (file.exists()) {
            blockSize += DataCleanManager.getFolderSize(file);
        }
        file = new File(ACache.getFilePath());
        if (file.exists()) {
            blockSize += DataCleanManager.getFolderSize(file);
        }
        return DataCleanManager.getFormatSize(blockSize);
    }
}
