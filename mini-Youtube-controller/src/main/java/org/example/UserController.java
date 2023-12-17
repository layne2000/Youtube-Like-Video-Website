package org.example;

import org.example.entity.User;
import org.example.entity.UserInfo;
import org.example.exception.CustomizedException;
import org.example.util.JsonResponse;
import org.example.util.RSAUtil;
import org.example.util.UserSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private final UserService userService;

    private final UserSupport userSupport;

    @Autowired
    public UserController(UserService userService, UserSupport userSupport){
        this.userService = userService;
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

    @PostMapping("/user-tokens")
    public JsonResponse<String> login(@RequestBody User user) throws Exception {
        String token = userService.login(user);
        return new JsonResponse<>(token);
    }

}
