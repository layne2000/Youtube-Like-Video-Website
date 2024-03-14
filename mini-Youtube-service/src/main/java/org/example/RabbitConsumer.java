package org.example;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.netty.util.internal.StringUtil;
import org.example.UserFollowingService;
import org.example.entity.LiveComment;
import org.example.entity.UserFollowing;
import org.example.entity.UserMoment;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.example.constant.MQConstant.QUEUE_LIVE_COMMENTS;
import static org.example.constant.MQConstant.QUEUE_MOMENTS;

@Component
public class RabbitConsumer {
    private final RedisTemplate<String, String> redisTemplate;
    private final UserFollowingService userFollowingService;
    private final LiveCommentService liveCommentService;

    @Autowired
    RabbitConsumer(RedisTemplate<String, String> redisTemplate, UserFollowingService userFollowingService,
                   LiveCommentService liveCommentService){
        this.redisTemplate = redisTemplate;
        this.userFollowingService = userFollowingService;
        this.liveCommentService = liveCommentService;
    }

    @RabbitListener(queues = QUEUE_MOMENTS)
    public void consumeMomentsQueueMessage(String msg){
        UserMoment userMoment = JSONObject.toJavaObject(JSONObject.parseObject(msg), UserMoment.class);
        Long userId = userMoment.getUserId();
        List<UserFollowing> fanList = userFollowingService.getUserFans(userId);
        for(UserFollowing fan : fanList){
            String key = "subscribed-" + fan.getUserId();
            String subscribedListStr = redisTemplate.opsForValue().get(key);
            List<UserMoment> subscribedList;
            if(StringUtil.isNullOrEmpty(subscribedListStr)){
                subscribedList = new ArrayList<>();
            }else{
                subscribedList = JSONArray.parseArray(subscribedListStr, UserMoment.class);
            }
            subscribedList.add(userMoment);
            // to make the stored data readable, if we don't want to serialize manually,
            // we can use its standard JdkSerializationRedisSerializer which will convert to
            // and from binary data automatically
            redisTemplate.opsForValue().set(key, JSONObject.toJSONString(subscribedList));
        }
    }

    @RabbitListener(queues = QUEUE_LIVE_COMMENTS)
    public void consumeLiveCommentsQueueMessage(String msg){
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String userId = jsonObject.getString("userId");
        String message = jsonObject.getString("message"); // original message sent by frontend
        LiveComment liveComment = JSONObject.parseObject(message, LiveComment.class);
        liveComment.setUserId(Long.valueOf(userId));
        liveComment.setCreatedTime(LocalDateTime.now());
        liveCommentService.addLiveCommentToDB(liveComment);
    }
}
