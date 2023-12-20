package org.example;

import org.example.entity.FollowingGroup;
import org.example.entity.UserFollowing;
import org.example.util.JsonResponse;
import org.example.util.UserSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserFollowingController {
    private UserSupport userSupport;
    private UserFollowingService userFollowingService;

    UserFollowingController(UserSupport userSupport, UserFollowingService userFollowingService){
        this.userSupport = userSupport;
        this.userFollowingService = userFollowingService;
    }

    // add customized following group
    @PostMapping("/user-following-groups")
    public JsonResponse<Long> addUserFollowingGroup(@RequestBody FollowingGroup followingGroup){
        Long userId = userSupport.getCurrentUserId();
        followingGroup.setUserId(userId);
        Long groupId = userFollowingService.addUserFollowingGroup(followingGroup);
        return new JsonResponse<>(groupId);
    }

    // get user's all following group without followingUserInfoList inside
    @GetMapping("/user-following-groups")
    public JsonResponse<List<FollowingGroup>> getUserFollowingGroups(){
        Long userId = userSupport.getCurrentUserId();
        List<FollowingGroup> list = userFollowingService.getUserFollowingGroupList(userId);
        return new JsonResponse<>(list);
    }

    @PostMapping("/user-followings")
    public JsonResponse<String> addUserFollowing(@RequestBody UserFollowing userFollowing){
        Long userId = userSupport.getCurrentUserId();
        userFollowing.setUserId(userId);
        userFollowingService.addUserFollowing(userFollowing);
        return JsonResponse.success();
    }

    // the first element in the returned list is the newly-created following group with all the user
    // followings, the remaining are different following group with different users
    @GetMapping("/user-followings")
    public JsonResponse<List<FollowingGroup>> getUserFollowing(){
        Long userId = userSupport.getCurrentUserId();
        List<FollowingGroup> result = userFollowingService.getUserFollowings(userId);
        return new JsonResponse<>(result);
    }

    // add followed status to the userFollowing whose followingId is the current userId and return
    @GetMapping("/user-fans")
    public JsonResponse<List<UserFollowing>> getUserFans(){
        Long userId = userSupport.getCurrentUserId();
        List<UserFollowing> result = userFollowingService.getUserFans(userId);
        return new JsonResponse<>(result);
    }

}
