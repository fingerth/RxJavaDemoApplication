package com.able.rxjavademo.myutils;

import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StaticUtils {

    public static final String TAG = "StaticUtils";

    public static int sysWidth = 0;
    public static int sysHeight = 0;
    public static String memberId = "";//
    public static String sLanguage = "";//語言hk、cn、en
    public static String currency = "";//货币单位
    public static String currencySymbol = "";//货币$

    public static String OrderNo = "";
    public static boolean isUseGoogle = false;

    /**
     * 获取手机的分比率，高和宽
     */
    public static void getScreen(Activity activity) {
        if (StaticUtils.sysWidth <= 0 || StaticUtils.sysHeight <= 0) {
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            StaticUtils.sysWidth = dm.widthPixels;
            StaticUtils.sysHeight = dm.heightPixels;
        }
    }

    public static int getStatusBarHeight(Context c) {
        int result = 0;
        int resourceId = c.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = c.getResources().getDimensionPixelSize(resourceId);
        }
        MyLogUtils.setTag(TAG, "" + result);
        return result;
    }

    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static int dp2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }


    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 震动一下
     */
    public static void virbate(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }


    /**
     * 获取系统时间
     */
    public static String getSystemTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }

    public static String getSystemDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }

    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }


    /**
     * 设置日期格式
     */
    public static String getSystemTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(time);//获取当前时间
        return formatter.format(curDate);
    }

    /**
     * 判断邮箱
     */
    public static boolean matchesEmail(String sEmail) {

        String format = "[_a-zA-Z\\d\\-\\./]+@[_a-zA-Z\\d\\-]+(\\.[_a-zA-Z\\d\\-]+)+";
        if (!TextUtils.isEmpty(sEmail) && sEmail.matches(format))
            return true;
        // 邮箱名不合法，
        //ABLEToastUtils.showToast(ABLEFindPasswordActivity.this, AppConstants.appStrMap.get(AppConstants.please_enter_true_email));
        return false;

    }

    /**
     * 修改数字的格式
     */
    public static String getDoubleToFormatString(Double money) {
        int position = (money + "").indexOf(".");
        int strA = Integer.parseInt((money + "").substring(0, position));
        int strB = Integer.parseInt((money + "").substring(position + 1));
        DecimalFormat df = new DecimalFormat();
        if (strB > 0) {//有小数
            if (1000000 < strA && strA < 9999999) {
                df.applyPattern("0,000,000.00");
            } else if (100000 < strA && strA < 999999) {
                df.applyPattern("000,000.00");
            } else if (10000 < strA && strA < 99999) {
                df.applyPattern("00,000.00");
            } else if (1000 < strA && strA < 9999) {
                df.applyPattern("0,000.00");
            } else if (100 < strA && strA < 999) {
                df.applyPattern("000.00");
            } else if (10 < strA && strA < 99) {
                df.applyPattern("00.00");
            } else if (strA < 9) {
                df.applyPattern("0.00");
            }
        } else {//没有小数
            if (1000000 < strA && strA < 9999999) {
                df.applyPattern("0,000,000");
            } else if (100000 < strA && strA < 999999) {
                df.applyPattern("000,000");
            } else if (10000 < strA && strA < 99999) {
                df.applyPattern("00,000");
            } else if (1000 < strA && strA < 9999) {
                df.applyPattern("0,000");
            } else if (100 < strA && strA < 999) {
                df.applyPattern("000");
            } else if (10 < strA && strA < 99) {
                df.applyPattern("00");
            } else if (strA < 9) {
                df.applyPattern("0");
            }
        }
        return df.format(money);
    }

}
