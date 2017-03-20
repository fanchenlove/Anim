package com.fanchen.anim.util;

import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

/**
 * 网络状态相关工具类
 * 
 * @author fanchen
 * 
 */
public class NetworkUtil {
  public final static int TYPE_WIFI = 1;
  public final static int TYPE_MOBILE = 2;
  public final static int TYPE_ERROR = -1;
	/**
	 * 检查当前网络状态是否可用
	 * 
	 * @param context
	 *            上下文
	 * @return 是否有网络连接
	 */
	public static boolean isNetWorkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	/**
	 * Return to the type of network: one on behalf of wifi, 2 on behalf of 2G
	 * or 3G, 3 on behalf of other
	 * 
	 * @param context
	 * @return
	 */
	public static int reportNetType(Context context) {
		int netMode = TYPE_ERROR;
		try {
			NetworkInfo info = getNetworkInfo(context);
			if (info != null && info.isAvailable()) {
				int netType = info.getType();
				if (netType == ConnectivityManager.TYPE_WIFI) {
					netMode = TYPE_WIFI;
				} else if (netType == ConnectivityManager.TYPE_MOBILE) {
					netMode = TYPE_MOBILE;
				}
			}
		} catch (Exception e) {
		}
		return netMode;
	}

	private static NetworkInfo getNetworkInfo(Context context) {
		final ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return connectivityManager.getActiveNetworkInfo();
	}

	/**
	 * @author 获取当前的网络状态 -1：没有网络 1：WIFI网络2：wap网络
	 * @param context
	 * @return
	 */
	public static int getAPNType(Context context) {
		int netType = -1;
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
				netType = 3;
			} else {
				netType = 2;
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = 1;
		}
		return netType;
	}

	/**
	 * // 判断WIFI网络是否可用
	 * 
	 * @param mContext
	 *            上下文
	 * @return 是否是WiFi连接
	 */
	public static boolean isWifiConnected(Context mContext) {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWiFiNetworkInfo = mConnectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return mWiFiNetworkInfo.isConnected();
	}

	/**
	 * // 判断MOBILE网络是否可用
	 * 
	 * @param context
	 *            上下文
	 * @return 是否是2/3G网络
	 */
	public static boolean isMobileConnected(Context context) {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mMobileNetworkInfo = mConnectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		return mMobileNetworkInfo.isConnected();
	}

	/**
	 * 打开手机网络设置界面
	 * 
	 * @param mContext
	 *            上下文
	 */
	public static void openNetworkSetting(Context mContext) {
		Intent intent = null;
		// 判断手机系统的版本 即API大于10 就是3.0或以上版本
		// if(android.os.Build.VERSION.SDK_INT>10){
		intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// }else{
		// intent = new Intent();
		// ComponentName component = new
		// ComponentName("com.android.settings","com.android.settings.WirelessSettings");
		// intent.setComponent(component);
		// intent.setAction("android.intent.action.VIEW");
		// }
		mContext.startActivity(intent);
	}

	/**
	 * 设置是否启用WIFI网络
	 * 
	 * @param context
	 * @param status
	 */
	public static void toggleWiFi(Context context, boolean status) {
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		if (status == true && !wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
		} else if (status == false && wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(false);
		}
	}

	/**
	 * 设置启用数据流量
	 * 
	 * @param context
	 */
	public final static void setMobileNetEnable(Context context) {
		Object[] arg = null;
		try {
			boolean isMobileDataEnable = invokeMethod(context,
					"getMobileDataEnabled", arg);
			if (!isMobileDataEnable) {
				invokeBooleanArgMethod(context, "setMobileDataEnabled", true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 设置不启用数据流量
	 */
	public final static void setMobileNetUnable(Context context) {
		Object[] arg = null;
		try {
			boolean isMobileDataEnable = invokeMethod(context,
					"getMobileDataEnabled", arg);
			if (isMobileDataEnable) {
				invokeBooleanArgMethod(context, "setMobileDataEnabled", false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//
	/**
	 * 执行某个影藏方法
	 * 
	 * @param context
	 * @param methodName
	 * @param arg
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean invokeMethod(Context context, String methodName,
			Object[] arg) throws Exception {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		Class ownerClass = mConnectivityManager.getClass();
		Class[] argsClass = null;
		if (arg != null) {
			argsClass = new Class[1];
			argsClass[0] = arg.getClass();
		}
		Method method = ownerClass.getMethod(methodName, argsClass);
		Boolean isOpen = (Boolean) method.invoke(mConnectivityManager, arg);
		return isOpen;
	}

	/**
	 * 调用context某个方法
	 * 
	 * @param context
	 * @param methodName
	 * @param value
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object invokeBooleanArgMethod(Context context,
			String methodName, boolean value) throws Exception {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		Class ownerClass = mConnectivityManager.getClass();
		Class[] argsClass = new Class[1];
		argsClass[0] = boolean.class;
		Method method = ownerClass.getMethod(methodName, argsClass);
		return method.invoke(mConnectivityManager, value);
	}
}
