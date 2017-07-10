package com.able.rxjavademo.myutils;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by dongping-yuan on 16/7/13.
 */
public class ToastUtils {
    public static Toast mToast;

    public static void showToast(final Activity context, final String msg) {

        if ("main".equals(Thread.currentThread().getName())) {
            doToast(context, msg);
        } else {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    doToast(context, msg);
                }
            });
        }
    }

    private static void doToast(Activity context, String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
        mToast.setText(msg);
        mToast.show();
    }

}
