package org.example;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.annotations.Select;
import org.example.entity.Video;
import org.example.entity.VideoTag;
import org.example.exception.CustomizedException;
import org.example.mapper.VideoMapper;
import org.example.mapper.VideoTagMapper;
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
    @Autowired
    VideoService(VideoMapper videoMapper, VideoTagMapper videoTagMapper, FastDFSUtil fastDFSUtil){
        this.videoMapper = videoMapper;
        this.videoTagMapper = videoTagMapper;
        this.fastDFSUtil = fastDFSUtil;
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
}
