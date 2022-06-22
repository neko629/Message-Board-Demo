package com.laorunzi.msgboard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.laorunzi.msgboard.model.MbUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 2 * @Author: Crimson
 * 3 * @Date: 2022/3/5 13:10
 * 4
 */
@Mapper
public interface MbUserMapper extends BaseMapper<MbUser> {

    /**
     * 插入新用户
     * @param user
     * @return
     */
    @Insert("insert into mb_user (password, salt, email, user_name, create_time) values (#{" +
            "password}, #{salt}, #{email}, #{userName}, #{createTime})")
    int insertUser(MbUser user);

    /**
     * 邮箱查找用户
     * @param email
     * @return
     */
    @Select("select id, password, salt, email, user_name, create_time from mb_user where email = " +
            "#{email}")
    MbUser selectByEmail(String email);

    /**
     * 用户名查找用户
     * @param userName
     * @return
     */
    @Select("select id, password, salt, email, user_name, create_time from mb_user where user_name = " +
            "#{userName}")
    MbUser selectByUserName(String userName);
}
