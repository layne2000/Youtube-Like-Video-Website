package org.example;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.entity.Video;
import org.example.entity.VideoCoin;
import org.example.entity.VideoCollection;
import org.example.util.JsonResponse;
import org.example.util.PageResult;
import org.example.util.UserSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class VideoController {
    private final VideoService videoService;

    private final UserSupport userSupport;

    @Autowired
    VideoController(VideoService videoService, UserSupport userSupport){
        this.videoService = videoService;
        this.userSupport = userSupport;
    }

    // TODO: save the video in file server by frontend?
    @PostMapping("/videos")
    public JsonResponse<String> addVideo(@RequestBody Video video){
        Long userId = userSupport.getCurrentUserId();
        video.setUserId(userId);
        videoService.addVideo(video);
        //TODO
//        //在es中添加一条视频数据
//        elasticSearchService.addVideo(video);
        return JsonResponse.success();
    }

    @GetMapping("/videos")
    public JsonResponse<PageResult<Video>> pageListVideos(Integer size, Integer num, String section){
        PageResult<Video> result = videoService.pageListVideos(size, num, section);
        return new JsonResponse<>(result);
    }

    @GetMapping("/video-slices")
    public void viewVideoOnlineBySlices(HttpServletRequest request,
                                        HttpServletResponse response,
                                        String url) {
        videoService.viewVideoOnlineBySlices(request, response, url);
    }

    @PostMapping("/video-likes")
    public JsonResponse<String> addVideoLike(@RequestParam Long videoId){
        Long userId = userSupport.getCurrentUserId();
        videoService.insertVideoLike(videoId, userId);
        return JsonResponse.success();
    }

    @DeleteMapping("/video-likes")
    public JsonResponse<String> cancelVideoLike(@RequestParam Long videoId){
        Long userId = userSupport.getCurrentUserId();
        videoService.deleteVideoLike(videoId, userId);
        return JsonResponse.success();
    }

    // return video like count and whether this user has liked the video
    @GetMapping("/video-likes")
    public JsonResponse<Map<String, Object>> getVideoLikes(@RequestParam Long videoId){
        Long userId = null;
        try{
            userId = userSupport.getCurrentUserId();
        }catch (Exception ignored){} // allow non-user visitor to see the like num
        Map<String, Object> result = videoService.getVideoLikes(videoId, userId);
        return new JsonResponse<>(result);
    }

    @PostMapping("/video-collections")
    public JsonResponse<String> addVideoCollection(@RequestBody VideoCollection videoCollection){
        Long userId = userSupport.getCurrentUserId();
        videoService.insertVideoCollection(videoCollection, userId);
        return JsonResponse.success();
    }

    @DeleteMapping("/video-collections")
    public JsonResponse<String> deleteVideoCollection(@RequestParam Long videoId){
        Long userId = userSupport.getCurrentUserId();
        videoService.deleteVideoCollection(videoId, userId);
        return JsonResponse.success();
    }

    @GetMapping("/video-collections")
    public JsonResponse<Map<String, Object>> getVideoCollections(@RequestParam Long videoId){
        Long userId = null;
        try{
            userId = userSupport.getCurrentUserId();
        }catch (Exception ignored){}
        Map<String, Object> result = videoService.getVideoCollections(videoId, userId);
        return new JsonResponse<>(result);
    }

    // allow giving more than 1 coin
    @PostMapping("/video-coins")
    public JsonResponse<String> addVideoCoin(@RequestBody VideoCoin videoCoin){
        Long userId = userSupport.getCurrentUserId();
        videoService.insertVideoCoin(videoCoin, userId);
        return JsonResponse.success();
    }

    @GetMapping("/video-coins")
    public JsonResponse<Map<String, Object>> getVideoCoins(@RequestParam Long videoId){
        Long userId = null;
        try{
            userId = userSupport.getCurrentUserId();
        }catch (Exception ignored){}
        Map<String, Object> result = videoService.getVideoCoins(videoId, userId);
        return new JsonResponse<>(result);
    }

}
