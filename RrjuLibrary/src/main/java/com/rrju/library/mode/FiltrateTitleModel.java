package com.rrju.library.mode;

import java.io.Serializable;

/**
 * Created by Android Studio.
 * User: tanyan
 * Date: 2019-08-28
 * Time: 9:25
 */
public class FiltrateTitleModel implements Serializable {
    private String mId;
    private String mTitleName;
    private String mSelectTextIcon = "—";
    private String mUnSelectTextIcon = "–";

    public FiltrateTitleModel(String mTitleName) {
        this.mTitleName = mTitleName;
    }

    public FiltrateTitleModel() {
    }

    public FiltrateTitleModel(String mTitleName, String mSelectTextIcon, String mUnSelectTextIcon) {
        this.mTitleName = mTitleName;
        this.mSelectTextIcon = mSelectTextIcon;
        this.mUnSelectTextIcon = mUnSelectTextIcon;
    }

    public FiltrateTitleModel(String mId, String mTitleName, String mSelectTextIcon, String mUnSelectTextIcon) {
        this.mId = mId;
        this.mTitleName = mTitleName;
        this.mSelectTextIcon = mSelectTextIcon;
        this.mUnSelectTextIcon = mUnSelectTextIcon;
    }

    public FiltrateTitleModel(String mId, String mTitleName) {
        this.mId = mId;
        this.mTitleName = mTitleName;
    }

    public String getmTitleName() {
        return mTitleName;
    }

    public void setmTitleName(String mTitleName) {
        this.mTitleName = mTitleName;
    }

    public String getmSelectTextIcon() {
        return mSelectTextIcon;
    }

    public void setmSelectTextIcon(String mSelectTextIcon) {
        this.mSelectTextIcon = mSelectTextIcon;
    }

    public String getmUnSelectTextIcon() {
        return mUnSelectTextIcon;
    }

    public void setmUnSelectTextIcon(String mUnSelectTextIcon) {
        this.mUnSelectTextIcon = mUnSelectTextIcon;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    @Override
    public String toString() {
        return "FiltrateTitleModel{" +
                "mTitleName='" + mTitleName + '\'' +
                ", mSelectTextIcon='" + mSelectTextIcon + '\'' +
                ", mUnSelectTextIcon='" + mUnSelectTextIcon + '\'' +
                '}';
    }
}
