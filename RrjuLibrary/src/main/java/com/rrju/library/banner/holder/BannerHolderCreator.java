package com.rrju.library.banner.holder;

/**
 * Created by tanyan on 2018/07/28.
 */

public interface BannerHolderCreator<VH extends BannerViewHolder> {
    /**
     * 创建ViewHolder
     * @return
     */
    public VH createViewHolder();
}
