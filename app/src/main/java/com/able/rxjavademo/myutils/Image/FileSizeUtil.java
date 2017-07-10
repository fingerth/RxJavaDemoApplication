package com.able.rxjavademo.myutils.Image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.TextUtils;


import com.able.rxjavademo.myutils.LocalSaveFileUtils;
import com.able.rxjavademo.myutils.MyLogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * ======================================================
 * Created by Administrator -周晓明 on 2016/8/22.
 * <p/>
 * 版权所有，违者必究！
 * <详情描述/>
 */
public class FileSizeUtil {
    private final static String TAG = "FileSizeUtil";
    /**
     * 得到指定路径图片的options，不加载内存
     *
     * @param srcPath 源图片路径
     * @return Options {@link BitmapFactory.Options}
     */
    public static BitmapFactory.Options getBitmapOptions(String srcPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, options);
        return options;
    }

    /**
     * 压缩指定路径图片，并将其保存在缓存目录中;<br>
     * 通过isDelSrc判定是否删除源文件，并获取到缓存后的图片路径;<br>
     * 图片过大可能OOM
     *
     * @param context
     * @param srcPath
     * @param rqsW
     * @param rqsH
     * @param isDelSrc
     * @return
     */
    public static String compressBitmap(Context context, String srcPath,
                                        Bitmap.CompressFormat format,
                                        int rqsW, int rqsH, boolean isDelSrc) {
        Bitmap bitmap = compressBitmap(srcPath, rqsW, rqsH);
        File srcFile = new File(srcPath);
        String desPath = getImageCacheDir(context) + "share_" + srcFile.getName();
        clearCropFile(desPath);
        int degree = getDegrees(srcPath);
        try {
            if (degree != 0) bitmap = rotateBitmap(bitmap, degree);
            File file = new File(desPath);
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(format, 70, fos);
            fos.close();
            if (isDelSrc) srcFile.deleteOnExit();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return desPath;
    }

    public static File compressBitmapToFile(Context context, String srcPath,
                                            Bitmap.CompressFormat format,
                                            int rqsW, int rqsH, boolean isDelSrc) {
        Bitmap bitmap = compressBitmap(srcPath, rqsW, rqsH);
        File srcFile = new File(srcPath);
        File prentFiles = new File(getImageCacheDir(context));
        File desPathFile = new File(prentFiles, "share_" + srcFile.getName());
        String desPath = desPathFile.getAbsolutePath();
        MyLogUtils.setTag(TAG, "压缩后图片所在文件夹:" + getImageCacheDir(context));
        MyLogUtils.setTag(TAG,"压缩后图片位置:" + desPath);
        clearCropFile(desPath);
        int degree = getDegrees(srcPath);
        try {
            if (degree != 0) bitmap = rotateBitmap(bitmap, degree);
            FileOutputStream fos = new FileOutputStream(desPathFile);
            bitmap.compress(format, 70, fos);
            fos.close();
            if (isDelSrc) srcFile.deleteOnExit();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return desPathFile;
    }

    /**
     * 压缩指定路径的图片，并得到图片对象
     *
     * @param path bitmap source path
     * @return Bitmap {@link Bitmap}
     */
    public static Bitmap compressBitmap(String path, int rqsW, int rqsH) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, rqsW, rqsH);
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * calculate the bitmap sampleSize
     *
     * @param options
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int rqsW, int rqsH) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (rqsW == 0 || rqsH == 0) return 1;
        if (height > rqsH || width > rqsW) {
            final int heightRatio = Math.round((float) height / (float) rqsH);
            final int widthRatio = Math.round((float) width / (float) rqsW);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 获取图片缓存路径
     *
     * @param context
     * @return
     */
    private static String getImageCacheDir(Context context) {
        //String dir = context.getCacheDir() + "Image" + File.separator;
        File file = LocalSaveFileUtils.getImgFiles();
        if (!file.exists()) file.mkdirs();
        return file.getAbsolutePath();
    }

    /**
     * 删除文件
     *
     * @param path 文件路径
     * @return 成功与否
     */
    public static boolean clearCropFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        //有时文件明明存在  但 file.exists为false

        File file = new File(path);

        //System.out.println("工具判断:"+FileUtils.exists(file)+" 原始判断:"+file.exists()+" \npath:"+file.getPath());

        if (file.exists()) {
            boolean result = file.delete();
            if (result) {
                System.out.println("Cached crop file cleared.");
            } else {
                System.out.println("Failed to clear cached crop file.");
            }
            return result;
        } else {
            System.out.println("Trying to clear cached crop file but it does not exist.");
        }

        return false;
    }

    /**
     * get the orientation of the bitmap {@link ExifInterface}
     *
     * @param path 图片路径
     * @return
     */
    public static int getDegrees(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * rotate the bitmap
     *
     * @param bitmap
     * @param degrees
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degrees);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            return bitmap;
        }
        return null;
    }
}
