package com.rrju.library.http;

import org.json.JSONObject;

/**
 * 网络接口情况返回值
 * Created by zhenchen on 2017/2/21.
 */

public interface HttpResponseHandler {
    /**
     * 成功
     *
     * @param response
     */
    void onSuccess(JSONObject response);

    /**
     * 上传文件进度
     *
     * @param progress
     */
    void onProgress(int progress);

    /**
     * 异常情况
     *
     * @param state [0]网络异常
     *              [1]请求成功返回值异常
     *              [2]请求成功转换成JSONObject对象异常
     *              [3]请求参数异常
     * @param error
     */
    void onError(int state, String error);
}
