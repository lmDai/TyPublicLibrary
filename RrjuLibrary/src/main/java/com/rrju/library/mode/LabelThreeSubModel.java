package com.rrju.library.mode;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Android Studio.
 * User: tanyan
 * Date: 2019-08-28
 * Time: 9:46
 */
public class LabelThreeSubModel implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6781090537495910052L;

    /**
     * 是否选择
     */
    private boolean isSelect;
    /**
     * 筛选条件传输名称
     */
    @SerializedName(value = "displayNameCondition")
    private String displayNameCondition;
    /**
     * 显示名称
     */
    @SerializedName(value = "displayName")
    private String displayName;

    public LabelThreeSubModel() {
    }

    public LabelThreeSubModel(boolean isSelect, String displayNameCondition, String displayName) {
        this.isSelect = isSelect;
        this.displayNameCondition = displayNameCondition;
        this.displayName = displayName;
    }

    public LabelThreeSubModel(String displayNameCondition, String displayName) {
        this.displayNameCondition = displayNameCondition;
        this.displayName = displayName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getDisplayNameCondition() {
        return displayNameCondition;
    }

    public void setDisplayNameCondition(String displayNameCondition) {
        this.displayNameCondition = displayNameCondition;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "LabelThreeSubModel{" +
                "isSelect=" + isSelect +
                ", displayNameCondition='" + displayNameCondition + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}