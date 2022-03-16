package com.wangx.oj.service;

import com.wangx.oj.common.Result;
import com.wangx.oj.entity.TestCase;

public interface TestCaseService {
    void add(TestCase testCase);

    TestCase findTestCaseById(String id);
}
