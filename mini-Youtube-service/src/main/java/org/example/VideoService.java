package org.example;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.annotations.Select;
import org.example.entity.*;
import org.example.exception.CustomizedException;
import org.example.mapper.*;
import org.example.util.FastDFSUtil;
import org.example.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VideoService {
    private final VideoMapper videoMapper;
    private final VideoTagMapper videoTagMapper;
    private final FastDFSUtil fastDFSUtil;
    private final VideoLikeMapper videoLikeMapper;
    private final VideoCollectionMapper videoCollectionMapper;
    private final UserCoinMapper userCoinMapper;
    private final VideoCoinMapper videoCoinMapper;
    @Autowired
    VideoService(VideoMapper videoMapper, VideoTagMapper videoTagMapper,
                 FastDFSUtil fastDFSUtil, VideoLikeMapper videoLikeMapper,
                 VideoCollectionMapper videoCollectionMapper, UserCoinMapper userCoinMapper,
                 VideoCoinMapper videoCoinMapper){
        this.videoMapper = videoMapper;
        this.videoTagMapper = videoTagMapper;
        this.fastDFSUtil = fastDFSUtil;
        this.videoLikeMapper = videoLikeMapper;
        this.videoCollectionMapper = videoCollectionMapper;
        this.userCoinMapper = userCoinMapper;
        this.videoCoinMapper = videoCoinMapper;
    }

    @Transactional
    public void addVideo(Video video) {
        video.setCreatedTime(LocalDateTime.now());
        videoMapper.insertVideo(video);
        List<VideoTag> videoTagList = video.getVideoTagList();
        videoTagList.forEach(item -> {
            item.setCreatedTime(LocalDateTime.now());
            item.setVideoId(video.getId());
        });
        videoTagMapper.addVideoTagList(videoTagList);
    }

    public PageResult<Video> pageListVideos(Integer size, Integer num, String section) {
        if(size == null || num == null){
            throw new CustomizedException("Invalid parameter!");
        }
        Map<String, Object> mapperParams = new HashMap<>();
        mapperParams .put("start", (num - 1) * size);
        mapperParams .put("limit", size);
        mapperParams .put("section", section);
        Integer total = videoMapper.countVideosBySection(section);
        List<Video> videoList = new ArrayList<>();
        if(total > 0){
            videoList = videoMapper.pageListVideos(mapperParams);
        }
        return new PageResult<>(total, videoList);
    }

    public void viewVideoOnlineBySlices(HttpServletRequest request, HttpServletResponse response, String url) {
        try{
            fastDFSUtil.viewVideoOnlineBySlices(request, response, url);
        }catch(Exception ignored){}
    }

    @Transactional
    public void insertVideoLike(Long videoId, Long userId) {
        Video video = videoMapper.getVideoById(videoId);
        if(video == null){
            throw new CustomizedException("Invalid video!");
        }
        VideoLike videoLike = videoLikeMapper.getVideoLikeByUserIdAndVideoId(userId, videoId);
        if(videoLike != null){
            throw new CustomizedException("Already liked!");
        }
        VideoLike videoLike1 = new VideoLike(userId, videoId, LocalDateTime.now());
        videoLikeMapper.insertVideoLike(videoLike1);
    }

    // no need to check like the above func because deletion what doesn't exist is not harmful
    public void deleteVideoLike(Long videoId, Long userId) {
        videoLikeMapper.deleteVideoLike(userId, videoId);
    }

    public Map<String, Object> getVideoLikes(Long videoId, Long userId) {
        Map<String, Object> res = new HashMap<>();
        Long count = videoLikeMapper.getVideoLikeNum(videoId);
        boolean isLiked = (videoLikeMapper.getVideoLikeByUserIdAndVideoId(userId, videoId) != null);
        res.put("count", count);
        res.put("like", isLiked);
        return res;
    }

    @Transactional
    public void insertVideoCollection(VideoCollection videoCollection, Long userId) {
        Long videoId = videoCollection.getVideoId();
        Long groupId = videoCollection.getGroupId();
        if(videoId == null || groupId == null){
            throw new CustomizedException("Invalid parameters!");
        }
        if(videoMapper.getVideoById(videoId) == null){
            throw new CustomizedException("Invalid video!");
        }
        videoCollectionMapper.deleteVideoCollectionByVideoIdAndUserId(videoId, userId);
        videoCollection.setUserId(userId);
        videoCollection.setCreatedTime(LocalDateTime.now());
        videoCollectionMapper.insertVideoCollection(videoCollection);
    }

    public void deleteVideoCollection(Long videoId, Long userId) {
        videoCollectionMapper.deleteVideoCollectionByVideoIdAndUserId(videoId, userId);
    }

    public Map<String, Object> getVideoCollections(Long videoId, Long userId) {
        Map<String, Object> res = new HashMap<>();
        Long count = videoCollectionMapper.getVideoCollectionNum(videoId);
        boolean isCollected = (videoCollectionMapper.getVideoCollectionByUserIdAndVideoId(userId, videoId) != null);
        res.put("count", count);
        res.put("collected", isCollected);
        return res;
    }

    @Transactional
    public void insertVideoCoin(VideoCoin videoCoin, Long userId) {
        Long videoId = videoCoin.getVideoId();
        if(videoId == null){
            throw new CustomizedException("Invalid params!");
        }
        if(videoMapper.getVideoById(videoId) == null){
            throw new CustomizedException("Invalid video");
        }
        // check if use has enough coins
        Long videoCoinAmount = videoCoin.getAmount(); // coin num given by this user
        Long userCoinAmount = userCoinMapper.getAmountByUserId(userId);
        if(userCoinAmount < videoCoinAmount){
            throw new CustomizedException("Not enough coins");
        }
        VideoCoin dbVideoCoin = videoCoinMapper.getVideoCoinByVideoIdAndUserId(videoId, userId);
        // TODO: totally different?
        if(dbVideoCoin == null){ // user haven't given this video coin before
            dbVideoCoin.setUserId(userId);
            dbVideoCoin.setVideoId(videoId);
            dbVideoCoin.setAmount(videoCoinAmount);
            dbVideoCoin.setCreatedTime(LocalDateTime.now());
            videoCoinMapper.insertVideoCoin(dbVideoCoin);
        } else {
            dbVideoCoin.setAmount(dbVideoCoin.getAmount() + videoCoinAmount);
            dbVideoCoin.setUpdatedTime(LocalDateTime.now());
            videoCoinMapper.updateAmountAndTime(dbVideoCoin);
        }
        userCoinMapper.updateAmountByUserId(userId, userCoinAmount - videoCoinAmount);
    }

    public Map<String, Object> getVideoCoins(Long videoId, Long userId) {
        Long count = videoCoinMapper.getVideoCoinTotalAmount(videoId);
        boolean hasGivenCoin = (videoCoinMapper.getVideoCoinByVideoIdAndUserId(videoId, userId) != null);
        Map<String, Object> res = new HashMap<>();
        res.put("count", count);
        res.put("hasGivenCoin", hasGivenCoin);
        return res;
    }
}
