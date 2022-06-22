package com.laorunzi.msgboard.service.impl;

import com.laorunzi.msgboard.constants.Constants;
import com.laorunzi.msgboard.enums.ErrorEnum;
import com.laorunzi.msgboard.mapper.MbUserMapper;
import com.laorunzi.msgboard.model.MbUser;
import com.laorunzi.msgboard.model.ServiceResult;
import com.laorunzi.msgboard.service.MbUserService;
import com.laorunzi.msgboard.utils.CookieUtils;
import com.laorunzi.msgboard.utils.DateUtils;
import com.laorunzi.msgboard.utils.StringUtils;
import com.laorunzi.msgboard.utils.UserUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 2 * @Author: Crimson
 * 3 * @Date: 2022/3/5 14:38
 * 4 用户相关操作
 */
@Service
public class MbUserServiceImpl implements MbUserService {

    @Resource
    private MbUserMapper mbUserMapper;

    @Override
    public ServiceResult register(HttpServletRequest request, HttpServletResponse response,
                            String userName, String email, String password) {
        // 校验输入内容
        ServiceResult check = registerCheck(userName, email, password);
        if (!check.isOk()) {
            return check;
        }
        // 执行入库
        Date now = new Date();
        MbUser mbUser = new MbUser();
        mbUser.setUserName(userName);
        mbUser.setEmail(email);
        String salt = StringUtils.getRandomString(8);
        mbUser.setSalt(salt);
        mbUser.setPassword(UserUtils.getEncodePwd(password, salt));
        mbUser.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(now));
        int result = mbUserMapper.insertUser(mbUser);
        if (result != 1) { // 入库失败
            return new ServiceResult(false, ErrorEnum.REGISTER_ERROR.getCode(), "注册失败, 请稍后重试");
        }
        // 注册成功, 自动登录
        // 设置 cookie
        String cookieValue = StringUtils.getRandomString(16);
        Integer maxAge = null;
        CookieUtils.setCookie(request, response, Constants.COOKIE_NAME, cookieValue, maxAge, null);
        Constants.COOKIE_MAP.put(cookieValue, mbUser);
        // 返回前端前脱敏
        mbUser.setPassword(null);
        mbUser.setSalt(null);
        return new ServiceResult(true, ErrorEnum.SUCCESS.getCode(), "", mbUser);
    }

    @Override
    public ServiceResult login(HttpServletRequest request, HttpServletResponse response,
                           String userName, String password, boolean rememberMe) {
        // 判空
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            ErrorEnum err = ErrorEnum.LOGIN_ERROR;
            return new ServiceResult(false, err.getCode(), err.getMsg());
        }
        MbUser mbUser = null;
        // 判断是邮箱登录还是用户名登录
        if (userName.contains("@")) {
            mbUser = mbUserMapper.selectByEmail(userName);
        } else {
            mbUser = mbUserMapper.selectByUserName(userName);
        }
        if (mbUser == null) {
            // 没有找对用户记录, 返回提示
            ErrorEnum err = ErrorEnum.LOGIN_ERROR;
            return new ServiceResult(false, err.getCode(), err.getMsg());
        }
        // 核对密码
        if (mbUser.getPassword().equals(UserUtils.getEncodePwd(password, mbUser.getSalt()))) {
            // 登录成功, 设置 cookie
            String cookieValue = StringUtils.getRandomString(16);
            Integer maxAge = null;
            if (rememberMe) {
                // 勾选了记录登录状态, 设置超时时间为一个月后
                Date now = new Date();
                Date later = DateUtils.getDayAfterMonth(now, 1);
                maxAge = (int) DateUtils.getSecendsBetweenTwoDate(now, later, true);
            }
            CookieUtils.setCookie(request, response, Constants.COOKIE_NAME, cookieValue, maxAge,
                    null);
            Constants.COOKIE_MAP.put(cookieValue, mbUser);
            // 返回前端前脱敏
            mbUser.setPassword(null);
            mbUser.setSalt(null);
            return new ServiceResult(true, ErrorEnum.SUCCESS.getCode(), "", mbUser);
        } else {
            // 密码错误
            ErrorEnum err = ErrorEnum.LOGIN_ERROR;
            return new ServiceResult(false, err.getCode(), err.getMsg());
        }

    }

    @Override
    public ServiceResult logout(HttpServletRequest request, HttpServletResponse response) {
        // 获取 cookie
        String value = CookieUtils.getCookie(request, Constants.COOKIE_NAME);
        if (StringUtils.isNotBlank(value)) {
            // 删除 cookie
            CookieUtils.setCookie(request, response, Constants.COOKIE_NAME, "", 0, null);
            // 删除映射
            Constants.COOKIE_MAP.remove(value);
        }
        return new ServiceResult(true, ErrorEnum.SUCCESS.getCode(), "");
    }

    @Override
    public ServiceResult checkLogin(HttpServletRequest request, HttpServletResponse response) {
        MbUser mbUser = UserUtils.checkLogin(request);
        if (mbUser == null) {
            return new ServiceResult(false, ErrorEnum.NEED_LOGIN.getCode(),
                    ErrorEnum.NEED_LOGIN.getMsg());
        }
        // 脱敏
        mbUser.setSalt(null);
        mbUser.setPassword(null);
        return new ServiceResult(true, ErrorEnum.SUCCESS.getCode(), "" , mbUser);
    }

    @Override
    public ServiceResult registerCheck(String userName, String email, String password) {
        ErrorEnum err = ErrorEnum.REGISTER_ERROR;
        // 用户名格式, 不可为空，只能使用字母和数字，长度在5~20之间
        String userNameRegex = "^[0-9A-Za-z]{5,20}$";
        if (StringUtils.isBlank(userName) || !userName.matches(userNameRegex)) {
            return new ServiceResult(false, err.getCode(), "用户名不可为空，只能使用字母和数字，长度在5~20之间");
        }
        // 邮箱格式校验
        String emailRegex = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\" +
                ".[a-zA-Z]+\\s*$";
        if (StringUtils.isBlank(email) || !email.matches(emailRegex)) {
            return new ServiceResult(false, err.getCode(), "邮箱格式错误, 请修改后提交");
        }
        // 密码格式, 不可为空，长度在8~20之间，至少包含一个大写、一个小写、一个数字、一个特殊符号
        String pwdRegex = "^(?![A-Za-z0-9]+$)(?![a-z0-9\\W]+$)(?![A-Za-z\\W]+$)(?![A-Z0-9\\W]+$)" +
                "[a-zA-Z0-9\\W]{8,20}$";
        if (StringUtils.isBlank(password) || !password.matches(pwdRegex)) {
            return new ServiceResult(false, err.getCode(), "密码不可为空，长度在8~20之间，至少包含一个大写、一个小写、一个数字、一个特殊符号");
        }
        MbUser mbUser = null;
        // 用户名是否已注册
        mbUser = mbUserMapper.selectByUserName(userName);
        if (mbUser != null) {
            return new ServiceResult(false, err.getCode(), "用户名已注册, 请更换用户名或直接登录");
        }
        // 邮箱是否已注册
        mbUser = mbUserMapper.selectByEmail(email);
        if (mbUser != null) {
            return new ServiceResult(false, err.getCode(), "该邮箱已注册, 请更换邮箱或直接登录");
        }
        return new ServiceResult(true, ErrorEnum.SUCCESS.getCode(), "");
    }
}

