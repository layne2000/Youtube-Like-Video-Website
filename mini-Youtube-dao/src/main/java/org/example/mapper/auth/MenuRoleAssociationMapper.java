package org.example.mapper.auth;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.entity.auth.AuthMenu;
import org.example.entity.auth.MenuRoleAssociation;
import org.example.entity.auth.UserRoleAssociation;

import java.util.List;

@Mapper
public interface MenuRoleAssociationMapper {
    @Select("<script>" +
            "SELECT am.*, ma.* " +
            "FROM t_auth_menu AS am LEFT JOIN t_menu_role_association AS ma ON am.id = ma.menuId " +
            "WHERE roleId IN " +
            "<foreach item='userRoleAssociation' collection='userRoleAssociationList' open='(' separator=',' close=')'>" +
            "#{userRoleAssociation.roleId}" +
            "</foreach>" +
            "</script>")
    List<MenuRoleAssociation> getListByUserRoleAssociationList(List<UserRoleAssociation> userRoleAssociationList);
}
