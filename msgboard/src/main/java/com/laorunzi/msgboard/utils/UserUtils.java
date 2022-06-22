package com.laorunzi.msgboard.utils;

import com.laorunzi.msgboard.constants.Constants;
import com.laorunzi.msgboard.model.MbUser;
import jdk.nashorn.internal.ir.RuntimeNode;

import javax.servlet.http.HttpServletRequest;

/**
 * 2 * @Author: Crimson
 * 3 * @Date: 2022/3/5 15:04
 * 4 用户相关工具类
 */
public class UserUtils {

    /**
     * 对密码加盐 md5 加密
     * @param pwd
     * @param salt
     * @return
     */
    public static String getEncodePwd(String pwd, String salt) {
        return MD5Utils.encoderByMd5(pwd + salt);
    }

    /**
     * 校验用户是否登录, 如果登录了返回用户对象
     * @param request
     * @return
     */
    public static MbUser checkLogin(HttpServletRequest request) {
        String cookieValue = CookieUtils.getCookie(request, Constants.COOKIE_NAME);
        return Constants.COOKIE_MAP.get(cookieValue);
    }

}
