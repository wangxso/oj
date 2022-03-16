package com.wangx.oj.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangx.oj.entity.Contest;
import com.wangx.oj.entity.Submission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ContestMapper extends BaseMapper<Contest>  {
    @Select("SELECT sid,pid,create_time,uid,language,time_cost,result,memory_cost,code FROM t_submission WHERE sid in (SELECT sid FROM `t_contest_submission`WHERE cid = #{cid})  ORDER BY create_time DESC LIMIT #{start},#{end}")
    List<Submission> findSubmissionForContest(@Param("cid") String cid, @Param("start") Integer start, @Param("end") Integer end);

    @Select("SELECT COUNT(*) FROM  t_submission WHERE sid in (SELECT sid FROM `t_contest_submission`WHERE cid = #{cid})")
    Integer findSubmissionContestCount(@Param("cid") String cid);
}
