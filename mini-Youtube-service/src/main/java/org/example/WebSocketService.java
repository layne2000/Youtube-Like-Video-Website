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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@ServerEndpoint("/websocket/{token}") // will create an instance for each connection
public class WebSocketService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);
    public static final ConcurrentHashMap<String, WebSocketService> WEBSOCKET_MAP = new ConcurrentHashMap<>();
    private Session session;
    private String sessionId;
    private Long userId;
    private static ApplicationContext applicationContext;

    @OnOpen
    public void openConnection(Session session, @PathParam("token") String token){
        try{
            userId = TokenUtil.verifyToken(token);
        } catch(Exception ignored){}
        sessionId = session.getId();
        this.session = session;
        if(WEBSOCKET_MAP.containsKey(sessionId)){
            WEBSOCKET_MAP.remove(sessionId);
            WEBSOCKET_MAP.put(sessionId, this);
        } else{
            WEBSOCKET_MAP.put(sessionId, this);
            ONLINE_COUNT.getAndIncrement();
        }
        try{
            this.sendMessage("0"); // test connection but also indicate 0 people online
        } catch (Exception e){
            logger.error("Websocket abnormal connection");
        }
    }

    @OnClose
    public void closeConnection(){
        if(WEBSOCKET_MAP.containsKey(sessionId)){
            WEBSOCKET_MAP.remove(sessionId);
            ONLINE_COUNT.getAndDecrement();
        }
        logger.info("Websocket session was closed: " + sessionId + " online people: " + ONLINE_COUNT.get());
    }

    @OnMessage
    public void onMessage(String message){ // message includes videoId info
        logger.info("Websocket session id: " + sessionId + ", msg: " + message);
        if(message != null && !message.equals("")){
            try{
                // add the message to MQ
                for(Map.Entry<String, WebSocketService> entry : WEBSOCKET_MAP.entrySet()){
                    WebSocketService webSocketService = entry.getValue();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("message", message);
                    jsonObject.put("sessionId", webSocketService.getSessionId());
                    RabbitUtil rabbitUtil = (RabbitUtil)applicationContext.getBean(RabbitUtil.class);
                    rabbitUtil.sendAsyncMsgToMQ(jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8));
                }
                if(userId !=null){
                    // async add to DB
                    LiveComment liveComment = JSONObject.parseObject(message, LiveComment.class);
                    liveComment.setUserId(userId);
                    liveComment.setCreatedTime(LocalDateTime.now());
                    LiveCommentService liveCommentService = (LiveCommentService) applicationContext.getBean(LiveCommentService.class);
                    liveCommentService.asyncAddLiveCommentToDB(liveComment);
                    // add to redis
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

    @Scheduled(fixedRate = 5000) // 5s, only total people online rather than per video
    public void sendOnlineCount() throws IOException{
        for(Map.Entry<String, WebSocketService> entry : WEBSOCKET_MAP.entrySet()){
            WebSocketService webSocketService = entry.getValue();
            if(webSocketService.session.isOpen()){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("onlineCount", ONLINE_COUNT.get());
                jsonObject.put("msg", "current people online is " + ONLINE_COUNT.get());
                webSocketService.sendMessage(jsonObject.toJSONString());
            }
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
