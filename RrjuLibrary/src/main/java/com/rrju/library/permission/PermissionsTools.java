package com.rrju.library.permission;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.rrju.library.R;
import com.rrju.library.ui.SysAlertDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.List;

/**
 * Created by tanyan on 2018-10-05.
 */

public class PermissionsTools {

    private static PermissionListener permissionListener;
    private static String[] necessaryPermission;

    /**
     * @param permissionsArray 必须的权限数组
     */
    public static void necessaryPermission(String... permissionsArray) {
        necessaryPermission = permissionsArray;
    }

    public static void setPermissions(Context mContext, PermissionListener Listener, String... permissionsArray) {
        permissionListener = Listener;
        if (necessaryPermission == null || necessaryPermission.length < 0) {
            necessaryPermission = permissionsArray;
        }
        AndPermission.with(mContext)
                .runtime()
                .permission(permissionsArray)
                .rationale(new RuntimeRationale())
                .onGranted(permissions -> permissionListener.onSucceed())
                .onDenied(permissions -> showSettingDialog(mContext, permissions))
                .start();
    }

    /**
     * 请求许可通知。
     */
    public static void requestNotification(Context mContext) {
        AndPermission.with(mContext)
                .notification()
                .permission()
                .rationale(new NotifyRationale())
                .onGranted(data -> Log.e("Notification", "允许"))
                .onDenied(data -> Log.e("Notification", "失败"))
                .start();
    }

    /**
     * 请求通知侦听器。
     */
    public static void requestNotificationListener(Context mContext) {
        AndPermission.with(mContext)
                .notification()
                .listener()
                .rationale(new NotifyListenerRationale())
                .onGranted(data -> Log.e("NotificationListener", "允许"))
                .onDenied(data -> Log.e("NotificationListener", "拒绝"))
                .start();
    }

    /**
     * 显示弹窗设置
     *
     * @param permissions
     */
    public static void showSettingDialog(Context mContext, final List<String> permissions) {
        //必须允许的权限是否已经开启
        if (AndPermission.hasPermissions(mContext, necessaryPermission)) {
            permissionListener.onSucceed();
        } else {
            if (AndPermission.hasAlwaysDeniedPermission(mContext, permissions)) {
                List<String> permissionNames = Permission.transformText(mContext, permissions);
                String message = mContext.getString(R.string.message_permission_always_failed,
                        TextUtils.join(",", permissionNames));
                Dialog mDialog = SysAlertDialog.showAlertDialog(mContext,
                        "权限提示", message, "去设置",
                        (dialog, which) -> setPermission(mContext), "取消", (dialog, which) -> permissionListener.onCancel());
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.setCancelable(false);
            } else {
                permissionListener.onCancel();
            }
        }
    }

    /**
     * 进入权限设置界面
     * Set permissions.
     */
    private static void setPermission(Context mContext) {
        AndPermission.with(mContext).runtime().setting().start(1);
//        AndPermission.with(mContext)
//                .runtime()
//                .setting()
//                .onComeback(new Setting.Action() {
//                    @Override
//                    public void onAction() {
//                        //从权限设置界面返回
//                        showSettingDialog(mContext, permissions);
//                    }
//                })
//                .start();
    }
}
