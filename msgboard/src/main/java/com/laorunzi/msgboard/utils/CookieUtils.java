package com.laorunzi.msgboard.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 2 * @Author: Crimson
 * 3 * @Date: 2022/3/5 15:41
 * 4 cookie 操作工具类
 */
public class CookieUtils {

    /**
     * 设置 cookie
     * @param response
     * @param key
     * @param value
     * @param maxAge 持久化时间, 单位秒, 如果不设置, 关闭浏览器后会失效, 设置为 0, 表示删除 cookie
     * @param path
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response,
                                 String key,
                                 String value,
                                 Integer maxAge, String path) {
        ResponseCookie.ResponseCookieBuilder cookieBuilder =
                ResponseCookie.from(key, value).httpOnly(false).secure(true).sameSite("None");
        if (StringUtils.isNotBlank(path)) {
            cookieBuilder.path(path);
        } else {
            cookieBuilder.path("/");
        }
        if (maxAge != null) {
            cookieBuilder.maxAge(maxAge);
        }
        ResponseCookie cookie = cookieBuilder.build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
//        Cookie cookie = new Cookie(key, value);
//        if (StringUtils.isNotBlank(path)) {
//            // 设置 path
//            cookie.setPath(path);
//        }
//        if (maxAge != null) {
//            // 设置过期时间
//            cookie.setMaxAge(maxAge);
//        }
//        response.addCookie(cookie);
    }

    /**
     * 获取 cookie
     * @param request
     * @param key
     * @return
     */
    public static String getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
