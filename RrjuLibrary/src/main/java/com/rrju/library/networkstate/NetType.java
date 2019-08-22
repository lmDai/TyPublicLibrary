package com.rrju.library.networkstate;

/**
 * 网络状态实体
 * Created by tanyan on 2018-07-23.
 */

public class NetType {
    private String wifi = "WIFI";
    private String cmnet = "CMNET";
    private String cmwap = "CMWAP";
    private String none = "NONE";

    public String getWifi() {
        return wifi;
    }

    public void setWifi(String wifi) {
        this.wifi = wifi;
    }

    public String getCmnet() {
        return cmnet;
    }

    public void setCmnet(String cmnet) {
        this.cmnet = cmnet;
    }

    public String getCmwap() {
        return cmwap;
    }

    public void setCmwap(String cmwap) {
        this.cmwap = cmwap;
    }

    public String getNone() {
        return none;
    }

    public void setNone(String none) {
        this.none = none;
    }
}
