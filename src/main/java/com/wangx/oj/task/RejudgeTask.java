package com.wangx.oj.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wangx.oj.entity.Submission;
import com.wangx.oj.mapper.SubmissionMapper;
import com.wangx.oj.utils.JudgeUtils;
import com.wangx.oj.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class RejudgeTask {
    private int i;

    @Autowired
    private SubmissionMapper submissionMapper;

    @Autowired
    private JudgeUtils judgeUtils;

    @Autowired
    private RedisUtils redisUtils;
    /**
     * 重新
     */
    @Scheduled(cron = "*/15 * * * * ?")
    public void execute() {
        QueryWrapper<Submission> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Submission::getResult, -1);
        List<Submission> submissions = submissionMapper.selectList(queryWrapper);
        for(Submission submission: submissions) {
            judgeUtils.reJudge(submission);
        }
        log.info("Run Rejudge if has pending submission per 15 seconds， and now rejudge num is : " + submissions.size());
    }
}
