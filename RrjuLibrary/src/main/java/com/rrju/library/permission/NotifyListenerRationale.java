/*
 * Copyright 2018 Zhenjie Yan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rrju.library.permission;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.rrju.library.R;
import com.rrju.library.ui.SysAlertDialog;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

/**
 * Created by Zhenjie Yan on 2018/5/30.
 */
public class NotifyListenerRationale implements Rationale<Void> {

    @Override
    public void showRationale(Context context, Void data, final RequestExecutor executor) {
        SysAlertDialog.showAlertDialog(context, "权限申请", "您的设备不允许我们访问通知。",
                "允许", (dialog, which) -> executor.execute(),
                "取消", (dialog, which) -> executor.cancel());
    }
}