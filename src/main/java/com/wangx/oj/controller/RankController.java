package com.wangx.oj.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangx.oj.common.Result;
import com.wangx.oj.entity.Rank;
import com.wangx.oj.service.RankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rank")
public class RankController {
    @Autowired
    RankService rankService;

    @RequestMapping(value = "/{page}/{pageSize}", method = RequestMethod.GET)
    public Result findRankPagination(@PathVariable int page, @PathVariable int pageSize){
        IPage<Rank> pagination = rankService.findRankListRedis(page, pageSize);
        return Result.success(pagination);
    }
}
