package com.rrju.library.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by tanyan on 2018-05-10.
 * 应用配置信息类
 */
public class AppConfig {
    private static final String DEFAULT_PREFERENCES_NAME = "appconfig";
    private static AppConfig instance;
    private Context m_appContext;
    /**
     * 是否存在崩溃
     */
    private boolean m_bCrashHappen;
    /**
     * 是否第一次安装进入
     */
    private boolean m_bIsFirstOpenWelcome;
    /**
     * 是否打开键盘
     */
    private boolean m_bKey;
    /**
     * 用户ID
     */
    private String m_userID;
    /**
     * token
     */
    private String token;

    /**
     * 用户名
     */
    private String m_userName;
    /**
     * 用户昵称
     */
    private String m_nickName;
    /**
     * 登录账号
     */
    private String m_LoginNumber;
    /**
     * 是否存在版本更新
     */
    private boolean m_isHaveVersionUpdate;
    /**
     * 用户登录的webtoken
     */
    private String m_webToken;
    /**
     * 用户头像
     */
    private String mUserHeadUrl;
    /**
     * 电话号码
     */
    private String phone;
    /**
     * 关注数量
     */
    private String mAttentionNum;
    /**
     * 图片服务器公共前缀
     */
    private String mPublicImageURL;
    /**
     * 图片服务器私有地方前缀
     */
    private String mPrivateImageURL;
    /**
     * 网络请求地址前缀
     */
    private String mHttpURL;

    /**
     * 全景图片，视频前缀
     */
    private String mBucketDomain;
    /**
     * 全局筛选条件
     */
    private String mAllFiltrate;
    /**
     * 用户登录类型 1、表示用户注册登录 ；2、用户快捷登录
     */
    private String mLoginType;
    /**
     * 通过服务器获取的融云Token
     */
    private String m_RongToken = "";
    /**
     * 通过服务器获取的融云Token
     */
    private String m_ImId = "";
    /**
     * 判断是否是从会话详情进入
     */
    private boolean m_IsDeatilsInputMessage = false;

    //19 - 01 - 18
    /**
     * 补丁名称
     */
    private String mPatchName;
    /**
     * IM登录状态【0】登录成功【1】正在登录【2】登录失败
     */
    private int IMLoginState = -1;
    /**
     * 设备标识保存在本地
     */
    private String mDeviceId = "";

    /**
     * 获取单件实例
     *
     * @return
     */
    public static AppConfig getInstance() {
        if (null == instance)
            instance = new AppConfig();
        return instance;
    }

