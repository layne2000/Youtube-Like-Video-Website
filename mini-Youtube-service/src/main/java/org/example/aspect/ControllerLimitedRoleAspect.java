package org.example.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.example.auth.UserRoleAssociationService;
import org.example.entity.annotation.ControllerLimitedRole;
import org.example.entity.auth.UserRoleAssociation;
import org.example.exception.CustomizedException;
import org.example.util.UserSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Order(1)
@Component
@Aspect
public class ControllerLimitedRoleAspect {
    private final UserSupport userSupport;

    private final UserRoleAssociationService userRoleAssociationService;

    @Autowired
    public ControllerLimitedRoleAspect(UserSupport userSupport,
                                       UserRoleAssociationService userRoleAssociationService) {
        this.userSupport = userSupport;
        this.userRoleAssociationService = userRoleAssociationService;
    }

    @Pointcut("@annotation(org.example.entity.annotation.ControllerLimitedRole)")
    public void check() {
    }

    // && @annotation(controllerLimitedRole) allows to get info about the annotation
    @Before("check() && @annotation(controllerLimitedRole)")
    public void doBefore(JoinPoint joinPoint, ControllerLimitedRole controllerLimitedRole) {
        Long userId = userSupport.getCurrentUserId();
        List<String> userRoleCodeList = userRoleAssociationService.getRoleCodeListByUserId(userId);
        String[] limitedRoleCodeList = controllerLimitedRole.limitedRoleCodeArray();
        Set<String> limitedRoleCodeSet = new HashSet<>(Arrays.asList(limitedRoleCodeList));
        Set<String> roleCodeSet = new HashSet<>(userRoleCodeList);
        roleCodeSet.retainAll(limitedRoleCodeSet);
        if (roleCodeSet.size() > 0) {
            throw new CustomizedException("Permission denied");
        }
    }
}
