//package com.able.rxjavademo.myutils.dialog;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.support.v7.app.AlertDialog;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//
//import com.able.rxjavademo.R;
//
//
///**
// * ======================================================
// * Created by Administrator able_fingerth on 2016/10/28.
// * // Android 精仿iOS的提示库 SVProgressHUD
// * compile 'com.bigkoo:svprogresshud:1.0.6'
// * <p/>
// * 版权所有，违者必究！
// * <详情描述/>
// */
//public class DiaLogUtils {
//    private final static int dialogStype = 1;
//    private final static String loading = "加载中";
//    private final static String sure = "确认";
//
//    public static int getDialogStype() {
//        switch (dialogStype) {
//            case 0:
//                return SYSTEM_STYPE;
//            case 1:
//                return SVP_STYPE;
//            default:
//                return SYSTEM_STYPE;
//        }
//    }
//
//    public static final int SYSTEM_STYPE = 0x11;//安卓系統樣式
//    public static final int SVP_STYPE = 0x12;// Android 精仿iOS的提示库 SVProgressHUD
//
//
//    private static ProgressDialog pDialog; //系統的ProgressDialog
//    private static SVProgressHUD mSVProgressHUD; //高仿IOS
//
//    /**
//     * @param context
//     * @param canceledOnTouchOutside 遮罩下面控件点击是否可以點擊
//     */
//    public synchronized static void showProgress(Activity context, boolean canceledOnTouchOutside, int i) {
//        if (context == null || context.isFinishing()) {
//            return;
//        }
//        closeKeyboardHidden(context);
//
//        switch (getDialogStype()) {
//            case SVP_STYPE:// Android 精仿iOS的提示库 SVProgressHUD
//                initSVP(context);
//                if (canceledOnTouchOutside) {
//                    mSVProgressHUD.showWithStatus(loading, SVProgressHUD.SVProgressHUDMaskType.BlackCancel);
//                } else {
//                    mSVProgressHUD.showWithStatus(loading, SVProgressHUD.SVProgressHUDMaskType.Black);
//                }
//                break;
//            case SYSTEM_STYPE://系統樣式
//            default://系統樣式
//                pDialog = ProgressDialog.show(context, "", loading);
//                pDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
//                break;
//        }
//
////        Log.v("svp", "==showProgress==" + showCount + "=" + mSVProgressHUD.isShowing());
//
//    }
//
//    public synchronized static void showProgress(Activity context, String message, int i) {
//        if (context == null || context.isFinishing()) {
//            return;
//        }
//        closeKeyboardHidden(context);
//
//        switch (getDialogStype()) {
//
//            case SVP_STYPE:// Android 精仿iOS的提示库 SVProgressHUD
//                initSVP(context);
//
//                mSVProgressHUD.showWithStatus(message, SVProgressHUD.SVProgressHUDMaskType.Black);
//                break;
//            case SYSTEM_STYPE://系統樣式
//            default://系統樣式
//                pDialog = ProgressDialog.show(context, "", message);
//                pDialog.setCancelable(false);
//                break;
//        }
//
//
////        Log.v("svp", "==showProgress==" + showCount + "=" + mSVProgressHUD.isShowing());
//
//    }
//
//    public synchronized static void dismissProgress() {
//
//        switch (getDialogStype()) {
//
//            case SVP_STYPE:// Android 精仿iOS的提示库 SVProgressHUD
//                if (mSVProgressHUD != null) {
//
//                    mSVProgressHUD.dismiss();
//                    mSVProgressHUD.dismissImmediately();
//                    mSVProgressHUD = null;
//
////            Log.v("svp", "==dismissProgress==" + showCount + "=" + mSVProgressHUD.isShowing());
//                }
//                break;
//            case SYSTEM_STYPE://系統樣式
//            default://系統樣式
//                try {
//                    pDialog.dismiss();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                break;
//        }
//
//
//    }
//
//    //成功
//    public synchronized static void showSuccess(Activity context, String success, int i) {
//        if (context == null || context.isFinishing()) {
//            return;
//        }
//
//        switch (getDialogStype()) {
//            case SVP_STYPE:// Android 精仿iOS的提示库 SVProgressHUD
//
//                closeKeyboardHidden(context);
//                initSVP(context);
//
//                SVProgressHUD m = new SVProgressHUD(context);
//                m.showSuccessWithStatus(success, SVProgressHUD.SVProgressHUDMaskType.Black);
//                break;
//            case SYSTEM_STYPE://系統樣式
//            default://系統樣式
//                showAlertDialog(context, success);
//                break;
//        }
//
//
//    }
//
//    //网络错误时提示
//    public synchronized static void showError(Activity context, String error, int i) {
//        if (context == null || context.isFinishing()) {
//            return;
//        }
//        switch (getDialogStype()) {
//            case SVP_STYPE:// Android 精仿iOS的提示库 SVProgressHUD
//
//                closeKeyboardHidden(context);
//                initSVP(context);
//                SVProgressHUD m = new SVProgressHUD(context);
//                m.showErrorWithStatus(error, SVProgressHUD.SVProgressHUDMaskType.Black);
//                break;
//            case SYSTEM_STYPE://系統樣式
//            default://系統樣式
//                showAlertDialog(context, error);
//                break;
//        }
//
//
//    }
//
//    //提示
//    public synchronized static void showInfo(Activity context, String info, int i) {
//        if (context == null || context.isFinishing()) {
//            return;
//        }
//        switch (getDialogStype()) {
//            case SVP_STYPE:// Android 精仿iOS的提示库 SVProgressHUD
//                closeKeyboardHidden(context);
//                initSVP(context);
//                SVProgressHUD m = new SVProgressHUD(context);
//                m.showInfoWithStatus(info, SVProgressHUD.SVProgressHUDMaskType.Black);
//                break;
//            case SYSTEM_STYPE://系統樣式
//            default://系統樣式
//                showAlertDialog(context, info);
//                break;
//        }
//
//
//    }
//
//
//    /**
//     * @param context
//     */
//    private static void initSVP(Context context) {
//        if (mSVProgressHUD != null) {
//            mSVProgressHUD.dismiss();
//            mSVProgressHUD.dismissImmediately();
//            mSVProgressHUD = null;
//        }
//        mSVProgressHUD = new SVProgressHUD(context);
//    }
//
//    /**
//     * 隐藏软键盘
//     **/
//    public static void closeKeyboardHidden(Activity context) {
//        View view = context.getWindow().peekDecorView();
//        if (view != null) {
//            InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
//    }
//
//    private static void showAlertDialog(Context context, String message) {
//        // <style name="AlertDialog_Styles" parent="Theme.AppCompat.Light.Dialog" />
//        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialog_Styles);
//        builder.setMessage(message);
//        builder.setPositiveButton(sure, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//        AlertDialog dialog = builder.create();
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.show();
//    }
//
////    private static void showCustomizeLoadingProgressDialog(Context context, boolean canceledOnTouchOutside) {
////        ProgressDialog mCustomizeLoadingProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
////        mCustomizeLoadingProgressDialog.setMessage(loading);
////        mCustomizeLoadingProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
////        mCustomizeLoadingProgressDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
////        mCustomizeLoadingProgressDialog.setCancelable(true);
////        mCustomizeLoadingProgressDialog.show();
////        Point size = new Point();
////        mCustomizeLoadingProgressDialog.getWindow().getWindowManager().getDefaultDisplay().getSize(size);
////        //记得用mProgressDialog来得到这个界面的大小，实际上不加就是得到当前监听器匿名类对象的界面宽度</span>
////
////        int width = size.x;//获取界面的宽度像素
////        int height = size.y;
////        WindowManager.LayoutParams params = mCustomizeLoadingProgressDialog.getWindow().getAttributes(); //一定要用mProgressDialog得到当前界面的参数对象，否则就不是设置ProgressDialog的界面了
////        params.alpha = 1.0f;//设置进度条背景透明度
////        params.height = height / 9;//设置进度条的高度
////        params.gravity = Gravity.BOTTOM;//设置ProgressDialog的重心
////        params.width = 2 * width / 3;//设置进度条的宽度
////        params.dimAmount = 0f;//设置半透明背景的灰度，范围0~1，系统默认值是0.5，1表示背景完全是黑色的,0表示背景不变暗，和原来的灰度一样
////        mCustomizeLoadingProgressDialog.getWindow().setAttributes(params);//把参数设置给进度条，注意，一定要先show出来才可以再设置，不然就没效果了，因为只有当界面显示出来后才可以获得它的屏幕尺寸及参数等一些信息
////    }
//}
