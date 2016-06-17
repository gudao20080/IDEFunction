package com.greatmap.idefunction.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.greatmap.idefunction.IdeApplication;

/**
 * Toast统一工具类
 */
public class T {
	private static Context mContext = getContext();
	public static boolean isShow = true;
	private static Toast toast;// 防止多次弹出相同的土司

	private T() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("T: cannot be instantiated");
	}

	/**
	 * 短时间显示Toast
	 * 
	 * @param message
	 */
	public static void show(CharSequence message) {
		if (isShow){
			if (toast == null) {
				toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
			} else {
				toast.setText(message);
				toast.setDuration(Toast.LENGTH_SHORT);
			}
			toast.show();
		}
	}

	/**
	 * 短时间显示Toast
	 * 
	 * @param resId
	 */
	public static void show(int resId) {
		if (isShow){
			if (isShow){
				if (toast == null) {
					toast = Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT);
				} else {
					toast.setText(resId);
					toast.setDuration(Toast.LENGTH_SHORT);
				}
				toast.show();

			}
		}
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param message
	 */
	public static void showLong(CharSequence message) {
		if (isShow){
			if (toast == null) {
				toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
			} else {
				toast.setText(message);
				toast.setDuration(Toast.LENGTH_LONG);
			}
			toast.show();
		}
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param resId
	 */
	public static void showLong(int resId) {
		if (isShow){
			if (isShow){
				if (toast == null) {
					toast = Toast.makeText(getContext(), resId, Toast.LENGTH_LONG);
				} else {
					toast.setText(resId);
					toast.setDuration(Toast.LENGTH_LONG);
				}
				toast.show();
			}
		}
	}

	/**
	 * 自定义显示Toast时间
	 * 
	 * @param message
	 * @param duration
	 */
	public static void show(CharSequence message, int duration) {
		if (isShow)
			Toast.makeText(mContext, message, duration).show();
	}

	/**
	 * 自定义显示Toast时间
	 * 
	 * @param message
	 * @param duration
	 */
	public static void show(int message, int duration) {
		if (isShow)
			Toast.makeText(mContext, message, duration).show();
	}


	/**
	 * 自定义时间显示Toast
	 *
	 * @param message 显示的内容
	 */
	public static void showCustomToast( String message, int second) {
		if (toast == null) {
			toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
		} else {
			toast.setText(message);
			toast.setDuration(Toast.LENGTH_LONG);
		}
		toast.setGravity(Gravity.CENTER, 0, 0);
		showToast(second);
	}

	private static void showToast(final int second) {
		new Thread(new Runnable() {
			int tempSecond = 0;

			@Override
			public void run() {
				while (tempSecond < second + 1) {
					toast.show();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
					tempSecond++;
				}
				toast.cancel();
			}
		}).start();
	}

	private static Context getContext() {
		return IdeApplication.getInstance();
	}

}