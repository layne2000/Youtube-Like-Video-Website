package org.example;

import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.entity.*;
import org.example.exception.CustomizedException;
import org.example.mapper.*;
import org.example.util.FastDFSUtil;
import org.example.util.IpUtil;
import org.example.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VideoService {
    private final VideoMapper videoMapper;
    private final VideoTagMapper videoTagMapper;
    private final FastDFSUtil fastDFSUtil;
    private final VideoLikeMapper videoLikeMapper;
    private final VideoCollectionMapper videoCollectionMapper;
    private final UserCoinMapper userCoinMapper;
    private final VideoCoinMapper videoCoinMapper;
    private final VideoCommentMapper videoCommentMapper;
    private final UserInfoMapper userInfoMapper;
    private final VideoViewMapper videoViewMapper;
    @Autowired
    VideoService(VideoMapper videoMapper, VideoTagMapper videoTagMapper,
                 FastDFSUtil fastDFSUtil, VideoLikeMapper videoLikeMapper,
                 VideoCollectionMapper videoCollectionMapper, UserCoinMapper userCoinMapper,
                 VideoCoinMapper videoCoinMapper, VideoCommentMapper videoCommentMapper,
                 UserInfoMapper userInfoMapper, VideoViewMapper videoViewMapper){
        this.videoMapper = videoMapper;
        this.videoTagMapper = videoTagMapper;
        this.fastDFSUtil = fastDFSUtil;
        this.videoLikeMapper = videoLikeMapper;
        this.videoCollectionMapper = videoCollectionMapper;
        this.userCoinMapper = userCoinMapper;
        this.videoCoinMapper = videoCoinMapper;
        this.videoCommentMapper = videoCommentMapper;
        this.userInfoMapper = userInfoMapper;
        this.videoViewMapper = videoViewMapper;
    }

    @Transactional
    public void addVideo(Video video) {
        video.setCreatedTime(LocalDateTime.now());
        videoMapper.insertVideo(video);
        List<VideoTagAssociation> videoTagAssociationList = video.getVideoTagList();
        if(videoTagAssociationList != null){
            videoTagAssociationList.forEach(item -> {
                item.setCreatedTime(LocalDateTime.now());
                item.setVideoId(video.getId());
            });
            videoTagMapper.addVideoTagList(videoTagAssociationList);
        }
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
        if(userCoinAmount == null || userCoinAmount < videoCoinAmount){
            throw new CustomizedException("Not enough coins");
        }
        VideoCoin dbVideoCoin = videoCoinMapper.getVideoCoinByVideoIdAndUserId(videoId, userId);
        if(dbVideoCoin == null){ // user haven't given this video coin before
            dbVideoCoin = new VideoCoin();
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

    public void insertVideoComment(VideoComment videoComment, Long userId) {
        Long videoId = videoComment.getVideoId();
        if(videoId == null){
            throw new CustomizedException("Invalid param");
        }
        if(videoMapper.getVideoById(videoId) == null){
            throw new CustomizedException("Invalid video");
        }
        videoComment.setUserId(userId);
        videoComment.setCreatedTime(LocalDateTime.now());
        videoCommentMapper.insertVideoComment(videoComment);
    }

    public PageResult<VideoComment> pageListVideoComments(Integer size, Integer num, Long videoId) {
        if(videoId == null){
            throw new CustomizedException("Invalid param");
        }
        if(videoMapper.getVideoById(videoId) == null){
            throw new CustomizedException("Invalid video");
        }
        Map<String, Object> videoCommentParams = new HashMap<>();
        videoCommentParams.put("start", (num - 1) * size);
        videoCommentParams.put("limit", size);
        videoCommentParams.put("videoId", videoId);
        List<VideoComment> resList = videoCommentMapper.pageListRootVideoComments(videoCommentParams);
        Integer total = videoCommentMapper.getRootCommentNumByVideoId(videoId);
        if(resList.size() == 0){
            return new PageResult<>(total, resList);
        }

        List<Long> rootCommentIdList = resList.stream().map(VideoComment::getId).toList();
        // all roots' child comments including comment to child comment
        List<VideoComment> childCommentList;
        if(!rootCommentIdList.isEmpty()){
            childCommentList = videoCommentMapper.getVidoCommentListByRootCommentIdList(rootCommentIdList);
        } else {
            childCommentList = new ArrayList<>();
        }

        // to get userInfoMap for both root comments and child comments' user
        Set<Long> userIdSet = resList.stream().map(VideoComment::getUserId).collect(Collectors.toSet());
        userIdSet.addAll(childCommentList.stream().map(VideoComment::getUserId).collect(Collectors.toSet()));
        //TODO: no need to add its replyUser because all the users are here?
        List<UserInfo> userInfoList = userInfoMapper.getUserInfoListByUserIdSet(userIdSet);
        Map<Long, UserInfo> userInfoMap = userInfoList.stream().collect(Collectors.toMap(UserInfo::getUserId, userInfo -> userInfo));

        // fill in its childList, userInfo
        resList.forEach(videoComment -> {
            Long id = videoComment.getId();
            List<VideoComment> childList = new ArrayList<>();
            childCommentList.forEach(childComment ->{
                if(id.equals(childComment.getRootCommentId())){
                    childComment.setUserInfo(userInfoMap.get(childComment.getUserId()));
                    childComment.setReplyUserInfo(userInfoMap.get(childComment.getReplyUserId()));
                    childList.add(childComment);
                }
            });
            videoComment.setChildList(childList);
            videoComment.setUserInfo(userInfoMap.get(videoComment.getUserId()));
        });
        return new PageResult<>(total, resList);
    }

    public Map<String, Object> getVideoDetails(Long videoId) {
        if(videoId == null){
            throw new CustomizedException("Invalid parameter");
        }
        Video video = videoMapper.getVideoById(videoId);
        if(video == null){
            throw new CustomizedException("Invalid video");
        }
        UserInfo userInfo = userInfoMapper.getUserInfoByUserId(video.getUserId());
        Map<String, Object> res = new HashMap<>();
        res.put("video", video);
        res.put("userInfo", userInfo);
        return res;
    }

    public List<VideoTagAssociation> getVideoTagListByVideoId(Long videoId) {
        if(videoId == null){
            throw new CustomizedException("Invalid parameter");
        }
        Video video = videoMapper.getVideoById(videoId);
        if(video == null){
            throw new CustomizedException("Invalid video");
        }
        return videoTagMapper.getVideoTagListByVideoId(videoId);
    }

    public void deleteVideoTagsByTagIdList(List<Long> tagIdList, Long videoId) {
        if(videoId == null){
            throw new CustomizedException("Invalid parameter");
        }
        Video video = videoMapper.getVideoById(videoId);
        if(video == null){
            throw new CustomizedException("Invalid video");
        }
        videoTagMapper.deleteVideoTagsByTagIdList(tagIdList, videoId);
    }

    public void addVideoView(VideoView videoView, HttpServletRequest request) {
        Long userId = videoView.getUserId();
        Long videoId = videoView.getVideoId();
        Map<String, Object> params = new HashMap<>();
        // generate clientId based on browser and OS
        String clientId = String.valueOf(UserAgent.parseUserAgentString(request.getHeader("User-Agent")));
        String ip = IpUtil.getIP(request);
        if(userId != null){
            params.put("userId", userId);
        } else { // visitor mode
            params.put("ip", ip);
            params.put("clientId", clientId);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        params.put("today", LocalDateTime.now().format(formatter));
        params.put("videoId", videoId);
        // get the videoView today by userId or clientId and ip (visitor)
        VideoView dbVideoView = videoViewMapper.getVideoView(params);
        if(dbVideoView == null){
            videoView.setIp(ip);
            videoView.setClientId(clientId);
            videoView.setCreatedTime(LocalDateTime.now());
            videoViewMapper.insertVideoView(videoView);
        }
    }

    public Integer getVideoViewByVideoId(Long videoId) {
        return videoViewMapper.getCountByVideoId(videoId);
    }
}
