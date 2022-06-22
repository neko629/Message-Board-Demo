package com.laorunzi.msgboard.controller;

import com.laorunzi.msgboard.model.ServiceResult;
import com.laorunzi.msgboard.service.MbUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 2 * @Author: Crimson
 * 3 * @Date: 2022/3/5 17:20
 * 4 用户相关功能 controller
 */
@RestController
@RequestMapping("/app/user")
public class MbUserController {

    @Resource
    private MbUserService mbUserService;

    /**
     * 注册用户
     * @param request
     * @param response
     * @param userName 用户名
     * @param email 邮箱
     * @param password 密码
     * @return
     */
    @RequestMapping(value = "/register.json", method = RequestMethod.POST)
    public Object register(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(required = false) String userName,
                           @RequestParam(required = false) String email,
                           @RequestParam(required = false) String password) {
        ServiceResult result = mbUserService.register(request, response, userName, email, password);
        return ServiceResult.transfer2Map(result);
    }

    /**
     * 登录
     * @param request
     * @param response
     * @param userName 用户名或邮箱
     * @param password 密码
     * @param rememberMe 保持登录状态
     * @return
     */
    @RequestMapping(value = "/login.json", method = RequestMethod.POST)
    public Object login(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam(required = false) String userName,
                        @RequestParam(required = false) String password,
                        @RequestParam(defaultValue = "false") boolean rememberMe) {
        ServiceResult result = mbUserService.login(request, response, userName, password,
                rememberMe);
        return ServiceResult.transfer2Map(result);
    }

    /**
     * 用户登出
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/logout.json", method = RequestMethod.POST)
    public Object logout(HttpServletRequest request, HttpServletResponse response) {
        ServiceResult result = mbUserService.logout(request, response);
        return ServiceResult.transfer2Map(result);
    }

    /**
     * 判断用户是否登录, 如果已登录, 返回用户信息
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/checkLogin.json", method = RequestMethod.POST)
    public Object checkLogin(HttpServletRequest request, HttpServletResponse response) {
        ServiceResult result = mbUserService.checkLogin(request, response);
        return ServiceResult.transfer2Map(result);
    }


}
