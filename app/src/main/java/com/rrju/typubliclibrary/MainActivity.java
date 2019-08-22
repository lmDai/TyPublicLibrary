package com.rrju.typubliclibrary;


import android.app.Activity;
import android.os.Bundle;

import com.rrju.library.base.BaseActivity;

public class MainActivity extends BaseActivity {
    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
//        setLeftImageButton();
        setTitle("的撒所打算多发顺丰水电费 的范德萨发fsdffsddfgd防守打法");
        setLeftTextView("返回",R.color.rrj_theme_color);
//        setLeftImageButton();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }
}
