package org.example;

import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import org.example.entity.User;
import org.example.entity.UserInfo;
import org.example.util.JsonResponse;
import org.example.util.PageResult;
import org.example.util.RSAUtil;
import org.example.util.UserSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    private final UserService userService;
    private final UserFollowingService userFollowingService;
    private final UserSupport userSupport;

    @Autowired
    public UserController(UserService userService, UserFollowingService userFollowingService, UserSupport userSupport){
        this.userService = userService;
        this.userFollowingService = userFollowingService;
        this.userSupport = userSupport;
    }

    @GetMapping("/rsa-pks")
    public JsonResponse<String> getRsaPublicKey(){
        String pk = RSAUtil.getPublicKeyStr();
        return new JsonResponse<>(pk);
    }

    @GetMapping("/users")
    public JsonResponse<User> getUserInfo(){
        // get from JWT which is carried in the request
        Long userId = userSupport.getCurrentUserId();
        User user = userService.getUserById(userId);
        return new JsonResponse<>(user);
    }

    @PostMapping("/users")
    public JsonResponse<String> addUser(@RequestBody User user){
        userService.addUser(user);
        return JsonResponse.success();
    }

    @PutMapping("/users")
    public JsonResponse<String> updateUser(@RequestBody User user){
        Long userId = userSupport.getCurrentUserId();
        user.setId(userId);
        userService.updateUser(user);
        return JsonResponse.success();
    }

    @PutMapping("/user-infos")
    public JsonResponse<String> updateUserInfo(@RequestBody UserInfo userInfo){
        Long userId = userSupport.getCurrentUserId();
        userInfo.setUserId(userId);
        userService.updateUserInfo(userInfo);
        return JsonResponse.success();
    }

    // search the userInfo that has the keyword of nickname and mark (set the followed field as true)
    // those that is followed by the current user and then return
    @GetMapping("/user-infos")
    public JsonResponse<PageResult<UserInfo>> pageListUserInfos(@RequestParam(name = "no") Integer pageNum, @RequestParam(name = "size") Integer pageSize, String nick){
        Long userId = userSupport.getCurrentUserId();
        JSONObject params = new JSONObject();
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        params.put("nickname", nick);
        params.put("userId", userId);
        PageResult<UserInfo> result = userService.pageListUserInfo(params);
        if(result.getTotal() > 0){
            List<UserInfo> checkedUserInfoList = userFollowingService.addUserInfoFollowedStatus(result.getList(), userId);
            result.setList(checkedUserInfoList);
        }
        return new JsonResponse<>(result);
    }

    @PostMapping("/user-tokens")
    public JsonResponse<String> login(@RequestBody User user) throws Exception {
        String token = userService.login(user);
        return new JsonResponse<>(token);
    }

    // dts means double tokens
    @PostMapping("/user-dts")
    public JsonResponse<Map<String, Object>> loginForDts(@RequestBody User user) throws Exception {
        Map<String, Object> map = userService.loginForDts(user);
        return new JsonResponse<>(map);
    }

    @DeleteMapping("/refresh-tokens")
    public JsonResponse<String> logout(HttpServletRequest request){
        String refreshToken = request.getHeader("refreshToken");
        Long userId = userSupport.getCurrentUserId();
        userService.logout(userId, refreshToken);
        return JsonResponse.success();
    }

    @PostMapping("/access-tokens")
    public JsonResponse<String> refreshAccessToken(HttpServletRequest request) throws Exception {
        String refreshToken = request.getHeader("refreshToken");
        String accessToken = userService.refreshAccessToken(refreshToken);
        return new JsonResponse<>(accessToken);
    }

}
