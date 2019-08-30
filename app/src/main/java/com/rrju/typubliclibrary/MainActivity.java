package com.rrju.typubliclibrary;


import android.Manifest;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.rrju.library.base.BaseActivity;
import com.rrju.library.permission.PermissionListener;
import com.rrju.library.permission.PermissionsTools;



public class MainActivity extends BaseActivity {
    private TextView mTvWodedianji;

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
//        setLeftImageButton();
        setTitle("的撒所打算多发顺丰水电费", R.color.black);
        setLeftImageButton();
        requestPermissionArray(PERMISSIONS_STORAGE);
        PermissionsTools.requestNotification(this);
        PermissionsTools.requestNotificationListener(this);
    }

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    private void requestPermissionArray(String[] permission) {
        //设置必须允许的权限
        PermissionsTools.necessaryPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        PermissionsTools.setPermissions(this, new PermissionListener() {
            @Override
            public void onSucceed() {
                initData();
            }

            @Override
            public void onCancel() {
                finish();
            }
        }, permission);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initHttp() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
