package com.laorunzi.msgboard.model;

import lombok.Data;

import java.util.Date;

/**
 * 2 * @Author: Crimson
 * 3 * @Date: 2022/3/5 13:07
 * 用户类, 对应 mb_user 表
 */
@Data
public class MbUser {
    private int id;// 用户 id, 自增
    private String password; // 密码
    private String salt; // 密码加密串
    private String email; // 邮箱
    private String userName; // 用户名
    private String createTime; // 创建时间
}
