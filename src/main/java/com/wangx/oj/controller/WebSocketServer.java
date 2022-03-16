package com.wangx.oj.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@ServerEndpoint("/ws/{userId}") //WebSocket客户端建立连接的地址
@Component
@Slf4j
public class WebSocketServer {
    /**
     * 存活的session集合（使用线程安全的map保存）
     */
    private static Map<String, Session> livingSessions = new ConcurrentHashMap<>();
    private static Map<String, String> userId2SessionId = new ConcurrentHashMap<>();
    /**
     * 建立连接的回调方法
     *
     * @param session 与客户端的WebSocket连接会话
     * @param userId  用户名，WebSocket支持路径参数
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        livingSessions.put(session.getId(), session);
        userId2SessionId.put(userId, session.getId());
        log.info(userId + "进入连接");
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("userId") String userId) {
        log.info(userId + " : " + message);
        sendMessageToAll(userId + " : " + message);
    }


    @OnError
    public void onError(Session session, Throwable error) {
        log.info("发生错误");
        log.error(error.getStackTrace() + "");
    }


    @OnClose
    public void onClose(Session session, @PathParam("userId") String userId) {
        livingSessions.remove(session.getId());
        log.info(userId + " 关闭连接");
    }

    /**
     * 单独发送消息
     *
     * @param userId
     * @param message
     */
    public void sendMessageByUsername(String userId, String message) {
        try {
            log.info("send message to " + userId);
            String sessionId = userId2SessionId.get(userId);
            Session session = livingSessions.get(sessionId);
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            log.info("没有此用户");
        }
    }

    /**
     * 发送消息
     * @param session
     * @param message
     */
    public void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 群发消息
     *
     * @param message
     */
    public void sendMessageToAll(String message) {
        livingSessions.forEach((sessionId, session) -> {
            sendMessage(session, message);
        });
    }

}

