package com.wangx.oj.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangx.oj.entity.Submission;
import com.wangx.oj.entity.SubmissionStatics;

import java.util.List;

public interface SubmissionService {
    IPage<Submission> findSubmissionPagination(Integer index, Integer pageSize);
    void update(Submission submission);
    void add(Submission submission);
    Submission findSubmissionById(String sid);
    Integer getSubmissionCount();
    void addForContest(Submission submission, String cid);
    List<SubmissionStatics> findSubmissionStatics();
}
