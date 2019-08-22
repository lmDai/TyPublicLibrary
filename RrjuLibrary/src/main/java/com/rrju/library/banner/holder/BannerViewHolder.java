package com.rrju.library.banner.holder;

import android.content.Context;
import android.view.View;

/**
 * Created by tanyan on 2018/07/28.
 */

public interface BannerViewHolder<T> {
    /**
     *  创建View
     * @param context
     * @return
     */
    View createView(Context context);

    /**
     * 绑定数据
     * @param context
     * @param position
     * @param data
     */
    void onBind(Context context, int position, T data);
}
