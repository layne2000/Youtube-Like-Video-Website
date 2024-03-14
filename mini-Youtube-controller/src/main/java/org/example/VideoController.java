package org.example;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.entity.*;
import org.example.util.JsonResponse;
import org.example.util.PageResult;
import org.example.util.UserSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class VideoController {
    private final VideoService videoService;

    private final UserSupport userSupport;

    private final ElasticSearchService elasticSearchService;

    @Autowired
    VideoController(VideoService videoService, UserSupport userSupport, 
                    ElasticSearchService elasticSearchService){
        this.videoService = videoService;
        this.userSupport = userSupport;
        this.elasticSearchService = elasticSearchService;
    }

    // TODO: save the video in file server by frontend?
    @PostMapping("/videos")
    public JsonResponse<String> addVideo(@RequestBody Video video){
        Long userId = userSupport.getCurrentUserId();
        video.setUserId(userId);
        videoService.addVideo(video);
        elasticSearchService.addVideo(video);
        return JsonResponse.success();
    }

    @GetMapping("/videos")
    public JsonResponse<PageResult<Video>> pageListVideos(@RequestParam Integer size, @RequestParam Integer num, String section){
        PageResult<Video> result = videoService.pageListVideos(size, num, section);
        return new JsonResponse<>(result);
    }

    //TODO: to be tested
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

    //TODO: add CRUD for collection group and corresponding check in videoCollection api

    // multiple insertion would lead to deletion and then insertion
    // (user can collect the same video in only one group
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

    //TODO:add CRUD for userCoin

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

    // root video comment's rootCommentId should be NULL
    @PostMapping("/video-comments")
    public JsonResponse<String> addVideoComment(@RequestBody VideoComment videoComment){
        Long userId = userSupport.getCurrentUserId();
        videoService.insertVideoComment(videoComment, userId);
        return JsonResponse.success();
    }

    @GetMapping("/video-comments")
    public JsonResponse<PageResult<VideoComment>> pageListVideoComments(@RequestParam Integer size,
                                                                        @RequestParam Integer num,
                                                                        @RequestParam Long videoId){
        PageResult<VideoComment> result = videoService.pageListVideoComments(size, num, videoId);
        return new JsonResponse<>(result);
    }

    @GetMapping("/video-details")
    public JsonResponse<Map<String, Object>> getVideoDetails(@RequestParam Long videoId){
        Map<String, Object> result = videoService.getVideoDetails(videoId);
        return new JsonResponse<>(result);
    }

    //TODO: verify the videoId?
    @PostMapping("/video-views")
    public JsonResponse<String> addVideoView(@RequestBody VideoView videoView,
                                             HttpServletRequest request){
        Long userId;
        try{
            userId = userSupport.getCurrentUserId();
            videoView.setUserId(userId);
            videoService.addVideoView(videoView, request);
        }catch (Exception e){
            videoService.addVideoView(videoView, request);
        }
        return JsonResponse.success();
    }

    @GetMapping("/video-view-counts")
    public JsonResponse<Integer> getCountByVideoId(@RequestParam Long videoId){
        Integer count = videoService.getCountByVideoId(videoId);
        return new JsonResponse<>(count);
    }


    @GetMapping("/video-tags")
    public JsonResponse<List<VideoTagAssociation>> getVideoTagsByVideoId(@RequestParam Long videoId) {
        List<VideoTagAssociation> list = videoService.getVideoTagListByVideoId(videoId);
        return new JsonResponse<>(list);
    }

    @DeleteMapping("/video-tags")
    public JsonResponse<String> deleteVideoTags(@RequestBody JSONObject params) {
        String tagIdList = params.getString("tagIdList");
        Long videoId = params.getLong("videoId");
        videoService.deleteVideoTagsByTagIdList(JSONArray.parseArray(tagIdList).toJavaList(Long.class), videoId);
        return JsonResponse.success();
    }
}
