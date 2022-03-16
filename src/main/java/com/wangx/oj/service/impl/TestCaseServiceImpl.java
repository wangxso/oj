package com.wangx.oj.service.impl;

import com.wangx.oj.common.Result;
import com.wangx.oj.entity.TestCase;
import com.wangx.oj.mapper.TestCaseMapper;
import com.wangx.oj.service.TestCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestCaseServiceImpl implements TestCaseService {
    @Autowired
    TestCaseMapper testCaseMapper;

    @Override
    public void add(TestCase testCase) {
        testCaseMapper.insert(testCase);
    }

    @Override
    public TestCase findTestCaseById(String id) {
        TestCase testCase = testCaseMapper.selectById(id);
        return testCase;
    }
}