    /**
     * 序列化对象
     *
     * @param k
     * @return String
     */
    public static <K> String serialize(K k) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    byteArrayOutputStream);
            objectOutputStream.writeObject(k);
            String serStr = byteArrayOutputStream.toString("ISO-8859-1");
            serStr = URLEncoder.encode(serStr, "UTF-8");
            objectOutputStream.close();
            byteArrayOutputStream.close();
            return serStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 反序列化对象
     *
     * @param str
     * @return
     * @throws IOException
     */
    public static <K> K deSerialization(String str) {
        String redStr = null;
        try {
            redStr = URLDecoder.decode(str, "UTF-8");
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(redStr.getBytes("ISO-8859-1"));
            ObjectInputStream objectInputStream = new ObjectInputStream(
                    byteArrayInputStream);
            K person = (K) objectInputStream.readObject();
            objectInputStream.close();
            byteArrayInputStream.close();
            return person;
        } catch (Exception e) {
            return null;
        }
    }

    public void init(Context c) {
        m_appContext = c.getApplicationContext();
        SharedPrefData.init(m_appContext, DEFAULT_PREFERENCES_NAME);
        m_bCrashHappen = SharedPrefData.getBoolean("crashHappen", false);
        m_bIsFirstOpenWelcome = SharedPrefData.getBoolean("isFirstOpenWelcome", true);
        m_userID = SharedPrefData.getString("UserID", "");//默认用户为空
        token = SharedPrefData.getString("Token", "");//   用户token
        m_userName = SharedPrefData.getString("userName", "");
        m_nickName = SharedPrefData.getString("NickName", "");
        // m_proId = prefDefault.getInt("proId", -1);
        m_isHaveVersionUpdate = SharedPrefData.getBoolean("isHaveVersionUpdate", false);
        m_LoginNumber = SharedPrefData.getString("loginNumber", "");
        m_webToken = SharedPrefData.getString("m_webToken", "");
        mAllFiltrate = SharedPrefData.getString("AllFiltrate", "");
        mUserHeadUrl = SharedPrefData.getString("UserHeadUrl", "");
        phone = SharedPrefData.getString("phone", "");
        mAttentionNum = SharedPrefData.getString("AttentionNum", "");
        mLoginType = SharedPrefData.getString("LoginType", "");
        mPublicImageURL = SharedPrefData.getString("PublicImageURL", "https://image.rrju.com");//    http://192.168.0.75:8888
        mPrivateImageURL = SharedPrefData.getString("PrivateImageURL", "https://image.rrju.com");//    http://192.168.0.75:8888
        mHttpURL = SharedPrefData.getString("HttpURL", "https://image.rrju.com");//    http://192.168.0.75:8888
        mBucketDomain = SharedPrefData.getString("BucketDomain", "https://daojiale.oss-cn-hangzhou.aliyuncs.com");
        m_RongToken = SharedPrefData.getString("RongToken", "");
        m_ImId = SharedPrefData.getString("ImId", "");
        m_IsDeatilsInputMessage = SharedPrefData.getBoolean("DeatilsInputMessage", false);
        mPatchName = SharedPrefData.getString("PatchName", "patch_signed_7zip.patch");
        IMLoginState = SharedPrefData.getInt("IMLoginState", -1);
        mDeviceId = SharedPrefData.getString("mDeviceId", "");
    }

    /**
     * 获取补丁名称
     *
     * @return
     */
    public String getmPatchName() {
        return mPatchName;
    }

    /**
     * 设置补丁名称
     *
     * @param mPatchName
     */
    public void setmPatchName(String mPatchName) {
        this.mPatchName = mPatchName;
        SharedPrefData.putString("PatchName", mPatchName);
    }

    /**
     * 获取设备标识
     *
     * @return
     */
    public String getmDeviceId() {
        return mDeviceId;
    }

    /**
     * 设置设备标识
     *
     * @param mDeviceId
     */
    public void setmDeviceId(String mDeviceId) {
        this.mDeviceId = mDeviceId;
        SharedPrefData.putString("mDeviceId", mDeviceId);
    }

    /**
     * 获取IM登录状态【0】登录成功【1】正在登录【2】登录失败
     *
     * @return
     */
    public int getIMLoginState() {
        return IMLoginState;
    }

    /**
     * 【0】登录成功【1】正在登录【2】登录失败
     *
     * @param IMLoginState
     */
    public void setIMLoginState(int IMLoginState) {
        this.IMLoginState = IMLoginState;
        SharedPrefData.putInt("IMLoginState", IMLoginState);
    }

    /**
     * 获取即时通讯 ID
     *
     * @return
     */
    public String getmImId() {
        return m_ImId;
    }

    /**
     * 设置用户即时通讯ID
     *
     * @param m_ImId
     */
    public void setmImId(String m_ImId) {
        this.m_ImId = m_ImId;
        SharedPrefData.putString("ImId", m_ImId);
    }

    /**
     * 获取是否是从会话详情及进入
     *
     * @return
     */
    public boolean getmDeatilsInputMessage() {
        return m_IsDeatilsInputMessage;
    }

    /**
     * 设置是否是从会话详情进入
     *
     * @param m_IsDeatilsInputMessage
     */
    public void setmDeatilsInputMessage(boolean m_IsDeatilsInputMessage) {
        this.m_IsDeatilsInputMessage = m_IsDeatilsInputMessage;
        SharedPrefData.putBoolean("DeatilsInputMessage", m_IsDeatilsInputMessage);
    }

    /**
     * 获取用户登录类型 1注册用户登录，2快捷登录用户
     *
     * @return
     */
    public String getUserLoginType() {
        return mLoginType;
    }

    /**
     * 设置用户登录类型 1注册用户登录，2快捷登录用户
     *
     * @param mLoginType
     */
    public void setUserLoginType(String mLoginType) {
        this.mLoginType = mLoginType;
        SharedPrefData.putString("LoginType", mLoginType);
    }

    /**
     * 获取是否第一次安装进入
     *
     * @return
     */
    public boolean getIsFirstOpenWelcome() {
        return m_bIsFirstOpenWelcome;
    }

    /**
     * 设置是否第一次安装进入
     *
     * @param isFirstOpen
     */
    public void setIsFirstOpenWelcome(boolean isFirstOpen) {
        m_bIsFirstOpenWelcome = isFirstOpen;
        SharedPrefData.putBoolean("isFirstOpenWelcome", isFirstOpen);
    }

    /**
     * 获取是否存在崩溃
     *
     * @return
     */
    public boolean getCrashHappen() {
        return m_bCrashHappen;
    }

    /**
     * 设置存在崩溃
     *
     * @param bHappen
     */
    public void setCrashHappen(boolean bHappen) {
        m_bCrashHappen = bHappen;
        SharedPrefData.putBoolean("crashHappen", bHappen);
    }

    public boolean getKeyState() {
        return m_bKey;
    }

    public void setKeyState(boolean isKey) {
        m_bKey = isKey;
    }

    /**
     * 选择城市id
     */
    public void setCityId(String id) {
        SharedPrefData.putString("CityId", id);
    }

    /**
     * 选择城市id
     */
    public String getCityId() {
        String cityId = SharedPrefData.getString("CityId", "500000");
        return cityId;
    }


    /**
     * 选择城市名称
     */
    public void setCityName(String id) {
        SharedPrefData.putString("CityName", id);
    }

    /**
     * 选择城市名称
     */
    public String getCityName(Context context) {
        String cityId = SharedPrefData.getString("CityName", "重庆");
        return cityId;
    }

