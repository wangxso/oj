package com.wangx.oj.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangx.oj.entity.Contest;
import com.wangx.oj.entity.Problem;
import com.wangx.oj.entity.Submission;
import com.wangx.oj.mapper.ContestMapper;
import com.wangx.oj.service.ContestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ContestServiceImpl implements ContestService {
    @Autowired
    private ContestMapper contestMapper;


    @Override
    public IPage<Contest> findContestPagination(int page, int pageSize) {
        Page<Contest> contestPage = new Page<>();
        IPage<Contest> contestIPage = contestMapper.selectPage(contestPage, null);

        return contestIPage;
    }

    @Override
    public IPage findSubmissionForContestPagination(String cid, Integer page, Integer pageSize) {
        Integer start = (page - 1)*pageSize;
        Integer end = page * pageSize;
        List<Submission> submissionForContest = contestMapper.findSubmissionForContest(cid, start, end);
        Integer count = contestMapper.findSubmissionContestCount(cid);
        IPage<Submission> submissionIPage = new Page<>();
        submissionIPage.setRecords(submissionForContest);
        submissionIPage.setTotal(count);
        return submissionIPage;
    }

    @Override
    public void updateContest(Contest contest) {
        contestMapper.updateById(contest);
    }

    public void addContest(Contest contest) {
        contest.setCreateDate(new Date());
        contestMapper.insert(contest);
    }

}
