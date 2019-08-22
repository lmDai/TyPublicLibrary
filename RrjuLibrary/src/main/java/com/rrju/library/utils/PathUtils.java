/**
 * PathUtils.java
 * classes：com.rd.utils.PathUtils
 *
 * @author abreal
 */
package com.rrju.library.utils;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Date;

/**
 * 路径工具
 */
public class PathUtils {
    private static PathUtils instance;
    private String m_sFYRootPath;
    private String m_sFYLogPath;
    private String m_sFYTempPath;
    private String m_sFYDatabasePath;
    private String m_sFYDownloadPath;
    private String m_sFYImagePath;
    private String appName = "DaoJiaLe";

    /**
     * 获取单件实例
     *
     * @return
     */
    public static PathUtils getInstance() {
        if (null == instance)
            instance = new PathUtils();
        return instance;
    }

    /**
     * 获取本APP根目录
     *
     * @return
     */
    public String getFYRootPath() {
        return m_sFYRootPath;
    }

    /**
     * 获取系统临时目录
     *
     * @return
     */
    public String getFYDatabasePath() {
        return m_sFYDatabasePath;
    }

    /**
     * 获取系统临时目录
     *
     * @return
     */
    public String getFYTempPath() {
        return m_sFYTempPath;
    }


    /**
     * 获取download目录
     *
     * @return
     */
    public String getFYDownloadPath() {
        return m_sFYDownloadPath;
    }

    /**
     * 获取日志目录
     *
     * @return
     */
    public final String getFYLogPath() {
        String strResultPath;
        strResultPath = m_sFYLogPath;
        File fileLog = new File(strResultPath);
        checkPath(fileLog);
        return strResultPath;
    }

    /**
     * 获取image目录
     *
     * @return
     */
    public String getFYImagePath() {
        return m_sFYImagePath;
    }

    /**
     * 解析文件存储路径
     *
     */
    public void initialize(String appName) {
        this.appName = appName;
        File path = new File(StorageUtils.getStorageDirectory(), "DaoJiaLe/" + appName);
        if (!path.exists())
            path.mkdirs();
        m_sFYRootPath = path.getAbsolutePath();

        path = new File(m_sFYRootPath, "databases/");
        checkPath(path);
        m_sFYDatabasePath = path.getAbsolutePath();

        path = new File(m_sFYRootPath, "log/");
        checkPath(path);
        m_sFYLogPath = path.getAbsolutePath();

        path = new File(m_sFYRootPath, "temp/");
        checkPath(path);
        m_sFYTempPath = path.getAbsolutePath();

        path = new File(m_sFYRootPath, "download/");
        checkPath(path);
        m_sFYDownloadPath = path.getAbsolutePath();

        path = new File(m_sFYRootPath, "Image/");
        checkPath(path);
        m_sFYImagePath = path.getAbsolutePath();

//        path = context.getDir("log", Context.MODE_WORLD_WRITEABLE);
//        if (!path.exists()) {
//            path.mkdirs();
//        }
//        m_sFYDataLogPath = path.getAbsolutePath();
    }

    /**
     * 检查path，如不存在创建之<br>
     * 并检查此路径是否存在文件.nomedia,如没有创建之
     *
     * @param path
     */
    private void checkPath(File path) {
        File fNoMedia;
        if (!path.exists())
            path.mkdirs();
        fNoMedia = new File(path, ".nomedia");
        if (!fNoMedia.exists()) {
            try {
                fNoMedia.createNewFile();
            } catch (IOException e) {
            }
        }
        fNoMedia = null;
    }

    /**
     * 获取一个指定格式的临时文件
     *
     * @param strPrefix
     * @param strExtension
     * @return
     */
    public String getTempFileNameForSdcard(String strPrefix,
                                           String strExtension) {
        return getTempFileNameForSdcard(m_sFYTempPath, strPrefix, strExtension);
    }

