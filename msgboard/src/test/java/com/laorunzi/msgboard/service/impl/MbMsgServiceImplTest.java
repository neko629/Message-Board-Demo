package com.laorunzi.msgboard.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.laorunzi.msgboard.constants.Constants;
import com.laorunzi.msgboard.mapper.MbMsgMapper;
import com.laorunzi.msgboard.mapper.MbUserMapper;
import com.laorunzi.msgboard.model.MbMsg;
import com.laorunzi.msgboard.model.MbUser;
import com.laorunzi.msgboard.model.ServiceResult;
import com.laorunzi.msgboard.model.ShowMsg;
import com.laorunzi.msgboard.service.MbMsgService;
import com.laorunzi.msgboard.utils.CookieUtils;
import com.laorunzi.msgboard.utils.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collections;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.*;

/**
 * 2 * @Author: Crimson
 * 3 * @Date: 2022/3/6 13:55
 * 4 消息相关 service 单元测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MbMsgServiceImplTest {

    @Resource
    private MbMsgService mbMsgService;

    @Resource
    private MbUserMapper mbUserMapper;

    @Resource
    private MbMsgMapper mbMsgMapper;

    @Test
    public void submitMsg() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Assert.assertFalse(mbMsgService.submitMsg(request, response, -1,
                "未登录").isOk());
        mockCookie(request, response);
        request.setCookies(response.getCookies());
        Assert.assertFalse(mbMsgService.submitMsg(request, response, -1, "12").isOk());
        Assert.assertTrue(mbMsgService.submitMsg(request, response, -1,
                "二十个字以上二十个字以上二十个字以上二十个字以上").isOk());
        Assert.assertFalse(mbMsgService.submitMsg(request, response, -1,
                "试一试两百个字以上吧试一试两百个字以上吧试一试两百个字以上吧试一试两百个字以上吧试一试两百个字以上吧试一试两百个字以上吧试一试两百个字以上吧试一试两百个字以上吧试一试两百个字以上吧试一试两百个字以上吧试一试两百个字以上吧试一试两百个字以上吧试一试两百个字以上吧试一试两百个字以上吧试一试两百个字以上吧试一试两百个字以上吧试一试两百个字以上吧试一试两百个字以上吧试一试两百个字以上吧试一试两百个字以上吧试一试两百个字以上吧试一试两百个字以上吧").isOk());
        Assert.assertTrue(mbMsgService.submitMsg(request, response, -1,
                StringUtils.getRandomString(200)).isOk());
        Assert.assertFalse(mbMsgService.submitMsg(request, response, -5, "试一试").isOk());
    }

    /**
     * 模拟多级留言
     */
    @Test
    public void mockTreeMsg() {
        int loop = 10; // 留言层级
        // 先模拟模拟第一级留言
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        mockCookie(request, response);
        request.setCookies(response.getCookies());
        String content = StringUtils.getRandomString(100);
        mbMsgService.submitMsg(request, response, -1, content);
        List<MbMsg> mbMsgs = mbMsgMapper.selectAllMsgOrderByTime();
        int pid = mbMsgs.get(mbMsgs.size() - 1).getId();
        while (loop-- > 0) {
            try {
                Thread.sleep(200L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mockCookie(request, response);
            request.setCookies(response.getCookies());
            content = StringUtils.getRandomString(100);
            mbMsgService.submitMsg(request, response, pid++, content);
        }
    }

    @Test
    public void getMsgList() {
        ServiceResult result = mbMsgService.getMsgList(new MockHttpServletRequest(),
                new MockHttpServletResponse());
        Assert.assertTrue(result.isOk());
        System.out.println(JSONObject.toJSON(result.getModel()));
    }

//    @Test
//    public void delete() {
//        mbMsgMapper.delete(null);
//    }
    // 模拟一个用户cookie
    private void mockCookie(HttpServletRequest request, HttpServletResponse response) {
        List<MbUser> users = mbUserMapper.selectList(null);
        if (CollectionUtils.isEmpty(users)) {
            return;
        }
        Collections.shuffle(users);
        MbUser mbUser = users.get(0);
        String cookieValue = StringUtils.getRandomString(16);
        Integer maxAge = null;
        CookieUtils.setCookie(request, response, Constants.COOKIE_NAME, cookieValue, maxAge, null);
        Constants.COOKIE_MAP.put(cookieValue, mbUser);
    }
}