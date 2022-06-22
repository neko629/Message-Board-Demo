package com.laorunzi.msgboard.service;

import com.laorunzi.msgboard.model.ServiceResult;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 2 * @Author: Crimson
 * 3 * @Date: 2022/3/5 14:38
 * 4
 */
public interface MbUserService {
    /**
     * 用户注册
     * @param request
     * @param response
     * @param userName
     * @param email
     * @param password
     * @return
     */
    ServiceResult register(HttpServletRequest request, HttpServletResponse response,
                           String userName, String email, String password);

    /**
     * 用户登录
     * @param request
     * @param response
     * @param userName
     * @param password
     * @param rememberMe
     * @return
     */
    ServiceResult login(HttpServletRequest request, HttpServletResponse response, String userName,
                        String password, boolean rememberMe);

    /**
     * 用户登出
     * @param request
     * @param response
     * @return
     */
    ServiceResult logout(HttpServletRequest request, HttpServletResponse response);

    /**
     * 判断是否登录, 如果登录, 返回用户信息
     * @param request
     * @param response
     * @return
     */
    ServiceResult checkLogin(HttpServletRequest request, HttpServletResponse response);

    /**
     * 校验注册信息
     * @param userName
     * @param email
     * @param password
     * @return
     */
    ServiceResult registerCheck(String userName, String email, String password);
}
