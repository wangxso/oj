package com.wangx.oj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wangx.oj.entity.TestCase;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TestCaseMapper extends BaseMapper<TestCase> {
    void addOne(TestCase testCase);

    TestCase findOneByTid(TestCase testCase);

    TestCase deleteOneByTid(TestCase testCase);
}
