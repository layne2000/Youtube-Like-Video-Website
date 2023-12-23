package org.example;

import org.example.entity.UserMoment;
import org.example.util.JsonResponse;
import org.example.util.UserSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserMomentController {
    private final UserSupport userSupport;
    private final UserMomentService userMomentService;

    @Autowired
    public UserMomentController(UserSupport userSupport, UserMomentService userMomentService){
        this.userSupport = userSupport;
        this.userMomentService = userMomentService;
    }

    //TODO: more annotations needed
    @PostMapping("/user-moments")
    public JsonResponse<String> addUserMoment(@RequestBody UserMoment userMoment) throws Exception {
        Long userId = userSupport.getCurrentUserId();
        userMoment.setUserId(userId);
        userMomentService.addUserMoment(userMoment);
        return JsonResponse.success();
    }

    @GetMapping("/user-subscribed-moments")
    public JsonResponse<List<UserMoment>> getUserSubscribedMoments(){
        Long userId = userSupport.getCurrentUserId();
        List<UserMoment> list = userMomentService.getUserSubscribedMoments(userId);
        return new JsonResponse<>(list);
    }

}
