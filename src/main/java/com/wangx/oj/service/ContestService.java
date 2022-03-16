package com.wangx.oj.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangx.oj.entity.Contest;


public interface ContestService {
    IPage<Contest> findContestPagination(int page, int pageSize);
    IPage findSubmissionForContestPagination(String cid, Integer page, Integer pageSize);
    void updateContest(Contest contest);
    void addContest(Contest contest);
}
