package org.example.auth;

import org.example.mapper.auth.UserRoleAssociationMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleAssociationService {
    private final UserRoleAssociationMapper userRoleAssociationMapper;
    public UserRoleAssociationService(UserRoleAssociationMapper userRoleAssociationMapper){
        this.userRoleAssociationMapper = userRoleAssociationMapper;
    }

    public List<String> getRoleCodeListByUserId(Long userId) {
        return userRoleAssociationMapper.getRoleCodeListByUserId(userId);
    }
}
