package com.laorunzi.msgboard.enums;

import lombok.Data;

/**
 * 2 * @Author: Crimson
 * 3 * @Date: 2022/3/5 14:47
 * 4 错误消息
 */
public enum ErrorEnum {
    SUCCESS(200, ""),// 200 表示成功, 文案根据场景变动

    // 用户相关错误, 4 开头
    NEED_LOGIN(403, "请注册或登录后再进行操作"),
    REGISTER_ERROR(412, ""), //  注册失败, 文案根据场景变动
    LOGIN_ERROR(411, "您输入的用户名或密码有误, 请核对后重新登录"),

    // 消息相关错误, 5 开头
    MSG_ERROR(511, "留言内容不可为空, 且长度需在 3-200 之间, 请修改后重新提交"),
    PARENT_NOT_EXIST(512, "回复的对象不存在, 请重试");

    private int code;// 错误码
    private String msg;// 错误消息内容

    ErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
