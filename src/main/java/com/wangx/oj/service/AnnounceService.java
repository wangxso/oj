package com.wangx.oj.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangx.oj.common.Result;
import com.wangx.oj.entity.Announce;

public interface AnnounceService {
    Result findAll();
    void publishAnnounce(Announce announce);
    void deleteAnnounce(Integer aid);
    Result findByAid(Announce announce);
    IPage<Announce> findAnnouncePagination(Integer page, Integer pageSize);
    void updateAnnounce(Announce announce);
}
