package com.wangx.oj.common;

import com.alibaba.fastjson.JSON;
import com.wangx.oj.controller.WebSocketServer;
import com.wangx.oj.entity.Message;
import com.wangx.oj.entity.Submission;
import com.wangx.oj.entity.Submit;
import com.wangx.oj.service.SubmissionService;
import com.wangx.oj.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

@Component
@Slf4j
public class MessageConsumer {
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    SubmissionService submissionService;
    @Autowired
    WebSocketServer webSocketServer;

    @RabbitListener(queues = "result")
    public void receive(String message) {
        try {
            message = message.replace("\\", "");
            message = message.substring(1, message.length() - 1);
            log.info("result 接收消息-->" + message);
            Submission submission = JSON.parseObject(message, Submission.class);
            submissionService.update(submission);
            submission = submissionService.findSubmissionById(submission.getSid());
            log.info(submission.toString());
            webSocketServer.sendMessageByUsername(submission.getUid(), submission.getResult().toString());
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }
}
