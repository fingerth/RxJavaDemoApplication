package com.able.rxjavademo.myutils;

import android.app.Activity;
import android.os.Environment;

import java.io.File;

/**
 * ======================================================
 * Created by Administrator -周晓明 on 2016/9/21.
 * <p>
 * 版权所有，违者必究！
 * <详情描述/>
 */
public class LocalSaveFileUtils {

    public final static String CACHE_PATH = "EaseSales";
    public final static String CACHE_PATH_APK = "EaseSales/apk";
    public final static String CACHE_PATH_IMG = "EaseSales/image";
    public final static String CACHE_PATH_DEVICEID = "deviceId";
    public final static String PATH_IMG = "image";
    public final static String PATH_APK = "apk";


    /**
     * 获取apk所在的文件夹
     *
     * @return x
     */
    public static File getApkFiles() {
        File dir = Environment.getExternalStorageDirectory();
        File destDir = new File(dir, CACHE_PATH);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        File apkFiles = new File(destDir, PATH_APK);
        if (!apkFiles.exists()) {
            apkFiles.mkdirs();
        }
        return apkFiles;
    }

    public static File getImgFiles() {
        File dir = Environment.getExternalStorageDirectory();
        File destDir = new File(dir, CACHE_PATH);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        File imgFiles = new File(destDir, PATH_IMG);
        if (!imgFiles.exists()) {
            imgFiles.mkdirs();
        }
        return imgFiles;
    }

    public static File getDeviceIdFiles() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File dir = Environment.getExternalStorageDirectory();
            File destDir = new File(dir, CACHE_PATH);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            File apkFiles = new File(destDir, CACHE_PATH_DEVICEID);
            if (!apkFiles.exists()) {
                apkFiles.mkdirs();
            }
            return apkFiles;
        }
        return null;
    }


    public static void cleanApkFiles() {
        File destDir = getApkFiles();
        if (destDir.listFiles() != null && destDir.listFiles().length > 0) {
            for (File f : destDir.listFiles()) {
                if (f.isFile()) {
                    f.delete();
                }
            }
        }
    }

    /**
     * 获取apk文件
     *
     * @param activity xx
     * @return x
     */
    public static File getApkFile(Activity activity) {
        return new File(getApkFiles(), getApkName(activity));
    }

    public static String getApkName(Activity activity) {
        return "update_" + VersionUtils.getVersionCode(activity) + ".apk";
    }

}
