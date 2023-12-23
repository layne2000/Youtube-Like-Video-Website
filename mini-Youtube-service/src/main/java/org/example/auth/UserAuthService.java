package org.example.auth;

import org.example.constant.AuthRoleConstant;
import org.example.entity.auth.*;
import org.example.mapper.auth.ElementOperationRoleAssociationMapper;
import org.example.mapper.auth.MenuRoleAssociationMapper;
import org.example.mapper.auth.AuthRoleMapper;
import org.example.mapper.auth.UserRoleAssociationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserAuthService {
    private final UserRoleAssociationMapper userRoleAssociationMapper;
    private final ElementOperationRoleAssociationMapper elementOperationRoleAssociationMapper;
    private final MenuRoleAssociationMapper menuRoleAssociationMapper;
    private final AuthRoleMapper authRoleMapper;

    @Autowired
    public UserAuthService(UserRoleAssociationMapper userRoleAssociationMapper,
                           ElementOperationRoleAssociationMapper elementOperationRoleAssociationMapper,
                           MenuRoleAssociationMapper menuRoleAssociationMapper, AuthRoleMapper authRoleMapper) {
        this.userRoleAssociationMapper = userRoleAssociationMapper;
        this.elementOperationRoleAssociationMapper = elementOperationRoleAssociationMapper;
        this.menuRoleAssociationMapper = menuRoleAssociationMapper;
        this.authRoleMapper = authRoleMapper;
    }

    public UserAuthority getUserAuthority(Long userId) {
        List<UserRoleAssociation> userRoleAssociationList = userRoleAssociationMapper.getUserRoleAssociationByUserId(userId);
        List<ElementOperationRoleAssociation> elementOperationRoleAssociationListList = elementOperationRoleAssociationMapper.getListByUserRoleAssociationList(userRoleAssociationList);
        List<MenuRoleAssociation> menuRoleAssociationListList = menuRoleAssociationMapper.getListByUserRoleAssociationList(userRoleAssociationList);
        UserAuthority userAuthority = new UserAuthority();
        userAuthority.setElementOperationRoleAssociationList(elementOperationRoleAssociationListList);
        userAuthority.setMenuRoleAssociationList(menuRoleAssociationListList);
        return userAuthority;
    }

    public void addUserDefaultRole(Long userId) {
        UserRoleAssociation userRole = new UserRoleAssociation();
        //TODO: when to add auth role?
        AuthRole role = authRoleMapper.getAuthRoleByCode(AuthRoleConstant.ROLE_LV0);
        userRole.setUserId(userId);
        userRole.setRoleId(role.getId());
        userRole.setCreatedTime(LocalDateTime.now());
        userRoleAssociationMapper.addUserRole(userRole);
    }

}
