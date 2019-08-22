package com.rrju.library.http;

/**
 * Created by tanyan on 2018-05-10.
 * 任务消息数据加载器Listener
 */

public interface OnListInfoItemLoadListener {
    /**
     * 响应获取单条Item
     *
     * @param item
     */
    void onGetItem(Object item);

    /**
     * 响应获取某页开始
     */
    void onGetPageStart();

    /**
     * 响应获取某页结束
     */
    void onGetPageFinish(boolean bFirstPage);

    /**
     * 响应获取失败
     */
    void onError(boolean bFirstPage, String statusCode, String failInfo);
}
