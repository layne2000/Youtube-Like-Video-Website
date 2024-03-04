package org.example;

import org.example.auth.UserAuthService;
import org.example.entity.auth.UserAuthority;
import org.example.util.JsonResponse;
import org.example.util.UserSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserAuthController {

    private final UserSupport userSupport;
    private final UserAuthService userAuthService;
    @Autowired
    public UserAuthController(UserSupport userSupport, UserAuthService userAuthService){
        this.userSupport = userSupport;
        this.userAuthService = userAuthService;
    }

    @GetMapping("/user-authorities")
    public JsonResponse<UserAuthority> getUserAuthority(){
        Long userId = userSupport.getCurrentUserId();
        UserAuthority userAuthorities = userAuthService.getUserAuthority(userId);
        return new JsonResponse<>(userAuthorities);
    }
}
