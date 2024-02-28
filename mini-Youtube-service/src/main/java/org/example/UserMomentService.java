package org.example;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.example.constant.MQConstant;
import org.example.entity.UserMoment;
import org.example.mapper.UserMomentMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserMomentService {

    private final UserMomentMapper userMomentMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public UserMomentService(UserMomentMapper userMomentMapper, RedisTemplate<String, String> redisTemplate, RabbitTemplate rabbitTemplate){
        this.userMomentMapper = userMomentMapper;
        this.redisTemplate = redisTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void addUserMoment(UserMoment userMoment){
        userMoment.setCreatedTime(LocalDateTime.now());
        userMomentMapper.addUserMoment(userMoment);
        rabbitTemplate.convertAndSend(MQConstant.QUEUE_MOMENTS, JSONObject.toJSONString(userMoment).getBytes(StandardCharsets.UTF_8));
    }

    public List<UserMoment> getUserSubscribedMoments(Long userId) {
        String key = "subscribed-" + userId;
        String listStr = redisTemplate.opsForValue().get(key);
        return JSONArray.parseArray(listStr, UserMoment.class);
    }
}