//    /**
//     * 获取城市地址
//     *
//     * @return
//     */
//    public String getCityDomainNewest() {
//        CityInfoModel infoModel = ToolUtils.readCityInfo(m_appContext);
//        if (infoModel == null) {
//            infoModel = new CityInfoModel();
//            infoModel.setDomain("/cq");
//        }
//        return infoModel.getDomain();
//    }


    public String getUserHeadUrl() {
        return mUserHeadUrl;
    }

    public void setUserHeadUrl(String UserHeadUrl) {
        this.mUserHeadUrl = UserHeadUrl;
        SharedPrefData.putString("UserHeadUrl", mUserHeadUrl);
    }

    /**
     * 获取图片服务器前缀
     */
    public String getImageURL() {
        return mPublicImageURL;
    }

    /**
     * 保存图片服务器前缀
     */
    public void setImageURL(String string) {
        this.mPublicImageURL = string;
        SharedPrefData.putString("PublicImageURL", string);
    }

    /**
     *   获取图片服务器私有地方前缀
     * @return
     */
    public String getmPrivateImageURL() {
        return mPrivateImageURL;
    }

    /**
     * 保存图片服务器私有地方前缀
     */
    public void setmPrivateImageURL(String mPrivateImageURL) {
        this.mPrivateImageURL = mPrivateImageURL;
        SharedPrefData.putString("PrivateImageURL", mPrivateImageURL);

    }

    /**
     * 获取请求地址
     * @return
     */
    public String getmHttpURL() {
        return mHttpURL;
    }

    /**
     * 设置请求地址
     * @param mHttpURL
     */
    public void setmHttpURL(String mHttpURL) {
        this.mHttpURL = mHttpURL;
        SharedPrefData.putString("HttpURL", mHttpURL);

    }

    /**
     * 保存全景，视频前缀
     *
     * @param bucketDomain
     */
    public void setmBucketDomain(String bucketDomain) {
        this.mBucketDomain = bucketDomain;
        SharedPrefData.putString("BucketDomain", bucketDomain);
    }

    /**
     * 获取全景，视频前缀
     *
     * @return
     */
    public String getmBucketDomain() {
        return mBucketDomain;
    }


    /**
     * 获取户关注数量
     */
    public String getAttentionNum() {
        return mAttentionNum;
    }

    /**
     * 保存用户关注数量
     */
    public void setAttentionNum(String string) {
        this.mAttentionNum = string;
        SharedPrefData.putString("AttentionNum", string);
    }

    /**
     * 获取登录的电话号码
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 保存登录的电话号码
     */
    public void setPhone(String phone) {
        this.phone = phone;
        SharedPrefData.putString("phone", phone);
    }

    /**
     * 根据获取用户ID
     */
    public String getUserId() {
        return m_userID;
    }

    /**
     * 根据用户ID
     *
     * @param userId 用户id号
     */
    public void setUserId(String userId) {
        this.m_userID = userId;
        SharedPrefData.putString("UserID", userId);
    }

    /**
     * 根据获取用户token
     */
    public String getToken() {
        return token;
    }

    /**
     * 用户token
     *
     * @param token
     */
    public void setToken(String token) {
        this.token = token;
        SharedPrefData.putString("Token", token);
    }

    /**
     * 根据获取用户名
     */
    public String getUserName() {
        return m_userName;
    }

    /**
     * 根据获取用户昵称
     */
    public String getNickName() {
        return m_nickName;
    }

    /**
     * 用户昵称
     *
     * @param nickName
     */
    public void setNickName(String nickName) {
        this.m_nickName = nickName;
        SharedPrefData.putString("NickName", m_nickName);
    }

    /**
     * 是否有版本更新
     *
     * @return
     */
    public boolean isHaveVersionUpdate() {
        return m_isHaveVersionUpdate;
    }

    /**
     * 是否有版本更新
     *
     * @param isHaveVersionUpdate
     */
    public void setIsHaveVersionUpdate(boolean isHaveVersionUpdate) {
        this.m_isHaveVersionUpdate = isHaveVersionUpdate;
        SharedPrefData.putBoolean("isHaveVersionUpdate", m_isHaveVersionUpdate);
    }

    /**
     * 获取登录账号
     *
     * @return
     */
    public String getLoginNumber() {
        return m_LoginNumber;
    }

    /**
     * 设置储存登录账号
     *
     * @param loginNumber
     */
    public void setLoginNumber(String loginNumber) {
        this.m_LoginNumber = loginNumber;
        SharedPrefData.putString("loginNumber", loginNumber);
    }

    /**
     * 获取登录后返回的webtoken
     *
     * @return
     */
    public String getM_webToken() {
        return m_webToken;
    }

    /**
     * 设置登录后返回的webtoken
     *
     * @param m_webToken
     */
    public void setWebToken(String m_webToken) {
        this.m_webToken = m_webToken;
        SharedPrefData.putString("m_webToken", m_webToken);
    }

    /**
     * 设置全局筛选条件
     *
     * @param mFiltrate
     */
    public void setAllFiltrate(String mFiltrate) {
        this.mAllFiltrate = mFiltrate;
        SharedPrefData.putString("AllFiltrate", mFiltrate);
    }

    public String getmAllFiltrate() {
        return mAllFiltrate;
    }

    /**
     * 获取通过服务器获取的融云Token
     *
     * @return
     */
    public String getRongToken() {
        return m_RongToken;
    }

    /**
     * 设置通过服务器获取的融云Token
     *
     * @param rongToken
     */
    public void setRongToken(String rongToken) {
        this.m_RongToken = rongToken;
        SharedPrefData.putString("RongToken", rongToken);
    }
}
