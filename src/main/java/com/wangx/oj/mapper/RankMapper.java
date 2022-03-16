package com.wangx.oj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wangx.oj.entity.Rank;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RankMapper extends BaseMapper<Rank> {
    @Select("SELECT COUNT(1) FROM t_submission where uid = #{uid} and result = 1")
    Integer findUserPassNum(String uid);

    @Select("SELECT COUNT(1) FROM t_submission where uid = #{uid}")
    Integer findUserSubmission(String uid);
}
