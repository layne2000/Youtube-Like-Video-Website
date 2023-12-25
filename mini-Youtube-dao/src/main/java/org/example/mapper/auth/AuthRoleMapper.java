package org.example.mapper.auth;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.entity.auth.AuthRole;

import java.time.LocalDateTime;

@Mapper
public interface AuthRoleMapper {

    @Select("SELECT * FROM t_auth_role WHERE code = #{code}")
    AuthRole getAuthRoleByCode(String code);

    @Insert("INSERT INTO t_auth_role WHERE name = #{name}, code = #{code}, createdTime = #{createdTime}")
    void addAuthRole(String name, String code, LocalDateTime createdTime);
}
