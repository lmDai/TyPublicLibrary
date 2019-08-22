package com.rrju.library.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.ViewParent;
import android.widget.Button;

@SuppressLint("AppCompatCustomView")
public class ExtButton extends Button implements Rotatable {

	// The color filter to apply when the button is pressed
	static final ColorFilter PRESSED_FILTER = new LightingColorFilter(
			Color.GRAY, 0x010101);
	// static final ColorFilter DISABLED_FILTER = new LightingColorFilter(
	// Color.DKGRAY, 0x010101);
	static final ColorFilter DISABLED_FILTER = new ColorMatrixColorFilter(
			new ColorMatrix(new float[] { 0.2f, 0.2f, 0.2f, 0, 0, 0.2f, 0.2f,
					0.2f, 0, 0, 0.2f, 0.2f, 0.2f, 0, 0, 0, 0, 0, 1, 0 }));
	private int m_nRepeatClickIntervalTime = 700;
	private long m_lastClickTime;
	private boolean m_bEnableExtState;

	/**
	 * 设置是否支持扩展状态
	 *
	 * @param value
	 */
	public void enableExtState(boolean value) {
		m_bEnableExtState = value;
	}

	/**
	 * 获取是否支持扩展状态
	 *
	 * @return
	 */
	public boolean getExtState() {
		return m_bEnableExtState;
	}

	public ExtButton(Context context) {
		this(context, null);
	}

	public ExtButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		m_bEnableExtState = false;
	}

	@Override
	public boolean performClick() {
		long time = System.currentTimeMillis();
		long timeD = time - m_lastClickTime;
		if (0 < timeD && timeD < m_nRepeatClickIntervalTime) {
			return false;
		}
		m_lastClickTime = time;
		return super.performClick();
	}

	@Override
	public boolean isFocused() {
		if (getEllipsize() == TruncateAt.MARQUEE) {
			return true;
		}
		return super.isFocused();
	}

	/**
	 * 设置重复点击间隔时间
	 *
	 * @param nIntervalTime
	 */
	public void setRepeatClickIntervalTime(int nIntervalTime) {
		m_nRepeatClickIntervalTime = nIntervalTime;
	}

	/**
	 * 设置按钮icon,默认为button的top icon
	 *
	 * @param resId
	 */
	public void setImageResource(int resId) {
		if (0 == resId) {
			this.setCompoundDrawables(null, null, null, null);
		} else {
			Drawable dwTop = this.getResources().getDrawable(resId);
			dwTop.setBounds(0, 0, dwTop.getIntrinsicWidth(),
					dwTop.getIntrinsicHeight());
			setCompoundDrawablePadding(-dwTop.getIntrinsicHeight());
			this.setCompoundDrawables(null, dwTop, null, null);
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void setOrientation(int orientation) {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			this.setRotation(-orientation);
		} else {
			ViewParent vp = getParent();
			if (vp instanceof Rotatable) {
				Rotatable rp = (Rotatable) vp;
				rp.setOrientation(orientation);
			}
		}
	}

	void onDrawableStateChange(Drawable dwThis, int[] states) {
		boolean enabled = false;
		boolean pressed = false;

		for (int state : states) {
			if (state == android.R.attr.state_enabled)
				enabled = true;
			else if (state == android.R.attr.state_pressed)
				pressed = true;
		}
		if (m_bEnableExtState) {
			if (enabled && pressed) {
				dwThis.setColorFilter(PRESSED_FILTER);
			} else if (!enabled) {
				dwThis.clearColorFilter();
				dwThis.setColorFilter(DISABLED_FILTER);
			} else {
				dwThis.clearColorFilter();
			}
		}
		dwThis.invalidateSelf();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setBackgroundDrawable(Drawable d) {
		if (null != d) {
			SAutoBgButtonBackgroundDrawable layer = new SAutoBgButtonBackgroundDrawable(
					d);
			super.setBackgroundDrawable(layer);
		} else {
			super.setBackgroundDrawable(d);
		}
	}

	@Override
	public void setCompoundDrawables(Drawable left, Drawable top,
			Drawable right, Drawable bottom) {
		if (top != null) {
			if (top instanceof BitmapDrawable) {
				BitmapDrawable setLeft = (BitmapDrawable) top;
				Rect rectBound = new Rect(top.getBounds());
				top = new StateBitmapDrawable(setLeft.getBitmap());
				top.setBounds(rectBound);
			}
		}
		if (left != null) {
			if (left instanceof BitmapDrawable) {
				BitmapDrawable setLeft = (BitmapDrawable) left;
				Rect rectBound = new Rect(left.getBounds());
				left = new StateBitmapDrawable(setLeft.getBitmap());
				left.setBounds(rectBound);
			}
		}
		if (right != null) {
			if (right instanceof BitmapDrawable) {
				BitmapDrawable setLeft = (BitmapDrawable) right;
				Rect rectBound = new Rect(right.getBounds());
				right = new StateBitmapDrawable(setLeft.getBitmap());
				right.setBounds(rectBound);
			}
		}
		if (bottom != null) {
			if (bottom instanceof BitmapDrawable) {
				BitmapDrawable setLeft = (BitmapDrawable) bottom;
				Rect rectBound = new Rect(bottom.getBounds());
				bottom = new StateBitmapDrawable(setLeft.getBitmap());
				bottom.setBounds(rectBound);
			}
		}
		super.setCompoundDrawables(left, top, right, bottom);
	}

	protected class StateBitmapDrawable extends BitmapDrawable {

		@SuppressWarnings("deprecation")
		public StateBitmapDrawable(Bitmap bitmap) {
			super(bitmap);
			// this.setCallback(this);
		}

		@Override
		protected boolean onStateChange(int[] states) {
			onDrawableStateChange(this, states);
			return super.onStateChange(states);
		}

		@Override
		public boolean isStateful() {
			return true;
		}
	}

	/**
	 * The stateful LayerDrawable used by this button.
	 */
	protected class SAutoBgButtonBackgroundDrawable extends LayerDrawable {

		public SAutoBgButtonBackgroundDrawable(Drawable d) {
			super(new Drawable[] { d });
		}

		@Override
		protected boolean onStateChange(int[] states) {
			onDrawableStateChange(this, states);
			return super.onStateChange(states);
		}

		@Override
		public boolean isStateful() {
			return true;
		}
	}

}
