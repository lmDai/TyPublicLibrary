package com.rrju.library.ui.datatime;

import android.view.View;


import com.rrju.library.R;
import com.rrju.library.utils.DateTimeUtils;

import java.util.Calendar;

/**
 * 开始和结束时间滑轮
 * 
 * @author johnny
 * 
 */
public class WheelStartAndEndTimeMain {

	private View view;
	private WheelView start_wv_hours;
	private WheelView start_wv_mins;
	private WheelView end_wv_hours;
	private WheelView end_wv_mins;
	public int screenheight;

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public WheelStartAndEndTimeMain(View view) {
		super();
		this.view = view;
		setView(view);
	}

	/**
	 * 弹出开始和结束时间选择器
	 * 
	 * @param differTime
	 */
	public void initTimePicker(int differTime) {
		long time = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateTimeUtils.getHourAndMinuteDateTime(time));
		int start_h = calendar.get(Calendar.HOUR_OF_DAY);
		int start_m = calendar.get(Calendar.MINUTE);
		differTime = differTime == 0 ? 0 : differTime;
		time = time + (differTime * 60 * 1000);
		calendar.setTime(DateTimeUtils.getHourAndMinuteDateTime(time));
		int end_h = calendar.get(Calendar.HOUR_OF_DAY);
		int end_m = calendar.get(Calendar.MINUTE);
		initTimePicker(start_h, start_m, end_h, end_m);
	}

	/**
	 * @Description: TODO 弹出开始和结束时间选择器
	 */
	public void initTimePicker(int start_h, int start_m, int end_h, int end_m) {
		// 开始时间
		start_wv_hours = (WheelView) view.findViewById(R.id.start_hour);
		start_wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
		start_wv_hours.setCyclic(true);
		// start_wv_hours.setLabel("时");// 添加文字
		start_wv_hours.setCurrentItem(start_h);
		start_wv_mins = (WheelView) view.findViewById(R.id.start_min);
		start_wv_mins.setAdapter(new NumericWheelAdapter(0, 59));
		start_wv_mins.setCyclic(true);
		// start_wv_mins.setLabel("分");
		start_wv_mins.setCurrentItem(start_m);
		// 结束时间
		end_wv_hours = (WheelView) view.findViewById(R.id.end_hour);
		end_wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
		end_wv_hours.setCyclic(true);
		// end_wv_hours.setLabel("时");// 添加文字
		end_wv_hours.setCurrentItem(end_h);
		end_wv_mins = (WheelView) view.findViewById(R.id.end_min);
		end_wv_mins.setAdapter(new NumericWheelAdapter(0, 59));
		end_wv_mins.setCyclic(true);
		// end_wv_mins.setLabel("分");
		end_wv_mins.setCurrentItem(end_m);
		// 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
		if (m_textSize == 0) {
			m_textSize = (screenheight / 100) * 3;
		}
		start_wv_hours.TEXT_SIZE = m_textSize;
		start_wv_mins.TEXT_SIZE = m_textSize;
		end_wv_hours.TEXT_SIZE = m_textSize;
		end_wv_mins.TEXT_SIZE = m_textSize;
		if (mWidth != 0 || mHeight != 0) {
			start_wv_hours.setWidthHeight(mWidth, mHeight);
			start_wv_mins.setWidthHeight(mWidth, mHeight);
			end_wv_hours.setWidthHeight(mWidth, mHeight);
			end_wv_mins.setWidthHeight(mWidth, mHeight);
		}
	}

	private int m_textSize = 0;

	public void setTextSize(int textSize) {
		m_textSize = textSize;
	}

	private int mWidth, mHeight;

	public void setWidthHeight(int width, int height) {
		this.mWidth = width;
		this.mHeight = height;
	}

	/**
	 * 获取开始时间
	 * 
	 * @return
	 */
	public String getStartTime() {
		String startTime = String
				.format(" %02d:%02d", start_wv_hours.getCurrentItem(),
						start_wv_mins.getCurrentItem());
		return startTime;
	}

	/**
	 * 获取结束时间
	 * 
	 * @return
	 */
	public String getEndTime() {
		String endTime = String.format(" %02d:%02d",
				end_wv_hours.getCurrentItem(), end_wv_mins.getCurrentItem());
		return endTime;
	}
}
