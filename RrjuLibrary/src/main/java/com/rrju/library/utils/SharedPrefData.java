package com.rrju.library.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Shared Preference 数据类
 * 
 * @author Lee
 * @date   2016.10.13
 * @note   使用方法：
 * 		   1.使用全局配置：
 * 			   // 仅在Application中初始化一次即可
 *             SharedPrefData.init(getApplicationContext, "GlobalPrefName"); 
 *             
 *             String useString = SharedPrefData.getString(key, defValue); 
 *             SharedPrefData.putString(key, value);
 *             
 * 		   2.使用自定义配置: 
 *             SharedPrefData spd = SharedPrefData.getPref("MyPref");
 *             String useString = spd.getStringEx(key, defValue); 
 *             spd.putStringEx(key, value);
 */
public class SharedPrefData {	
	// 上下文
	private static Context mContext;
	// 全局Shared Preference文件名
	private static String mGlobalSPName;
	// 局部文件名
	private String mSharedPrefName;
	
	/**
	 * 构造函数
	 * 
	 * @param sharedPrefName	SP名
	 */
	public SharedPrefData(String sharedPrefName) {
		mSharedPrefName = sharedPrefName;
	}
	
	/**
	 * 初始化
	 * 
	 * @param appContext	应用上下文
	 * @param globalSPName  全局SP名
	 */
	public static void init(Context appContext, String globalSPName) {
		if (mContext == null && mGlobalSPName == null) {
			mContext = appContext.getApplicationContext();
			mGlobalSPName = globalSPName;
		}
	}
	
	/**
	 * 获取值
	 * 
	 * @param key		键
	 * @param defValue	默认值
	 * @return 值
	 */
	public static int getInt(String key, int defValue) {
		return getSP(mGlobalSPName).getInt(key, defValue);	
		
	}
	
	/**
	 * 设置值
	 * 
	 * @param key	键
	 * @param value 值
	 */
	public static void putInt(String key, int value) {
		Editor editor = getEditor(mGlobalSPName);
		
		editor.putInt(key, value);
		
		editor.apply();
	}
	
	/**
	 * 获取值
	 * 
	 * @param key		键
	 * @param defValue	默认值
	 * @return 值
	 */
	public static float getFloat(String key, float defValue) {
		
		return getSP(mGlobalSPName).getFloat(key, defValue);		
	}
	
	/**
	 * 设置值
	 * 
	 * @param key	键
	 * @param value 值
	 */
	public static void putFloat(String key, float value) {
		Editor editor = getEditor(mGlobalSPName);
		
		editor.putFloat(key, value);
		
		editor.apply();
	}	
	
	/**
	 * 获取值
	 * 
	 * @param key		键
	 * @param defValue	默认值
	 * @return 值
	 */
	public static long getLong(String key, long defValue) {
		
		return getSP(mGlobalSPName).getLong(key, defValue);
	}
	
	/**
	 * 设置值
	 * 
	 * @param key	键
	 * @param value 值
	 */
	public static void putLong(String key, long value) {
		Editor editor = getEditor(mGlobalSPName);
		
		editor.putLong(key, value);
		
		editor.apply();
	}	
	
	/**
	 * 获取值
	 * 
	 * @param key		键
	 * @param defValue	默认值
	 * @return 值
	 */
	public static boolean getBoolean(String key, boolean defValue) {
		
		return getSP(mGlobalSPName).getBoolean(key, defValue);
	}
	
	/**
	 * 设置值
	 * 
	 * @param key	键
	 * @param value 值
	 */
	public static void putBoolean(String key, boolean value) {
		Editor editor = getEditor(mGlobalSPName);
		editor.putBoolean(key, value);
		editor.apply();
	}	
	
	/**
	 * 获取值
	 * 
	 * @param key		键
	 * @param defValue	默认值
	 * @return 值
	 */
	public static String getString(String key, String defValue) {
		return getSP(mGlobalSPName).getString(key, defValue);
	}
	
	/**
	 * 设置值
	 *
	 * @param key	键
	 * @param value 值
	 */
	public static void putString(String key, String value) {
		Editor editor = getEditor(mGlobalSPName);
		editor.putString(key, value);
		editor.apply();
	}	
	
	/**
	 * 获取Shared Preference
	 * 
	 * @param sharedPrefName	SP名
	 * @return SP对象
	 */
	private static SharedPreferences getSP(String sharedPrefName) {
		return mContext.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
	}
	
	/**
	 * 获取Editor对象
	 * 
	 * @param sharedPrefName	SP名
	 * @return Editor对象
	 */
	private static Editor getEditor(String sharedPrefName) {
		return getSP(sharedPrefName).edit();
	}
	
	/**
	 * 获取SharedPrefData对象
	 * 
	 * @param sharedPrefName	SP名
	 * @return SharedPrefData对象
	 */
	public static SharedPrefData getPref(String sharedPrefName) {
		return new SharedPrefData(sharedPrefName);
	}
	
	/**
	 * 获取值
	 * 
	 * @param key		键
	 * @param defValue	默认值
	 * @return 值
	 */
	public int getIntEx(String key, int defValue) {
		return getSP(mSharedPrefName).getInt(key, defValue);
	}
	
	/**
	 * 设置值
	 * 
	 * @param key	键
	 * @param value 值
	 */
	public void putIntEx(String key, int value) {
		Editor editor = getEditor(mSharedPrefName);
		editor.putInt(key, value);
		editor.apply();
	}
	
	/**
	 * 获取值
	 * 
	 * @param key		键
	 * @param defValue	默认值
	 * @return 值
	 */
	public float getFloatEx(String key, float defValue) {
		return getSP(mSharedPrefName).getFloat(key, defValue);
	}
	
	/**
	 * 设置值
	 * 
	 * @param key	键
	 * @param value 值
	 */
	public void putFloatEx(String key, float value) {
		Editor editor = getEditor(mSharedPrefName);
		editor.putFloat(key, value);
		editor.apply();
	}	
	
	/**
	 * 获取值
	 * 
	 * @param key		键
	 * @param defValue	默认值
	 * @return 值
	 */
	public long getLongEx(String key, long defValue) {
		return getSP(mSharedPrefName).getLong(key, defValue);
	}
	
	/**
	 * 设置值
	 * 
	 * @param key	键
	 * @param value 值
	 */
	public void putLongEx(String key, long value) {
		Editor editor = getEditor(mSharedPrefName);
		editor.putLong(key, value);
		editor.apply();
	}	
	
	/**
	 * 获取值
	 * 
	 * @param key		键
	 * @param defValue	默认值
	 * @return 值
	 */
	public boolean getBooleanEx(String key, boolean defValue) {
		return getSP(mSharedPrefName).getBoolean(key, defValue);
	}
	
	/**
	 * 设置值
	 * 
	 * @param key	键
	 * @param value 值
	 */
	public void putBooleanEx(String key, boolean value) {
		Editor editor = getEditor(mSharedPrefName);
		editor.putBoolean(key, value);
		editor.apply();
	}	
	
	/**
	 * 获取值
	 * 
	 * @param key		键
	 * @param defValue	默认值
	 * @return 值
	 */
	public String getStringEx(String key, String defValue) {
		return getSP(mSharedPrefName).getString(key, defValue);
	}
	
	/**
	 * 设置值
	 * 
	 * @param key	键
	 * @param value 值
	 */
	public void putStringEx(String key, String value) {
		Editor editor = getEditor(mSharedPrefName);
		editor.putString(key, value);
		editor.apply();
	}
}
