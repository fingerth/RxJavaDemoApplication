package com.able.rxjavademo.myutils;

import android.app.Activity;

import java.util.ArrayList;

/**
 * 关闭所有的Activity
 */
public class ManageApplication {
	private static ArrayList<Activity> activityList = new ArrayList<Activity>();
	/**
	 * 添加Activity到容器中
	 */
	public static void addActivity(Activity activity) {
		activityList.add(activity);
	}

	/**
	 * 刪除Activity
	 * 在Activity走onDestroy()時，一定要調用，不然會內存洩漏
	 */
	public static void removeActivity(Activity activity) {
		activityList.remove(activity);
	}

	/**
	 * 遍历所有Activity并finish
	 */
	public static void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		activityList.clear();
		//如果想銷毀所有Activity后，殺掉當前進程。 killProcess()只能殺掉當前進程，不能殺掉其他進程。
		android.os.Process.killProcess(android.os.Process.myPid());
	}

}
