package org.example;

import com.alibaba.fastjson.JSONObject;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.example.constant.MQConstant;
import org.example.entity.LiveComment;
import org.example.util.RabbitUtil;
import org.example.util.TokenUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@ServerEndpoint("/websocket/{token}/{videoId}") // will create an instance for each connection
public class WebSocketService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);

    // key is sessionId, value is WebSocketService object
    public static final ConcurrentHashMap<String, WebSocketService> WEBSOCKET_MAP = new ConcurrentHashMap<>();
    // key is videoId, value is the hashset of sessionId
    public static final ConcurrentHashMap<Long, HashSet<String>> VIDEO_SESSION_MAP = new ConcurrentHashMap<>();
    // key is videoId, value is the locker object for this videoId
    public static final ConcurrentHashMap<Long, Object> videoIdLockMap = new ConcurrentHashMap<>();
    private Session session;
    private String sessionId;
    private Long userId;
    private Long videoId;
    private static ApplicationContext applicationContext;

    @OnOpen
    public void openConnection(Session session, @PathParam("token") String token, @PathParam("videoId") Long videoId){
        try{
            userId = TokenUtil.verifyToken(token);
        } catch(Exception ignored){
            logger.info("Non-user connected via websocket!");
        }
        this.videoId = videoId;
        sessionId = session.getId();
        this.session = session;
        WEBSOCKET_MAP.put(sessionId, this);
        if(!videoIdLockMap.contains(videoId)){
            videoIdLockMap.put(videoId, new Object());
        }
        insertVideoSession(videoId, sessionId);
//            ONLINE_COUNT.getAndIncrement();
        try{
            this.sendMessage("0");
        } catch (Exception e){
            logger.error("Websocket abnormal connection");
        }
    }

    @OnClose
    public void closeConnection(){
        //            ONLINE_COUNT.getAndDecrement();
        WEBSOCKET_MAP.remove(sessionId);
        removeVideoSession(videoId, sessionId);
        logger.info("Websocket session was closed: " + sessionId );
    }

    @OnMessage
    public void onMessage(String message){ // message includes videoId info
        logger.info("Websocket session id: " + sessionId + ", msg: " + message);
        if(message != null && !message.equals("")){
            try{
                // send to frontend
                Set<String> sessionIdSet = VIDEO_SESSION_MAP.get(videoId);
                for(String sessionId : sessionIdSet){
                    WebSocketService webSocketService = WebSocketService.WEBSOCKET_MAP.get(sessionId);
                    if(webSocketService != null && webSocketService.getSession().isOpen()){
                        try {
                            webSocketService.sendMessage(message);
                        } catch (IOException e) {
                            logger.error(e.getMessage());
                        }
                    }
                }

                if(userId != null){ // shouldn't be null since only user can send liveComments
                    // async send to MQ and then add to DB
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("message", message);
                    jsonObject.put("userId", userId);
                    RabbitUtil rabbitUtil = (RabbitUtil)applicationContext.getBean(RabbitUtil.class);
                    rabbitUtil.sendAsyncLiveCommentMsgToMQ(jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8));

                    // add to redis
                    LiveComment liveComment = JSONObject.parseObject(message, LiveComment.class);
                    liveComment.setUserId(userId);
                    liveComment.setCreatedTime(LocalDateTime.now());
                    LiveCommentService liveCommentService = (LiveCommentService) applicationContext.getBean(LiveCommentService.class);
                    liveCommentService.addLiveCommentToRedis(liveComment);
                }
            } catch(Exception e){
                logger.error("Error in receiving live comment");
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Throwable error){
        logger.error("Websocket error: " + error.getMessage());
    }

    public void sendMessage(String message) throws IOException{
        session.getBasicRemote().sendText(message);
    }

//    @Scheduled(fixedRate = 5000) // 5s, only total people online rather than per video
//    public void sendOnlineCount() throws IOException{
//        for(Map.Entry<String, WebSocketService> entry : WEBSOCKET_MAP.entrySet()){
//            WebSocketService webSocketService = entry.getValue();
//            if(webSocketService.session.isOpen()){
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("onlineCount", ONLINE_COUNT.get());
//                jsonObject.put("msg", "current people online is " + ONLINE_COUNT.get());
//                webSocketService.sendMessage(jsonObject.toJSONString());
//            }
//        }
//    }

    // in case that multiple threads fetch the same value(hashset) of the same key
    // and modify it concurrently
    public static void insertVideoSession(Long videoId, String sessionId) {
        synchronized (videoIdLockMap.get(videoId)){ // finer lock
            HashSet<String> hashSet = VIDEO_SESSION_MAP.get(videoId);
            if (hashSet == null) {
                hashSet = new HashSet<>();
            }
            hashSet.add(sessionId);
            VIDEO_SESSION_MAP.put(videoId, hashSet);
        }
    }

    private static void removeVideoSession(Long videoId, String sessionId) {
        synchronized (videoIdLockMap.get(videoId)){
            HashSet<String> hashSet = VIDEO_SESSION_MAP.get(videoId);
            hashSet.remove(sessionId);
            VIDEO_SESSION_MAP.put(videoId, hashSet);
        }
    }

    public Session getSession() {
        return session;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Long getUserId() {
        return userId;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        WebSocketService.applicationContext = applicationContext;
    }


}
