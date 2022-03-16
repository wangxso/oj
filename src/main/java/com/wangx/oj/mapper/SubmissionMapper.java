package com.wangx.oj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wangx.oj.entity.Submission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SubmissionMapper extends BaseMapper<Submission> {
    List<Submission> findSubmissionPagination(Integer start, Integer end);


}
