package org.example.util;

import jakarta.servlet.http.HttpServletRequest;
import org.example.UserService;
import org.example.exception.CustomizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class UserSupport {
    private final UserService userService;

    @Autowired
    public UserSupport(UserService userService){
        this.userService = userService;
    }

    public Long getCurrentUserId() {
        // RequestContextHolder is embedded in spring mvc
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(requestAttributes == null){
            throw new CustomizedException("RequestAttributes is null!");
        }
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader("token");
        Long userId = TokenUtil.verifyToken(token);
        if(userId < 0) {
            throw new CustomizedException("Illegal user");
        }
        return userId;
    }
}
