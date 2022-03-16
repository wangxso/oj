package com.wangx.oj.utils;

import com.alibaba.fastjson.JSON;
import com.wangx.oj.common.Result;
import com.wangx.oj.entity.Submission;
import com.wangx.oj.entity.TestCase;
import com.wangx.oj.service.SubmissionService;
import com.wangx.oj.service.TestCaseService;
import com.wangx.oj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Slf4j
public class JudgeUtils {
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

    private static final String USER_PASS_TOTAL = "user_pass_total";
    /**
     * 重新判题
     * @param submission
     */
    public void reJudge(Submission submission) {
        // 设置为null更新题目详情的表格
        redisUtils.hmSet("problemChart", submission.getPid(), null);
        TestCase testCase = testCaseService.findTestCaseById(submission.getPid());
        // 测试样例和提交信息不存在一个表中，不进行存储
        submission.setInput(testCase.getInput());
        submission.setOutput(testCase.getOutput());
        // 发送信息
        amqpTemplate.convertAndSend("judge", JSON.toJSONString(submission));
    }

    /**
     * 提交比赛答案
     * @param submission
     */
    public void submitContestJudge(Submission submission, String cid, HttpServletRequest request){
        TestCase testCase = testCaseService.findTestCaseById(submission.getPid());
        String sid = UUIDGenerator.getUUID();
        submission.setSid(sid);
        submission.setIp(IPUtils.getIpAddress(request));
        submission.setInput(testCase.getInput());
        submission.setOutput(testCase.getOutput());
        submission.setCreateTime(new Date());
        submissionService.addForContest(submission, cid);
        amqpTemplate.convertAndSend("judge", JSON.toJSONString(submission));
    }

    public void submitJudge(Submission submission, String tid, HttpServletRequest request){
        if (redisUtils.hasKey("submissionCount")) redisUtils.remove("submissionCount");
        if (redisUtils.hasKey(USER_PASS_TOTAL)) redisUtils.remove(USER_PASS_TOTAL);
        Date dateNow = new Date();
        SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd");
        String sign = smf.format(dateNow).replace("-", "");
        log.info("sign: " + sign);
        redisUtils.setBit(sign, 0, true);
        // 设置为null更新题目详情的表格
        redisUtils.hmSet("problemChart", submission.getPid(), null);
        TestCase testCase = testCaseService.findTestCaseById(tid);
        submission.setSid(UUIDGenerator.getUUID());
        submission.setIp(IPUtils.getIpAddress(request));
        submission.setCreateTime(new Date());
        submission.setInput(testCase.getInput());
        submission.setOutput(testCase.getOutput());
        submission.setResult(-1); // pending
        log.info(submission.toString());
        submissionService.add(submission);
        amqpTemplate.convertAndSend("judge", JSON.toJSONString(submission));
    }

}
