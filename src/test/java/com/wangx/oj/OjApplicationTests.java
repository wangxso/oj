package com.wangx.oj;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangx.oj.entity.Rank;
import com.wangx.oj.entity.Submission;
import com.wangx.oj.entity.SubmissionStatics;
import com.wangx.oj.service.RankService;
import com.wangx.oj.service.SubmissionService;
import com.wangx.oj.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OjApplicationTests {
    @Autowired
    RankService rankService;
    @Autowired
    SubmissionService submissionService;
    @Autowired
    UserService userService;
    @Test
    public void test(){
        IPage<Rank> rankListRedis = rankService.findRankListRedis(1, 3);
        rankListRedis.getRecords().forEach(System.out::println);
    }

    @Test
    public void testSubStatics() {
        List<SubmissionStatics> submissionStatics = submissionService.findSubmissionStatics();
        submissionStatics.forEach(System.out::println);
    }

    @Test
    public void testUserStatics() {
        Map res = userService.findUserPassAndTotalSubmission("defa3bf67312450eac37f9c56334c7fa");
        System.out.println(JSON.toJSONString(res));
    }
}
