package com.laorunzi.msgboard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.laorunzi.msgboard.model.MbMsg;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 2 * @Author: Crimson
 * 3 * @Date: 2022/3/6 11:39
 * 4
 */
@Mapper
public interface MbMsgMapper extends BaseMapper<MbMsg> {

    /**
     * 留言入库
     * @param msg
     * @return
     */
    @Insert("insert into mb_msg (parent_id, msg, user_name, create_time) values(#{parentId}, " +
            "#{msg}, #{userName}, #{createTime})")
    int insertMsg(MbMsg msg);

    /**
     * 批量查出所有留言
     * @return
     */
    @Select("select id, parent_id, msg, user_name, create_time from mb_msg order by create_time " +
            "asc")
    List<MbMsg> selectAllMsgOrderByTime();
}
