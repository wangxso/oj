package com.wangx.oj.controller;

import com.wangx.oj.common.Result;
import com.wangx.oj.entity.TestCase;
import com.wangx.oj.service.TestCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/testcase")
public class TestCaseController {
    @Autowired
    TestCaseService testCaseService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    Result findOneByTid(@PathVariable String id) {
        TestCase testCase = testCaseService.findTestCaseById(id);
        return Result.success(testCase);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    Result add(@RequestBody TestCase testCase) {
        testCaseService.add(testCase);
        return Result.success("添加成功");
    }

}
