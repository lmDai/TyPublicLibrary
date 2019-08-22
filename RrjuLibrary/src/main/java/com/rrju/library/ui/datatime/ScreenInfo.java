package com.rrju.library.ui.datatime;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * 得到屏幕宽高密度等
 * 
 * @author wen
 * 
 */
public class ScreenInfo {
	private Context activity;
	/** 屏幕宽度（像素） */
	private int width;
	/** 屏幕高度（像素） */
	private int height;
	/** 屏幕密度（0.75 / 1.0 / 1.5） */
	private float density;
	/** 屏幕密度DPI（120 / 160 / 240） */
	private int densityDpi;

	public Context getActivity() {
		return activity;
	}

	public void setActivity(Context activity) {
		this.activity = activity;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public float getDensity() {
		return density;
	}

	public void setDensity(float density) {
		this.density = density;
	}

	public int getDensityDpi() {
		return densityDpi;
	}

	public void setDensityDpi(int densityDpi) {
		this.densityDpi = densityDpi;
	}

	public ScreenInfo(Context activity) {
		this.activity = activity;
		ini();
	}

	private void ini() {
		DisplayMetrics metric = new DisplayMetrics();
		((Activity) activity).getWindowManager().getDefaultDisplay()
				.getMetrics(metric);
		width = metric.widthPixels;
		height = metric.heightPixels;
		density = metric.density;
		densityDpi = metric.densityDpi;
	}

}
