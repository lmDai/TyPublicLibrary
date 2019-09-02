package com.rrju.library.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rrju.library.R;
import com.rrju.library.titlebar.CustomTitleBar;
import com.rrju.library.ui.SysAlertDialog;


/**
 * Created by tanyan on 2018-05-10.
 * fragment 基类
 */
public abstract class BaseFragment extends Fragment {
    /**
     * 友盟统计当前页面统计（当前页面名称），用于页面访问路径统计
     */
    public String mPageName = "BaseFragment";
    public Typeface fontFace;
    // 整个fragment 显示view
    public View view;
    // activity
    private Context context;
    public FragmentActivity mContext;
    private Resources res;
    /**
     * fragment管理
     */
    public FragmentManager m_fragmentManager;

    private CustomTitleBar mTitleBar;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFragmentContentView(getContentView());
        this.mContext = getActivity();
        // 获取通用资源
        res = getResources();
        // 获取FragmentManager 对象
        m_fragmentManager = getActivity().getSupportFragmentManager();
        setTitleBarLayoutParam();
        initHttp();
        initView();
        initData();
        initListener();
    }

    @Override
    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(mPageName);
    }
    private void setTitleBarLayoutParam() {
        try {
            mTitleBar = (CustomTitleBar) findViewById(R.id.public_title_bar);
            mTitleBar.setListener((v, action, extra) -> {
                if (action == CustomTitleBar.ACTION_LEFT_BUTTON
                        || action == CustomTitleBar.ACTION_LEFT_TEXT) {
                    mContext.onBackPressed();
                }
            });
        } catch (Exception e) {

        }
    }
    @Override
    public void onPause() {
        super.onPause();
        // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
//        MobclickAgent.onPageEnd(mPageName);
    }

    private void setFragmentContentView(int layout) {
        view = LayoutInflater.from(context).inflate(layout, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return view;
    }


    public View findViewById(int id) {
        return view.findViewById(id);
    }

    /**
     * 返回一个用于显示界面的布局id
     */
    public abstract int getContentView();

    /**
     * 初始化View的代码写在这个方法中
     *
     * @param
     */
    public abstract void initView();

    /**
     * 初始化监听器的代码写在这个方法中
     */
    public abstract void initListener();

    /**
     * 初始数据的代码写在这个方法中，用于从服务器获取数据
     */
    public abstract void initData();
    /**
     * 初始化网络请求
     */
    public abstract void initHttp();
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
        intent.setClass(getActivity(), cls);
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
        intent.setClass(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    /**
     * 从资源获取字符串
     */
    public String getStr(int resId) {
        return res.getString(resId);
    }

    /**
     * Toast提示.
     *
     * @param msg text字符串提示
     * @return void
     */
    public void toast(String msg) {
        SysAlertDialog.showAutoHideDialog(mContext, "", msg, Toast.LENGTH_SHORT);
    }

    /**
     * 得到根Fragment
     * <<<< getParentFragment() return null >>>>
     *
     * @return
     */
    public Fragment getRootFragment() {
        Fragment fragment = getParentFragment();
        if (fragment == null) {
            return BaseFragment.this;
        }
        while (fragment.getParentFragment() != null) {
            fragment = fragment.getParentFragment();
        }
        return fragment;
    }
}
