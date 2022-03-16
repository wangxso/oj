package com.wangx.oj.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangx.oj.entity.Rank;

import java.util.List;

public interface RankService {
    IPage<Rank> findRankListRedis(int page, int pageSize);
}
