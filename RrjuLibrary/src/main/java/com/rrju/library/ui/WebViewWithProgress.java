package com.rrju.library.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.rrju.library.R;
import com.rrju.library.utils.DisplayUtil;

/**
 * 备注: 此WebViewWithProgress继承自Relativielayout,
 * 如果要设置webview的属性，要先调用getWebView()来取得 webview的实例
 */
public class WebViewWithProgress extends RelativeLayout {

    private Context context;
    private WebView mWebView = null;
    // 水平进度条
    private ProgressBar progressBar = null;
    // 包含圆形进度条的布局
    private RelativeLayout progressBar_circle = null;
    // 进度条样式,Circle表示为圆形，Horizontal表示为水平
    private int mProgressStyle = ProgressStyle.Horizontal.ordinal();
    // 默认水平进度条高度
    public static int DEFAULT_BAR_HEIGHT = 2;
    // 水平进度条的高
    private int mBarHeight = DEFAULT_BAR_HEIGHT;
    private boolean isShowProgress = true;

    public enum ProgressStyle {
        Horizontal, Circle;
    }

    public WebViewWithProgress(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context.getApplicationContext();
        init();
    }

    public WebViewWithProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.context = context.getApplicationContext();
        TypedArray attributes = context.obtainStyledAttributes(attrs,
                R.styleable.WebViewWithProgress);
        mProgressStyle = attributes.getInt(
                R.styleable.WebViewWithProgress_progressStyle,
                ProgressStyle.Horizontal.ordinal());
        mBarHeight = attributes.getDimensionPixelSize(
                R.styleable.WebViewWithProgress_barHeight, DEFAULT_BAR_HEIGHT);
        attributes.recycle();
        init();
    }

    private void init() {
        mWebView = new WebView(context);
        this.addView(mWebView, LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        if (mProgressStyle == ProgressStyle.Horizontal.ordinal()) {
            progressBar = (ProgressBar) LayoutInflater.from(context).inflate(
                    R.layout.progress_horizontal, null);
            progressBar.setVisibility(View.GONE);
            progressBar.setMax(100);
            progressBar.setProgress(0);
            WebViewWithProgress.this.addView(progressBar,
                    LayoutParams.MATCH_PARENT,
                    DisplayUtil.dip(context, mBarHeight));
        } else {
            progressBar_circle = (RelativeLayout) LayoutInflater.from(context)
                    .inflate(R.layout.progress_circle, null);
            progressBar_circle.setVisibility(View.GONE);
            WebViewWithProgress.this.addView(progressBar_circle,
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                    if (progressBar_circle != null) {
                        progressBar_circle.setVisibility(View.GONE);
                    }
                } else {
                    if (mProgressStyle == ProgressStyle.Horizontal.ordinal()) {
                        progressBar.setVisibility(isShowProgress ? View.VISIBLE : View.GONE);
                        progressBar.setProgress(newProgress);
                    } else {
                        progressBar_circle.setVisibility(isShowProgress ? View.VISIBLE : View.GONE);
                    }
                }
            }

        });
    }

    public WebView getWebView() {
        return mWebView;
    }

    public void setIsShowProgress(boolean isShowProgress) {
        this.isShowProgress = isShowProgress;
    }
}
