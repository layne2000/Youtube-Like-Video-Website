package org.example;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.netty.util.internal.StringUtil;
import org.example.entity.LiveComment;
import org.example.mapper.LiveCommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

@Service
public class LiveCommentService {
    private RedisTemplate<String, String> redisTemplate;
    private static final String LIVE_COMMENT_BASE_KEY = "live-comment-video:";
    private final LiveCommentMapper liveCommentMapper;

    @Autowired
    LiveCommentService(RedisTemplate<String, String> redisTemplate, LiveCommentMapper liveCommentMapper){
        this.redisTemplate = redisTemplate;
        this.liveCommentMapper = liveCommentMapper;
    }

    public void addLiveCommentToDB(LiveComment liveComment) {
        liveCommentMapper.insertLiveComment(liveComment);
    }

    //TODO: concurrency problem???
    public void addLiveCommentToRedis(LiveComment liveComment) {
        String redisKey = LIVE_COMMENT_BASE_KEY + liveComment.getVideoId();
        String redisVal = redisTemplate.opsForValue().get(redisKey);
        List<LiveComment> liveCommentList = new ArrayList<>();
        if(redisVal != null && !redisVal.equals("")){
            liveCommentList = JSONArray.parseArray(redisVal, LiveComment.class);
        }
        liveCommentList.add(liveComment);
        redisTemplate.opsForValue().set(redisKey, JSONObject.toJSONString(liveCommentList));
    }

    public List<LiveComment> getLiveCommentList(Long videoId, String startTimeStr, String endTimeStr) throws ParseException {
        String redisKey = LIVE_COMMENT_BASE_KEY + videoId;
        String redisVal = redisTemplate.opsForValue().get(redisKey);
        List<LiveComment> resList;
        if(!StringUtil.isNullOrEmpty(redisVal)){// fetch from the redis
            resList = JSONArray.parseArray(redisVal, LiveComment.class);
            if(!StringUtil.isNullOrEmpty(startTimeStr)
                    && !StringUtil.isNullOrEmpty(endTimeStr)){
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//                LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
//                LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);
                List<LiveComment> filteredList = new ArrayList<>();
                for(LiveComment liveComment : resList){
//                    LocalDateTime createdTime = liveComment.getCreatedTime();
                    long appearingTime = Long.parseLong(liveComment.getAppearingTime());
//                    if (createdTime.isAfter(startTime) && createdTime.isBefore(endTime)) {
                    if(appearingTime >= Long.parseLong(startTimeStr) && appearingTime <= Long.parseLong(endTimeStr)){
                        filteredList.add(liveComment);
                    }
                }
                resList = filteredList;
            }
        } else {
            Map<String, Object> liveCommentParams = new HashMap<>();
            liveCommentParams.put("videoId", videoId);
            liveCommentParams.put("startTime", startTimeStr);
            liveCommentParams.put("endTime", endTimeStr);
            resList = liveCommentMapper.getLiveCommentList(liveCommentParams);
            // save the live comments into the redis
            redisTemplate.opsForValue().set(redisKey, JSONObject.toJSONString(resList));
        }
        return resList;
    }
}
