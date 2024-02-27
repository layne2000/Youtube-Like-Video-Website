package org.example;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.example.constant.MQConstant;
import org.example.entity.UserMoment;
import org.example.util.RocketMQUtil;
import org.example.mapper.UserMomentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserMomentService {

    private final ApplicationContext applicationContext;
    private final UserMomentMapper userMomentMapper;
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public UserMomentService(ApplicationContext applicationContext, UserMomentMapper userMomentMapper, RedisTemplate<String, String> redisTemplate){
        this.applicationContext = applicationContext;
        this.userMomentMapper = userMomentMapper;
        this.redisTemplate = redisTemplate;
    }

    public void addUserMoment(UserMoment userMoment) throws Exception {
        userMoment.setCreatedTime(LocalDateTime.now());
        userMomentMapper.addUserMoment(userMoment);
        DefaultMQProducer producer = (DefaultMQProducer)applicationContext.getBean("momentsProducer");
        Message msg = new Message(MQConstant.TOPIC_MOMENTS, JSONObject.toJSONString(userMoment).getBytes(StandardCharsets.UTF_8));
        RocketMQUtil.syncSendMsg(producer, msg);
    }

    public List<UserMoment> getUserSubscribedMoments(Long userId) {
        String key = "subscribed-" + userId;
        String listStr = redisTemplate.opsForValue().get(key);
        return JSONArray.parseArray(listStr, UserMoment.class);
    }
}
