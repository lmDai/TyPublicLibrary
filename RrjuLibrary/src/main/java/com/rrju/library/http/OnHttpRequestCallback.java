package com.rrju.library.http;

/**
 * Created by tanyan on 2018-05-10.
 */

public interface OnHttpRequestCallback  {
    /**
     * 成功
     *
     * @param obj
     * @param statusCode
     */
    void onSuccess(String statusCode, Object obj);

    /**
     *
     * 上传文件进度
     * @param statusCode
     * @param progress
     */
    void onProgress(String statusCode, int progress);

    /**
     * 异常情况
     *
     * @param obj
     * @param statusCode
     */
//    void onError(int statusCode, Object obj);

    /**
     * 失败
     *
     * @param obj
     * @param statusCode
     */
    void onFailure(String statusCode, Object obj);
}
