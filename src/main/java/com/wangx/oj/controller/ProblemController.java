package com.wangx.oj.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangx.oj.common.Result;
import com.wangx.oj.entity.Problem;
import com.wangx.oj.entity.User;
import com.wangx.oj.service.ProblemService;
import com.wangx.oj.service.UserService;
import com.wangx.oj.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/problem")
public class ProblemController {
    @Autowired
    ProblemService problemService;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    UserService userService;

    @RequestMapping("/deleteOne")
    Result delete(@RequestBody Problem problem) {
        problemService.deleteOneProblem(problem);
        return Result.success(null);
    }


    @RequestMapping("/findOneByPid")
    Result findOne(@RequestParam String pid) {
        Problem problem = problemService.findOneProblem(pid);
        User user = userService.findUserById(problem.getUid());
        Map res = new HashMap();
        res.put("problem", problem);
        res.put("user",user);
        return Result.success(res);
    }

    @RequestMapping(value = "/{page}/{pageSize}", method = RequestMethod.GET)
    Result findProblemPagination(@PathVariable Integer page, @PathVariable Integer pageSize) {
        IPage<Problem> problemPagination = problemService.findProblemPagination(page, pageSize, 0);
        return Result.success(problemPagination);
    }

    @RequestMapping(value = "/admin/{page}/{pageSize}", method = RequestMethod.GET)
    Result findProblemPaginationAdmin(@PathVariable Integer page, @PathVariable Integer pageSize) {
        IPage<Problem> problemPagination = problemService.findProblemPagination(page, pageSize, 1);
        return Result.success(problemPagination);
    }


    @RequestMapping(value = "", method = RequestMethod.PUT)
    Result updateProblem(@RequestBody Problem problem) {
        problem.setUpdateTime(new Date());
        problemService.update(problem);
        return Result.success("修改成功");
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    Result add(@RequestBody Problem problem) {
        problemService.add(problem);
        return Result.success("添加成功");
    }


    @RequestMapping(value = "chart", method = RequestMethod.GET)
    Result getChartData(@RequestParam String pid) {
        // 1. 从redis中找表格数据
        String problemDetail = (String) redisUtils.hmGet("problemChart", pid);
        // 2. redis中没有从数据库查，然后存到redis
        if (problemDetail == null || "".equals(problemDetail)) {
            log.info("从数据库中获取题目表格数据");
            problemDetail = problemService.getProblemDetail(pid);
            redisUtils.hmSet("problemChart", pid, problemDetail);
        }
        return Result.success(problemDetail);
    }

}
