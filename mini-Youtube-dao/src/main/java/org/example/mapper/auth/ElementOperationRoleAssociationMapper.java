package org.example.mapper.auth;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.entity.auth.AuthElementOperation;
import org.example.entity.auth.ElementOperationRoleAssociation;
import org.example.entity.auth.UserRoleAssociation;

import java.util.List;

@Mapper
public interface ElementOperationRoleAssociationMapper {
    @Select("<script>" +
            "SELECT ea.*, eo.* " +
            "FROM t_auth_element_operation AS eo LEFT JOIN t_element_operation_role_association AS ea " +
            "ON eo.id = ea.elementOperationId " +
            "WHERE roleId IN " +
            "<foreach item='userRoleAssociation' collection='userRoleAssociationList' open='(' separator=',' close=')'>" +
            "#{userRoleAssociation.roleId}" +
            "</foreach>" +
            "</script>")
    List<ElementOperationRoleAssociation> getListByUserRoleAssociationList(List<UserRoleAssociation> userRoleAssociationList);
}
