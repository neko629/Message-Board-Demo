package com.laorunzi.msgboard.service.impl;

import com.laorunzi.msgboard.service.MbUserService;
import com.laorunzi.msgboard.utils.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * 2 * @Author: Crimson
 * 3 * @Date: 2022/3/5 15:33
 * 4 用户相关 service 方法单元测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MbUserServiceImplTest {

    @Resource
    private MbUserService mbUserService;

    @Test
    public void register() {
        String userName = StringUtils.getRandomString(6);
        Assert.assertTrue(mbUserService.register(new MockHttpServletRequest(),
                new MockHttpServletResponse(), userName, userName + "@qq.com", "2sdfP++99").isOk());
        Assert.assertFalse(mbUserService.register(new MockHttpServletRequest(),
                new MockHttpServletResponse(), userName, userName + "@qq.com", "2sdfP++99").isOk());
//        Assert.isTrue(mbUserService.register(new MockHttpServletRequest(),
//                new MockHttpServletResponse(), "zhangsan",  "zhangsan@qq.com", "123aA+++").isOk());
    }

    @Test
    public void login() {
        Assert.assertTrue(mbUserService.login(new MockHttpServletRequest(),
                new MockHttpServletResponse(), "zhangsan", "123aA+++", false).isOk());
        Assert.assertTrue(mbUserService.login(new MockHttpServletRequest(),
                new MockHttpServletResponse(), "zhangsan@qq.com", "123aA+++", false).isOk());
        Assert.assertFalse(mbUserService.login(new MockHttpServletRequest(),
                new MockHttpServletResponse(), "zhangsan", "123aA+++11", false).isOk());
    }

    @Test
    public void logout() {
        Assert.assertTrue(mbUserService.logout(new MockHttpServletRequest(),
                new MockHttpServletResponse()).isOk());
    }





    @Test
    public void registerCheck() {
        Assert.assertFalse(mbUserService.registerCheck("abb1222", "1213123", "wrfsadf").isOk());
        Assert.assertFalse(mbUserService.registerCheck("abb@111", "1213123@qq.com", "1wwssWW+11").isOk());
        Assert.assertFalse(mbUserService.registerCheck("abb11", "1213123@qq.com", "1wW+").isOk());
        Assert.assertTrue(mbUserService.registerCheck("abb11f", "1213123@qq.com", "1wW+33ddaaa").isOk());
    }
}