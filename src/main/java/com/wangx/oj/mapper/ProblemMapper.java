package com.wangx.oj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wangx.oj.entity.Problem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProblemMapper extends BaseMapper<Problem> {
    List<Problem> findAll();

    Problem findOne(String pid);

    void insertOne(Problem problem);

    void deleteOne(Problem problem);
}
