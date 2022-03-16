package com.wangx.oj.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangx.oj.common.Result;
import com.wangx.oj.entity.Announce;
import com.wangx.oj.mapper.AnnounceMapper;
import com.wangx.oj.service.AnnounceService;
import com.wangx.oj.utils.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AnnounceServiceImpl implements AnnounceService {

    @Autowired
    AnnounceMapper announceMapper;
    @Override
    public Result findAll() {
        return Result.success(announceMapper.findAll());
    }

    @Override
    public void publishAnnounce(Announce announce) {
        announce.setCreateTime(new Date());
        announce.setUpdateTime(new Date());
        announceMapper.insert(announce);
    }

    @Override
    public void deleteAnnounce(Integer aid) {
        announceMapper.deleteById(aid);
    }

    @Override
    public Result findByAid(Announce announce) {
        return Result.success(announceMapper.findByAid(announce));
    }

    @Override
    public IPage<Announce> findAnnouncePagination(Integer page, Integer pageSize) {
        Page<Announce> announcePage = new Page<>(page, pageSize);
        IPage<Announce> announceIPage = announceMapper.selectPage(announcePage, null);
        return announceIPage;
    }

    @Override
    public void updateAnnounce(Announce announce) {
        announce.setUpdateTime(new Date());
        announceMapper.updateById(announce);
    }
}
