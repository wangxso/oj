package com.wangx.oj.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangx.oj.common.Result;
import com.wangx.oj.entity.Submission;
import com.wangx.oj.entity.SubmissionStatics;
import com.wangx.oj.entity.TestCase;
import com.wangx.oj.entity.User;
import com.wangx.oj.service.SubmissionService;
import com.wangx.oj.service.TestCaseService;
import com.wangx.oj.service.UserService;
import com.wangx.oj.utils.IPUtils;
import com.wangx.oj.utils.JudgeUtils;
import com.wangx.oj.utils.RedisUtils;
import com.wangx.oj.utils.UUIDGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;

@Slf4j
@RestController
@RequestMapping("/submission")
public class SubmissionController {
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
    public static final String SCORE_RANK = "score_rank";
    private static final String STATICS_SUBMISSION = "statics_submission";
    @RequestMapping(value = "/{index}/{pageSize}", method = RequestMethod.GET)
    public Result findAllPagination(@PathVariable Integer index, @PathVariable Integer pageSize) {
        IPage<Submission> submissionPagination = submissionService.findSubmissionPagination(index, pageSize);
        List<Submission> submissionList = submissionPagination.getRecords();
        Iterator<Submission> iterator = submissionList.iterator();
        while (iterator.hasNext()){
            Submission submission = iterator.next();
            User user = userService.findUserById(submission.getUid());
            // 未判题，重新判题
            if (submission.getResult().equals(-1)){
                judgeUtils.reJudge(submission);
            }
            submission.setUser(user);
        }
        submissionPagination.setRecords(submissionList);
        return Result.success(submissionPagination);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public Result update(Submission submission) {
        submissionService.update(submission);
        return Result.success("成功");
    }

    /**
     * 判题，通过mq发送消息给判题端
     * @param submission 提交信息
     * @param tid 测试样例 id
     * @return
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Result sendMessage(@RequestBody Submission submission, @RequestParam String tid, HttpServletRequest request) {
        redisUtils.remove(SCORE_RANK);
        judgeUtils.submitJudge(submission, tid, request);
        return Result.success("success");
    }

    /**
     * 获取提交总数
     * @return
     */
    @RequestMapping(value = "count", method = RequestMethod.GET)
    public Result getCount(){
        Integer count = 0;
        if (redisUtils.hasKey("submissionCount")){
            count = (Integer) redisUtils.get("submissionCount");
        } else {
            count = submissionService.getSubmissionCount();
            redisUtils.set("submissionCount", count);
        }
        return Result.success(count);
    }



    @RequestMapping("/statics")
    public Result getTodaySubmission(){
        List<SubmissionStatics> submissionStatics = submissionService.findSubmissionStatics();
        return Result.success(submissionStatics);
    }

}
