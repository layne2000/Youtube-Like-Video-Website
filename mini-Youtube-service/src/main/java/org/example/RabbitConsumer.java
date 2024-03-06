package org.example;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.netty.util.internal.StringUtil;
import org.example.UserFollowingService;
import org.example.entity.UserFollowing;
import org.example.entity.UserMoment;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RabbitConsumer {
    private final RedisTemplate<String, String> redisTemplate;

    private final UserFollowingService userFollowingService;

    @Autowired
    RabbitConsumer(RedisTemplate<String, String> redisTemplate, UserFollowingService userFollowingService){
        this.redisTemplate = redisTemplate;
        this.userFollowingService = userFollowingService;
    }

    @RabbitListener(queues = "momentsQueue")
    public void consumeMessage(String msg){
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

}
