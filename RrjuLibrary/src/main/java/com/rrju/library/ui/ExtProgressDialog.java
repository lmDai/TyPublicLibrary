package com.rrju.library.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.rrju.library.R;


/**
 * 圆形进度条
 * 
 */
public class ExtProgressDialog extends Dialog {

	private TextView m_tvMessage;
	private CircleProgressBar m_pwProgress;
	private String m_strMessage;
	private boolean m_bIndeterminate;
	private int m_nMax = 100, m_nProgress = 0;

	public ExtProgressDialog(Context context) {
		super(context, R.style.dialog);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.progress_dialog, null);
		m_tvMessage = (TextView) view.findViewById(R.id.tvMessage);
		m_pwProgress = (CircleProgressBar) view.findViewById(R.id.pbProgress);
		setMessage(m_strMessage);
		setContentView(view);
		setIndeterminate(m_bIndeterminate);
		if (!m_bIndeterminate) {
			setProgress(m_nProgress);
		}
		super.onCreate(savedInstanceState);
		LayoutParams lp = getWindow().getAttributes();
		lp.gravity = Gravity.CENTER;
		this.onWindowAttributesChanged(lp);
	}

	public void setMessage(String strMessage) {
		m_strMessage = strMessage;
		if (null != m_tvMessage) {
			m_tvMessage.setText(strMessage);
			m_tvMessage.setVisibility(TextUtils.isEmpty(strMessage) ? View.GONE
					: View.VISIBLE);
		}
	}

	public void setIndeterminate(boolean indeterminate) {
		m_bIndeterminate = indeterminate;
		if (m_pwProgress != null) {
			m_pwProgress.setIndeterminate(indeterminate);
		}
	}

	public void setMax(int max) {
		m_nMax = max;
		setProgress(m_nProgress);
	}

	public int getMax() {
		return m_nMax;
	}

	public void setProgress(int nProgress) {
		nProgress = Math.min(m_nMax, nProgress);
		nProgress = Math.max(0, nProgress);
		m_nProgress = nProgress;
		if (m_pwProgress != null) {
			m_pwProgress.setMax(m_nMax);
			m_pwProgress.setProgress(m_nProgress);
		}
	}
}