    /**
     * 获取临时文件路径(以sdcard为root目录)
     *
     * @param strPrefix
     * @param strExtension
     * @return
     */
    public String getTempFileNameForSdcard(String strRootPath,
                                           String strPrefix, String strExtension) {
        File rootPath = new File(strRootPath);
        checkPath(rootPath);
        File localPath = new File(rootPath, String.format("%s_%s.%s",
                strPrefix, DateFormat.format("yyyyMMdd_kkmmss", new Date()),
                strExtension));
        return localPath.getAbsolutePath();
    }

    /**
     * 获取临时文件路径(以sdcard为root目录)
     *
     * @param strPrefix
     * @param strExtension
     * @return
     */
    public String getVideoShotsFileNameForSdcard(String strRootPath,
                                                 String strPrefix, String strExtension) {
        File rootPath = new File(strRootPath);
        checkPath(rootPath);
        File localPath = new File(rootPath, String.format("%s.%s", strPrefix,
                strExtension));
        return localPath.getAbsolutePath();
    }

    /**
     * 获取相册目录
     *
     * @return
     */
    public String getPhotoDir() {
        File PhotoDir = new File(StorageUtils.getStorageDirectory(), "DCIM/" + appName);
        if (!PhotoDir.exists()) {
            PhotoDir.mkdirs();
        }
        return PhotoDir.getAbsolutePath();
    }

    /**
     * 删除指定文件名前缀临时文件
     *
     * @param strPrefix 文件名前缀
     */
    public void cleanTempFilesByPrefix(final String strPrefix) {
        File fTempPath = new File(getFYTempPath());
        File[] arrCleanTmpFiles = fTempPath.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String filename) {
                return filename.startsWith(strPrefix);
            }
        });
        if (arrCleanTmpFiles != null && arrCleanTmpFiles.length > 0) {
            for (File fTmp : arrCleanTmpFiles) {
                if (fTmp.exists()) {
                    fTmp.delete();
                }
            }
        }
        arrCleanTmpFiles = null;
        fTempPath = null;
    }

    /**
     * 复制文件到目标文件
     *
     * @param fromFile  源文件
     * @param toFile    复制目标文件
     * @param isRewrite 是否替换已存在的文件
     * @return
     */
    public String copyfile(Context context, File fromFile, File toFile,
                           Boolean isRewrite) {

        if (!fromFile.exists()) {
            return "文件不存在！";
        }

        if (!fromFile.isFile()) {
            return "不是一个非法的文件！";
        }

        if (!fromFile.canRead()) {
            return "文件不能读写操作！";
        }

        if (!toFile.getParentFile().exists()) {
            toFile.getParentFile().mkdirs();
        }
        if (toFile.exists()) {
            if (!isRewrite) {
                return "文件已存在（手机相册>" + appName + "）";
            }
            toFile.delete();
        }
        try {
            FileInputStream fosfrom = new FileInputStream(fromFile);
            FileOutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c); // 将内容写到新文件当中
            }
            fosfrom.close();
            fosto.close();
            // 其次把文件插入到系统图库
//            MediaStore.Images.Media.insertImage(context.getContentResolver(),
//                    toFile.getAbsolutePath(), toFile.getName(), null);
            // 最后通知图库更新
            //刷新图库
            MediaScannerConnection.scanFile(context.getApplicationContext()
                    , new String[]{toFile.getAbsolutePath()}
                    , new String[]{"image/jpeg"}, (path, uri) -> {
                    });
//            context.sendBroadcast(new Intent(
//                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(toFile)));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("copyfile", e.getMessage());
            return "读写文件异常！";
        }
        return "图片已保存（手机相册>" + appName + "）";
    }

    /**
     * 获取缓存目录
     *
     * @param context
     * @param cacheName
     * @return
     */
    public String getCache(Context context, String cacheName) {
        File file = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String filePath = StorageUtils.getStorageDirectory() + "Android/data/" + context.getPackageName() + "/files";
            file = new File(filePath, cacheName);
        } else {
            String sdkPath = "/data/data/" + context.getPackageName()
                    + "/files";
            file = new File(sdkPath, cacheName);
        }
        if (file == null) {
            return m_sFYDownloadPath;
        }
        checkPath(file);
        return file.toString();
    }
}
