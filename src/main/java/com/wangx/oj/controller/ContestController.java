package com.wangx.oj.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangx.oj.common.Result;
import com.wangx.oj.entity.Contest;
import com.wangx.oj.entity.Submission;
import com.wangx.oj.entity.TestCase;
import com.wangx.oj.entity.User;
import com.wangx.oj.service.ContestService;
import com.wangx.oj.service.SubmissionService;
import com.wangx.oj.service.TestCaseService;
import com.wangx.oj.service.UserService;
import com.wangx.oj.utils.JudgeUtils;
import com.wangx.oj.utils.RedisUtils;
import com.wangx.oj.utils.UUIDGenerator;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/contest")
public class ContestController {
    @Autowired
    private ContestService contestService;

    @Autowired
    SubmissionService submissionService;

    @Autowired
    AmqpTemplate amqpTemplate;

    @Autowired
    UserService userService;

    @Autowired
    TestCaseService testCaseService;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    JudgeUtils judgeUtils;

    @RequestMapping(value = "/{page}/{pageSize}", method = RequestMethod.GET)
    public Result findPagination(@PathVariable int page,@PathVariable int pageSize) {
        IPage<Contest> contestPagination = contestService.findContestPagination(page, pageSize);
        return Result.success(contestPagination);
    }

    @RequestMapping(value = "/submission/{cid}/{page}/{pageSize}", method = RequestMethod.GET)
    public Result findContestSubmissionPagination(@PathVariable String cid, @PathVariable int page, @PathVariable int pageSize) {
        IPage pagination = contestService.findSubmissionForContestPagination(cid, page, pageSize);
        List<Submission> submissionList = pagination.getRecords();
        Iterator<Submission> iterator = submissionList.iterator();
        while (iterator.hasNext()){
            Submission submission = iterator.next();
            User user = userService.findUserById(submission.getUid());
            submission.setUser(user);
            if (submission.getResult().equals(-1)){
                judgeUtils.reJudge(submission);
            }
        }
        return Result.success(pagination);
    }
    @RequestMapping(value = "/submission/{cid}", method = RequestMethod.POST)
    public Result contestSubmission(@RequestBody Submission submission, @PathVariable String cid, HttpServletRequest request){
        judgeUtils.submitContestJudge(submission, cid, request);
        return Result.success("success");
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public Result updateContest(@RequestBody Contest contest){
        contestService.updateContest(contest);
        return Result.success("更新成功");
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Result addContest(@RequestBody Contest contest) {
        contestService.addContest(contest);
        return Result.success("添加成功");
    }

}
