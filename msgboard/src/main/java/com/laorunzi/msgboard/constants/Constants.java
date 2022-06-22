package com.laorunzi.msgboard.constants;

import com.laorunzi.msgboard.model.MbUser;

import java.util.HashMap;
import java.util.Map;

/**
 * 2 * @Author: Crimson
 * 3 * @Date: 2022/3/5 17:53
 * 4 存放一些常量和全局变量
 */
public class Constants {

    /*
     记录已登录用户的 cookie
     */
    public static Map<String, MbUser> COOKIE_MAP = new HashMap<>();

    /*
    用户登录后产生的 cookie 的 key
     */
    public static String COOKIE_NAME = "mb_key";
}
