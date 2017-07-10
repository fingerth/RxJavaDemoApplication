package com.able.rxjavademo.myutils.premission;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;


import com.able.rxjavademo.R;

import java.util.ArrayList;

/**
 * ======================================================
 * Created by Administrator -周晓明 on 2016/9/21.
 * <p>
 * 版权所有，违者必究！
 * <详情描述/>
 */
public class PermissionUtils {
    /**
     * 判斷有沒文件管理權限
     */
    public static boolean checkWriteExternalStorage(Activity activity) {
        //判断又没哟文件存储的权限，
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkCamera(Activity activity) {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkReadPhoneState(Activity activity) {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
    }


    public static void requestWriteExternalStorage(Activity activity) {
        requestPermissions(activity, Android6Permission.WRITE_EXTERNAL_STORAGE);
        //TODO 在申請的Activity裡面的代碼
//        @Override
//        public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {
//            switch (permsRequestCode) {
//                case 200:
//
//                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                    if (cameraAccepted) {
//                        //授权成功之后
//                    } else {
//                        showOpenPermissionsDialog();
//                    }
//
//                    break;
//            }
//        }
    }

    public static void showOpenPermissionsDialog(final Activity activity, String help, String msg, String setting, String cance) {
        PackageManager pm = activity.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(activity.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        // 得到自己的包名
        String pkgName;
        if (pi != null && !TextUtils.isEmpty(pi.packageName)) {
            pkgName = pi.packageName;
        } else {
            pkgName = activity.getString(R.string.package_name);
        }
        final String packageName = pkgName;
        //用户授权拒绝之后，友情提示一下就可以了
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);  //创建对象
        builder.setTitle(help); //设置标题
        builder.setMessage(msg);  //设置内容
        builder.setPositiveButton(setting, new DialogInterface.OnClickListener() {
            @Override
            //设置确定按钮 ，注意导包import android.content.DialogInterface.OnClickListener;
            public void onClick(DialogInterface dialog, int which) {
                Intent detail_intent = new Intent();
                detail_intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                detail_intent.addCategory(Intent.CATEGORY_DEFAULT);
                detail_intent.setData(Uri.parse("package:" + packageName));//包名
                activity.startActivity(detail_intent);
            }
        });
        builder.setNegativeButton(cance, new DialogInterface.OnClickListener() {
            @Override    //设置取消按钮
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog aler = builder.create();
        aler.setCanceledOnTouchOutside(false);
        aler.show();  //show（）
    }


    /**
     * 申请权限
     *
     * @param activity       activity
     * @param primissionsStr 权限的名称
     */
    private static void requestPermissions(Activity activity, String primissionsStr) {
        if (ContextCompat.checkSelfPermission(activity, primissionsStr) != PackageManager.PERMISSION_GRANTED) {
            int permsRequestCode = 200;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String[] perms = {primissionsStr};
                activity.requestPermissions(perms, permsRequestCode);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{primissionsStr}, permsRequestCode);
            }
        }
    }

    private static void requestPermissions(Activity activity, String primissionsStr, int code) {
        if (ContextCompat.checkSelfPermission(activity, primissionsStr) != PackageManager.PERMISSION_GRANTED) {
            int permsRequestCode = code;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String[] perms = {primissionsStr};
                activity.requestPermissions(perms, permsRequestCode);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{primissionsStr}, permsRequestCode);
            }
        }
    }


    //============================================================================================
    //本項目所有需要動態申請的權限，一次申請===========================================================

    public final static int SDK_PERMISSION_REQUEST = 127;

    public final static void getPersimmions(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();

//            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                permissions.add(Manifest.permission.READ_PHONE_STATE);
//            }
            //xxxxxxxxxxxxxxxx
//            if (activity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                permissions.add(Manifest.permission.CAMERA);
//            }
            //xxxxxxxxxxxxxxxx
            if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            //xxxxxxxxxxxxxxxx
//            if (activity.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                permissions.add(Manifest.permission.READ_PHONE_STATE);
//            }


            if (permissions.size() > 0) {
                activity.requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    //本項目所有需要動態申請的權限，一次申請===========================================================
    //============================================================================================

}
