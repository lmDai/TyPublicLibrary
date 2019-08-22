package com.rrju.library.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.rrju.library.R;
import com.rrju.library.titlebar.CommonTitleBar;
import com.rrju.library.ui.SysAlertDialog;

/**
 * Created by tanyan on 2018-05-10.
 */
public abstract class BaseFragmentActivity extends FragmentActivity {
    public String mPageName = "BaseFragmentActivity";
    /**
     * 必须事先在assets底下创建一fonts文件夹 并放入要使用的字体文件(.ttf)
     * 并提供相对路径给creatFromAsset()来创建Typeface对象
     */
    public Typeface fontFace;
    private Resources res;
    private CommonTitleBar mTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        // 获取通用资源
        res = getResources();
        setTitleBarLayoutParam();
        initView();
        initData();
        initListener();
    }

    private void setTitleBarLayoutParam() {
        try {
            mTitleBar = findViewById(R.id.public_title_bar);
            mTitleBar.setListener((v, action, extra) -> {
                if (action == CommonTitleBar.ACTION_LEFT_BUTTON
                        || action == CommonTitleBar.ACTION_LEFT_TEXT) {
                    onBackPressed();
                }
            });
        } catch (Exception e) {
        }
    }
    /**
     * 返回一个用于显示界面的布局id
     *
     * @return 视图id
     */
    public abstract int getContentView();

    /**
     * 初始化View的代码写在这个方法中
     */
    public abstract void initView();

    /**
     * 初始数据的代码写在这个方法中，用于从服务器获取数据
     */
    public abstract void initData();

    /**
     * 初始化监听器的代码写在这个方法中
     */
    public abstract void initListener();


    @Override
    protected void onResume() {
        super.onResume();
        //友盟统计
//        MobclickAgent.onPageStart(mPageName);
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //友盟统计
//        MobclickAgent.onPageEnd(mPageName);
//        MobclickAgent.onPause(this);
    }

    public Typeface getFontFace() {
        return fontFace;
    }

    /**
     * 从资源获取字符串
     */
    public String getStr(int resId) {
        return res.getString(resId);
    }

    /**
     * 获取编辑框中内容
     */
    public String getStr(TextView editText) {
        return editText.getText().toString().trim();
    }

    /**
     * Toast提示.
     *
     * @param msg text字符串提示
     * @return void
     */
    public void toast(String msg) {
        SysAlertDialog.showAutoHideDialog(this, "", msg, Toast.LENGTH_SHORT);
    }

    public void toast(String msg, int duration) {
        SysAlertDialog.showAutoHideDialog(this, "", msg, duration);
    }

    /**
     * 标题栏左边设置文本
     *
     * @param text 左边字体内容
     * @return
     */
    public TextView setLeftTextView(String text) {
        return setLeftTextView(text, getResources().getColor(R.color.rrj_gray));
    }

    /**
     * 标题栏左边设置文本
     *
     * @param text      左边字体内容
     * @param textColor 字体颜色
     * @return
     */
    public TextView setLeftTextView(String text, int textColor) {
        return setLeftTextView(text, textColor, 16);

    }

    /***
     * 标题栏左边设置文本
     * @param text  左边字体内容
     * @param textColor 字体颜色
     * @param textSize 字体大小
     * @return
     */
    public TextView setLeftTextView(String text, int textColor, int textSize) {
        if(mTitleBar == null){
            return null;
        }
        TextView leftTextView = mTitleBar.getLeftTextView();
        leftTextView.setTextColor(getResources().getColor(textColor));
        leftTextView.setText(text);
        leftTextView.setTextSize(textSize);
        return leftTextView;
    }

    /**
     * 标题栏左边设置图标
     */
    public ImageButton setLeftImageButton() {
        return setLeftImageButton(R.drawable.comm_titlebar_back_normal);
    }

    /***
     * 标题栏左边设置图标
     * @param imageDrawable 资源文件ID
     * @return
     */
    public ImageButton setLeftImageButton(int imageDrawable) {
        if(mTitleBar == null){
            return null;
        }
        ImageButton leftImageButton = mTitleBar.getLeftImageButton();
        leftImageButton.setImageResource(imageDrawable);
        return leftImageButton;
    }

    /***
     * 获取左边自定义布局
     * @return
     */
    public View getLeftCusomView() {

        if(mTitleBar == null){
            return null;
        }
        return mTitleBar.getLeftCustomView();
    }

    /***
     * 设置每个页面的title
     * @param title 标题
     * @return
     */
    public TextView setTitle(String title) {
        return setTitle(title, R.color.rrj_text_black);
    }

    /**
     * 设置每个页面的title
     *
     * @param title     标题
     * @param textColor 字体颜色
     * @return
     */
    public TextView setTitle(String title, int textColor) {
        return setTitle(title, textColor, 16);
    }

    /**
     * 设置每个页面的title
     *
     * @param title     标题
     * @param textColor 字体颜色
     * @param textSize  字体大小
     * @return
     */
    public TextView setTitle(String title, int textColor, int textSize) {
        if(mTitleBar == null){
            return null;
        }
        TextView centerTextView = mTitleBar.getCenterTextView();
        centerTextView.setTextColor(getResources().getColor(textColor));
        centerTextView.setText(title);
        centerTextView.setTextSize(textSize);
        return centerTextView;
    }

    /***
     * 获取中心自定义布局
     * @return
     */
    public View getCenterCustomView() {
        if(mTitleBar == null){
            return null;
        }
        return mTitleBar.getCenterCustomView();
    }

    /**
     * 标题栏右边边设置文本
     *
     * @param text 左边字体内容
     * @return
     */
    public TextView setRightTextView(String text) {
        return setRightTextView(text, getResources().getColor(R.color.rrj_gray));
    }

    /**
     * 标题栏右边设置文本
     *
     * @param text      左边字体内容
     * @param textColor 字体颜色
     * @return
     */
    public TextView setRightTextView(String text, int textColor) {
        return setRightTextView(text, textColor, 16);

    }

    /***
     * 标题栏右边设置文本
     * @param text  左边字体内容
     * @param textColor 字体颜色
     * @param textSize 字体大小
     * @return
     */
    public TextView setRightTextView(String text, int textColor, int textSize) {
        if(mTitleBar == null){
            return null;
        }
        TextView leftTextView = mTitleBar.getRightTextView();
        leftTextView.setTextColor(getResources().getColor(textColor));
        leftTextView.setText(text);
        leftTextView.setTextSize(textSize);
        return leftTextView;
    }

    /**
     * 标题栏右边设置图标
     */
    public ImageButton setRightImageButton() {
        return setLeftImageButton(R.drawable.comm_titlebar_search_normal);
    }

    /***
     * 标题栏右边设置图标
     * @param imageDrawable 资源文件ID
     * @return
     */
    public ImageButton setRightImageButton(int imageDrawable) {
        if(mTitleBar == null){
            return null;
        }
        ImageButton leftImageButton = mTitleBar.getRightImageButton();
        leftImageButton.setImageResource(imageDrawable);
        return leftImageButton;
    }

    /***
     * 获取右边自定义布局
     * @return
     */
    public View getRighttCusomView() {
        if(mTitleBar == null){
            return null;
        }
        return mTitleBar.getRightCustomView();
    }

    /***
     * 点击EditText文本框之外任何地方隐藏键盘。
     * <p>
     * 通过dispatchTouchEvent每次ACTION_DOWN事件中动态判断非EditText本身区域的点击事件， 然后在事件中进行屏蔽
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    /**
     * 点击的是否为EditText
     */
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            // 获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}
