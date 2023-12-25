package org.example.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.example.auth.UserRoleAssociationService;
import org.example.constant.AuthRoleConstant;
import org.example.entity.UserMoment;
import org.example.exception.CustomizedException;
import org.example.util.UserSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Order(2)
@Component
@Aspect
public class DataLimitedAspect {
    private final UserSupport userSupport;
    private final UserRoleAssociationService userRoleAssociationService;

    @Autowired
    public DataLimitedAspect(UserSupport userSupport,
                             UserRoleAssociationService userRoleAssociationService){
        this.userSupport = userSupport;
        this.userRoleAssociationService = userRoleAssociationService;
    }

    @Pointcut("@annotation(org.example.entity.annotation.DataLimited)")
    public void check(){
    }

    @Before("check()")
    public void doBefore(JoinPoint joinPoint){
        Long userId = userSupport.getCurrentUserId();
        List<String> userRoleCodeList = userRoleAssociationService.getRoleCodeListByUserId(userId);
        Set<String> roleCodeSet = new HashSet<>(userRoleCodeList);
        Object[] args = joinPoint.getArgs(); // annotated method arguments
        for(Object arg : args){
            if(arg instanceof UserMoment){
                UserMoment userMoment = (UserMoment)arg;
                String type = userMoment.getType();
                if(roleCodeSet.contains(AuthRoleConstant.ROLE_LV1) && !"0".equals(type)){
                    throw new CustomizedException("Invalid parameter");
                }
            }
        }
    }
}
