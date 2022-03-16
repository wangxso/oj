package com.wangx.oj.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangx.oj.common.CodeMsg;
import com.wangx.oj.common.Result;
import com.wangx.oj.entity.Problem;
import com.wangx.oj.entity.Submission;
import com.wangx.oj.mapper.ProblemMapper;
import com.wangx.oj.mapper.SubmissionMapper;
import com.wangx.oj.service.ProblemService;
import com.wangx.oj.service.SubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;


@Service
@Slf4j
public class ProblemServiceImpl implements ProblemService {

    @Autowired
    ProblemMapper problemMapper;
    @Autowired
    SubmissionMapper submissionMapper;

    @Override
    public void deleteOneProblem(Problem problem) {
        problemMapper.deleteOne(problem);
    }

    @Override
    public Problem findOneProblem(String pid) {
        log.info(pid);
        Problem one = problemMapper.findOne(pid);
        return one;
    }

    @Override
    public void addOneProblem(Problem problem) {
        problemMapper.insertOne(problem);
    }

    @Override
    public IPage<Problem> findProblemPagination(Integer page, Integer pageSize, Integer isAdmin) {
        List<Problem> res = new ArrayList<>();
        QueryWrapper<Problem> problemQueryWrapper = new QueryWrapper<>();
        problemQueryWrapper.eq("visible", true);
        Page<Problem> problemPage = new Page<>(page, pageSize);
        // 若是管理员查询就不设置条件
        if (isAdmin == 1) problemQueryWrapper = null;
        IPage<Problem> iPage = problemMapper.selectPage(problemPage, problemQueryWrapper);
        List<Problem> problemList = iPage.getRecords();
        DecimalFormat df = new DecimalFormat("0.00");
        // 计算每个题目的正确率
        for(Problem problem: problemList) {
            Integer totalNum = getTotalNum(problem.getPid());
            problem.setTotalSubmit(totalNum);
            Integer acNum = getAcNum(problem.getPid());
            problem.setPass(acNum);
            if (problem.getTotalSubmit() == 0) {
                problem.setAcRate("100");
            } else {
                Double rate = ((double)problem.getPass() / problem.getTotalSubmit());
                problem.setAcRate(df.format(rate * 100));
            }
            res.add(problem);
        }
        iPage.setRecords(res);
        return iPage;
    }

    @Override
    public void update(Problem problem) {
        problemMapper.updateById(problem);
    }

    @Override
    public void add(Problem problem) {
        log.info(problem.toString());
        problem.setCreateTime(new Date());
        problem.setUpdateTime(new Date());
        problemMapper.insert(problem);
    }

    @Override
    public String getProblemDetail(String pid) {
        Map<String, Integer> passDetailMap = new HashMap<>();
        for (Integer i=1;i<=9;i++) {
            QueryWrapper<Submission> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("result", i).eq("pid", pid);
            Integer count = submissionMapper.selectCount(queryWrapper);
            passDetailMap.put(i.toString(), count);
        }
        String jsonString = JSON.toJSONString(passDetailMap);
        return jsonString;
    }

    @Override
    public Integer getAcNum(String pid) {
        QueryWrapper<Submission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pid", pid).eq("result", 1);
        Integer count = submissionMapper.selectCount(queryWrapper);
        return count;
    }

    @Override
    public Integer getTotalNum(String pid) {
        QueryWrapper<Submission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pid", pid);
        Integer count = submissionMapper.selectCount(queryWrapper);
        return count;
    }
}
