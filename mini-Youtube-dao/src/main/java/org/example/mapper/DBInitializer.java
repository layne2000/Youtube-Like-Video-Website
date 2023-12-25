package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DBInitializer {

    @Update("DROP TABLE IF EXISTS t_user;")
    void dropUserTable();

    @Update("CREATE TABLE t_user ("
            + "id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
            + "phone varchar(100) NOT NULL, "
            + "email varchar(100) DEFAULT NULL, "
            + "password varchar(255) DEFAULT NULL, "
            + "salt varchar(50) DEFAULT NULL, "
            + "createdTime DATETIME DEFAULT NULL, "
            + "updatedTime DATETIME DEFAULT NULL"
            + ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;")
    void createUserTable();

    @Update("DROP TABLE IF EXISTS t_user_info;")
    void dropUserInfoTable();

    @Update("CREATE TABLE t_user_info ("
            + "id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY, "
            + "userId bigint DEFAULT NULL, "
            + "nickname varchar(100) DEFAULT NULL, "
            + "avatar varchar(255) DEFAULT NULL, "
            + "sign text, "
            + "gender varchar(10) DEFAULT NULL, "
            + "birthday varchar(20) DEFAULT NULL, "
            + "createdTime DATETIME DEFAULT NULL, "
            + "updatedTime DATETIME DEFAULT NULL"
            + ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;")
    void createUserInfoTable();

    @Update("DROP TABLE IF EXISTS t_following_group;")
    void dropFollowingGroupTable();

    @Update("CREATE TABLE t_following_group ( "
            + "id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY, "
            + "userId bigint DEFAULT NULL, "
            + "name varchar(50) DEFAULT NULL COMMENT 'following group name', "
            + "type varchar(5) DEFAULT NULL COMMENT 'following group type: 0 special attention "
            + "1 follow privately 2 group by default 3 customized group', "
            + "createdTime datetime DEFAULT NULL, "
            + "updatedTime datetime DEFAULT NULL "
            + ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;")
    void createFollowingGroupTable();

    @Update("DROP TABLE IF EXISTS t_user_following;")
    void dropUserFollowingTable();

    @Update("CREATE TABLE t_user_following ( "
            + "id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY, "
            + "userId bigint DEFAULT NULL, "
            + "followingId bigint DEFAULT NULL COMMENT 'user that is followed', "
            + "groupId bigint DEFAULT NULL, "
            + "createdTime datetime DEFAULT NULL "
            + ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;")
    void createUserFollowingTable();

    @Update("DROP TABLE IF EXISTS t_user_moment;")
    void dropUserMomentTable();

    @Update("CREATE TABLE t_user_moment ( "
            + "id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY, "
            + "userId bigint DEFAULT NULL, "
            + "type varchar(5) DEFAULT NULL COMMENT 'moment type: 0 video "
            + "1 livestreaming 2 user interaction', "
            + "contentId bigint DEFAULT NULL, "
            + "createdTime datetime DEFAULT NULL, "
            + "updatedTime datetime DEFAULT NULL "
            + ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;")
    void createUserMomentTable();

    @Update("DROP TABLE IF EXISTS t_auth_role")
    void dropAuthRoleTable();

    @Update("CREATE TABLE t_auth_role ( "
            + "id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY, "
            + "name varchar(255) DEFAULT NULL, "
            + "code varchar(50) DEFAULT NULL COMMENT 'different from name, e.g. name is Regular User "
            + "while code is user. Use this rather than id in the code because this is human readable', "
            + "createdTime datetime DEFAULT NULL, "
            + "updatedTime datetime DEFAULT NULL "
            + ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;")
    void createAuthRoleTable();

    @Update("DROP TABLE IF EXISTS t_user_role_association")
    void dropUserRoleAssociationTable();

    @Update("CREATE TABLE t_user_role_association ( "
            + "id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY, "
            + "userId bigint DEFAULT NULL, "
            + "roleId bigint DEFAULT NULL, "
            + "createdTime datetime DEFAULT NULL"
            + ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;")
    void createUserRoleAssociationTable();

    @Update("DROP TABLE IF EXISTS t_auth_element_operation")
    void dropAuthElementOperationTable();

    @Update("CREATE TABLE t_auth_element_operation ( "
            + "id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY, "
            + "elementName varchar(255) DEFAULT NULL, "
            + "elementCode varchar(50) DEFAULT NULL, "
            + "operationType varchar(5) DEFAULT NULL COMMENT '0 clickable 1 visible', "
            + "createdTime datetime DEFAULT NULL, "
            + "updatedTime datetime DEFAULT NULL "
            + ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;")
    void createAuthElementOperationTable();

    @Update("DROP TABLE IF EXISTS t_element_operation_role_association")
    void dropElementOperationRoleAssociationTable();

    @Update("CREATE TABLE t_element_operation_role_association ( "
            + "id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY, "
            + "elementOperationId bigint DEFAULT NULL, "
            + "roleId bigint DEFAULT NULL, "
            + "createdTime datetime DEFAULT NULL"
            + ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;")
    void createElementOperationRoleAssociationTable();

    @Update("DROP TABLE IF EXISTS t_auth_menu")
    void dropAuthMenuTable();

    @Update("CREATE TABLE t_auth_menu ( "
            + "id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY, "
            + "menuName varchar(255) DEFAULT NULL, "
            + "menuCode varchar(50) DEFAULT NULL, "
            + "createdTime datetime DEFAULT NULL, "
            + "updatedTime datetime DEFAULT NULL "
            + ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;")
    void createAuthMenuTable();

    @Update("DROP TABLE IF EXISTS t_menu_role_association")
    void dropMenuRoleAssociationTable();

    @Update("CREATE TABLE t_menu_role_association ( "
            + "id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY, "
            + "menuId bigint DEFAULT NULL, "
            + "roleId bigint DEFAULT NULL, "
            + "createdTime datetime DEFAULT NULL"
            + ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;")
    void createMenuRoleAssociationTable();

    @Update("DROP TABLE IF EXISTS t_refresh_token")
    void dropRefreshTokenTable();

    @Update("CREATE TABLE t_refresh_token ( "
            + "id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY, "
            + "userId bigint DEFAULT NULL, "
            + "refreshToken varchar(500) DEFAULT NULL, "
            + "createdTime datetime DEFAULT NULL"
            + ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;")
    void createRefreshTokenTable();

}
