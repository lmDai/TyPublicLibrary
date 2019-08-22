package com.rrju.library.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import android.support.annotation.Nullable;

import com.rrju.library.ui.SysAlertDialog;


/**
 * *Fragment预加载问题的解决方案：
 * 1.可以懒加载的Fragment
 * 2.切换到其他页面时停止加载数据（可选）
 * Created by tanyan on 2018-07-17.
 */

public abstract class LazyLoadFragment extends Fragment {
    /**
     * 视图是否已经初初始化
     */
    protected boolean isInit = false;
    protected boolean isLoad = false;
    protected final String TAG = "LazyLoadFragment";
    /**
     * 友盟统计当前页面统计（当前页面名称），用于页面访问路径统计
     */
    public String mPageName = "LazyLoadFragment";
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
        // 获取通用资源
        res = getResources();
        // 获取FragmentManager 对象
        m_fragmentManager = getActivity().getSupportFragmentManager();
    }

    public void setFragmentContentView(int layout) {
        view = LayoutInflater.from(context).inflate(layout, null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // view = inflater.inflate(setContentView(), container, false);
        isInit = true;
        /**初始化的时候去加载数据**/
        isCanLoadData();
        return view;
    }

    /**
     * 视图是否已经对用户可见，系统的方法
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isCanLoadData();
    }

    /**
     * 是否可以加载数据
     * 可以加载数据的条件：
     * 1.视图已经初始化
     * 2.视图对用户可见
     */
    private void isCanLoadData() {
        if (!isInit) {
            return;
        }

        if (getUserVisibleHint()) {
            lazyLoad();
            isLoad = true;
        } else {
            if (isLoad) {
                stopLoad();
            }
        }
    }

    /**
     * 视图销毁的时候讲Fragment是否初始化的状态变为false
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInit = false;
        isLoad = false;

    }

    protected void showToast(String message) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 找出对应的控件
     *
     * @param id
     * @return
     */
    public View findViewById(int id) {
        return view.findViewById(id);
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
     * 当视图初始化并且对用户可见的时候去真正的加载数据
     */
    protected abstract void lazyLoad();

    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以覆写此方法
     */
    protected void stopLoad() {
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
            return LazyLoadFragment.this;
        }
        while (fragment.getParentFragment() != null) {
            fragment = fragment.getParentFragment();
        }
        return fragment;
    }
}