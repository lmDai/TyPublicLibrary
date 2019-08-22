package com.rrju.library.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 32位加密
 */
public class MD5 {
    /**
     * 自定规则的MD5加码。32位
     *
     * @param inStr
     * @return
     */
    public static String getMD5FY(String inStr) {
        String str = MD5.getMD5(inStr);
        str = str.substring(10, str.length()) + str.substring(0, 10);
        str = MD5.getMD5(str);
        return str;
    }

    /**
     * MD5加码。32位
     *网络请求参数加密
     * @param content
     * @return
     */
    public static String getMD5(String content) {
        String s = makeMD5(content);
        String s1 = null;
        if (s != null) {
            s1 = s.substring(0, 16);
        }
        String s2 = null;
        if (s != null) {
            s2 = s.substring(16, 32);
        }
        s1 = makeMD5(s1);
        s2 = makeMD5(s2);
        s = s1 + s2;
        for (int i = 0; i < 100; i++) {
            s = makeMD5(s);
        }
        return s;
    }

    public static String makeMD5(String content) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(content.getBytes());
            return getHashString(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getHashString(MessageDigest messageDigest) {
        StringBuilder builder = new StringBuilder();
        for (byte b : messageDigest.digest()) {
            builder.append(Integer.toHexString(  ( b >> 4 ) & 0xf )  );
            builder.append(Integer.toHexString(  b & 0xf )           );
        }
        return builder.toString();
    }
    /**
     *
     * @Title: MD5
     * @Description: 普通MD5
     * @author Sunt.
     * @time 2018年4月18日 上午11:53:07
     * @return String    返回类型
     * @throws
     */
    public static String getRRJMD5(String input) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return "check jdk";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        char[] charArray = input.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }

        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();

        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }

        return hexValue.toString();
    }
}
