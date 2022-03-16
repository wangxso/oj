package com.wangx.oj.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangx.oj.common.Result;
import com.wangx.oj.entity.Announce;
import com.wangx.oj.service.AnnounceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/announce")
public class AnnounceController {
    @Autowired
    AnnounceService announceService;

    @RequestMapping("/findAll")
    Result findAll() {
        return announceService.findAll();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    Result add(@RequestBody Announce announce) {
        announceService.publishAnnounce(announce);
        return Result.success("success");
    }

    @RequestMapping(value = "/{aid}", method = RequestMethod.DELETE)
    Result delete(@PathVariable Integer aid) {
        announceService.deleteAnnounce(aid);
        return Result.success("success");
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    Result update(@RequestBody Announce announce){
        announceService.updateAnnounce(announce);
        return Result.success("success");
    }

    @RequestMapping("/findByAid")
    Result findByAid(@RequestBody Announce announce) {
        return announceService.findByAid(announce);
    }

    @RequestMapping(value = "/{page}/{pageSize}", method = RequestMethod.GET)
    Result findAnnouncePagination(@PathVariable Integer page, @PathVariable Integer pageSize) {
        IPage<Announce> announcePagination = announceService.findAnnouncePagination(page, pageSize);
        return Result.success(announcePagination);
    }
}
