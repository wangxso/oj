package com.wangx.oj.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangx.oj.entity.Rank;
import com.wangx.oj.entity.User;
import com.wangx.oj.mapper.RankMapper;
import com.wangx.oj.mapper.SubmissionMapper;
import com.wangx.oj.mapper.UserMapper;
import com.wangx.oj.service.RankService;
import com.wangx.oj.utils.RedisUtils;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

@Service
@Slf4j
public class RankServiceImpl implements RankService {

    @Autowired
    RankMapper rankMapper;

    @Autowired
    SubmissionMapper submissionMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisUtils redisUtils;

    public static final String SCORE_RANK = "score_rank";


    @Override
    public IPage<Rank> findRankListRedis(int page, int pageSize) {

        IPage<Rank> rankIPage = new Page<>();
        List<Rank> rankList = new ArrayList<>();
        if (!redisUtils.hasKey(SCORE_RANK)) {
            List<User> users = userMapper.findAll();
            List<Map<String, Integer>> res = new ArrayList<>();
            Map<String, Integer> map;
            for (User user:users) {
                Integer pass = rankMapper.findUserPassNum(user.getUid());
                map = new HashMap<>();
                map.put(user.getUid(), pass);
                res.add(map);
                redisUtils.zAdd(SCORE_RANK, user.getUid(), pass);
            }
        }
        DecimalFormat df = new DecimalFormat("0.00");

        Set<ZSetOperations.TypedTuple<String>> list =
                redisUtils.list(SCORE_RANK, (page - 1) * pageSize, page * pageSize - 1);
        for (ZSetOperations.TypedTuple<String> tuple:list) {
            Rank rank = new Rank();
            // 设置rank中的用户id
            rank.setUid(tuple.getValue());
            rank.setPass(Objects.requireNonNull(tuple.getScore()).intValue());
            //数据库中查找用户信息
            User user = userMapper.selectById(rank.getUid());
            Integer total = rankMapper.findUserSubmission(rank.getUid());
            rank.setTotal(Long.valueOf(total));
            try {
                rank.setUsername(user.getUsername());
                rank.setLevel(user.getLevel());
            } catch (NullPointerException e) {// 可能会发生找不到用户的情况，未知bug
                log.error("has null user with uid \t" + rank.getUid());
                continue;
            }

            if (rank.getTotal() == 0) {
                rank.setAcRate("0");
            } else {
                double rate = ((double)rank.getPass() / rank.getTotal());
                rank.setAcRate(df.format(rate * 100));
            }

            rankList.add(rank);
        }

        rankIPage.setRecords(rankList);
        return rankIPage;
    }
}
