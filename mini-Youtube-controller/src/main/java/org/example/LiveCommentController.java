package org.example;

import org.example.entity.LiveComment;
import org.example.util.JsonResponse;
import org.example.util.UserSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LiveCommentController {
    private final UserSupport userSupport;

    private final LiveCommentService liveCommentService;

    @Autowired
    LiveCommentController(UserSupport userSupport, LiveCommentService liveCommentService){
        this.userSupport = userSupport;
        this.liveCommentService = liveCommentService;
    }

    @GetMapping("/live-comments")
    public JsonResponse<List<LiveComment>> getLiveComments(@RequestParam Long videoId,
                                                     String startTime,
                                                     String endTime) throws Exception {
        List<LiveComment> resList;
        try{
            userSupport.getCurrentUserId();
            resList = liveCommentService.getLiveCommentList(videoId, startTime, endTime);
        }catch(Exception e){ // visitor with no userId
            resList = liveCommentService.getLiveCommentList(videoId, null, null);
        }
        return new JsonResponse<>(resList);
    }
}
