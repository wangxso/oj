package com.wangx.oj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wangx.oj.entity.Announce;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AnnounceMapper extends BaseMapper<Announce> {
    List<Announce> findAll();

    void insertOne(Announce announce);

    void deleteOne(Announce announce);

    Announce findByAid(Announce announce);
}
