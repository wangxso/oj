package com.wangx.oj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangx.oj.entity.Contest2Submission;
import com.wangx.oj.entity.Submission;
import com.wangx.oj.entity.SubmissionStatics;
import com.wangx.oj.mapper.Contest2SubmissionMapper;
import com.wangx.oj.mapper.SubmissionMapper;
import com.wangx.oj.mapper.SubmissionStaticsMapper;
import com.wangx.oj.service.SubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class SubmissionServiceImpl implements SubmissionService {

    @Autowired
    SubmissionMapper submissionMapper;

    @Autowired
    Contest2SubmissionMapper c2sMapper;

    @Autowired
    private SubmissionStaticsMapper submissionStaticsMapper;
    @Override
    public IPage findSubmissionPagination(Integer index, Integer pageSize) {
        Page<Submission> page = new Page<>(index, pageSize);
        QueryWrapper<Submission> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        return submissionMapper.selectPage(page, queryWrapper);
    }

    @Override
    public void update(Submission submission) {
        submissionMapper.updateById(submission);
        return;
    }

    @Override
    public void add(Submission submission) {
        log.info("add" + submission.toString());
        submissionMapper.insert(submission);
        return;
    }

    @Override
    public Submission findSubmissionById(String sid) {
        Submission submission = submissionMapper.selectById(sid);
        return submission;
    }

    @Override
    public Integer getSubmissionCount() {
        Integer count = submissionMapper.selectCount(null);
        return count;
    }

    @Override
    public void addForContest(Submission submission, String cid) {
        submission.setResult(-1);
        c2sMapper.insert(new Contest2Submission(submission.getSid(), cid, submission.getUid()));
        submissionMapper.insert(submission);
        return;
    }

    @Override
    public List<SubmissionStatics> findSubmissionStatics() {
        return submissionStaticsMapper.selectList(null);
    }
}
