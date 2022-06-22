package com.laorunzi.msgboard.utils;

import sun.misc.BASE64Encoder;

import java.security.MessageDigest;

/**
 * 2 * @Author: Crimson
 * 3 * @Date: 2022/3/5 15:04
 * 4 md5 工具列
 */
public class MD5Utils {

    /**
     * 对字符串进行 md5 加密
     * @param str
     * @return
     */
    public static String encoderByMd5(String str) {
        String result = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder base64en = new BASE64Encoder();
            result = base64en.encode(md5.digest(str.getBytes("utf-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
