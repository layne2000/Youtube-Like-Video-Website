package org.example.mapper.auth;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.entity.auth.AuthRole;

@Mapper
public interface AuthRoleMapper {

    @Select("SELECT * FROM t_auth_role WHERE code = #{code}")
    AuthRole getAuthRoleByCode(String code);
}
