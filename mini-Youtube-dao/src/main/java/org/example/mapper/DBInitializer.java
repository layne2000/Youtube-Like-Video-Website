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

    @Update("DROP TABLE IF EXISTS t_file")
    void dropFileTable();

    @Update("CREATE TABLE t_file ( "
            + "id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY, "
            + "url varchar(500) DEFAULT NULL, "
            + "type varchar(5) DEFAULT NULL, "
            + "SHA256 varchar(500) DEFAULT NULL, "
            + "createdTime datetime DEFAULT NULL"
            + ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;")
    void createFileTable();

    @Update("DROP TABLE IF EXISTS t_video")
    void dropVideoTable();

    @Update("CREATE TABLE t_video ( "
            + "id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY, "
            + "userId bigint NOT NULL, "
            + "url varchar(500) NOT NULL, "
            + "thumbnail varchar(500) NOT NULL COMMENT 'video thumbnail url', "
            + "title varchar(255) NOT NULL, "
            + "type varchar(5) NOT NULL COMMENT '0 self-made 1 shared', "
            + "duration bigint NOT NULL, "
            + "section varchar(5) DEFAULT NULL COMMENT '0 music 1 movie 2 funny', "
            + "description text DEFAULT NULL, "
            + "createdTime datetime DEFAULT NULL, "
            + "updatedTime datetime DEFAULT NULL"
            + ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;")
    void createVideoTable();

    @Update("DROP TABLE IF EXISTS t_tag")
    void dropTagTable();

    @Update("CREATE TABLE t_tag ( "
            + "id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY, "
            + "name varchar(500) NOT NULL, "
            + "createdTime datetime DEFAULT NULL"
            + ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;")
    void createTagTable();

    @Update("DROP TABLE IF EXISTS t_video_tag_association")
    void dropVideoTagAssociationTable();

    @Update("CREATE TABLE t_video_tag_association ( "
            + "id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY, "
            + "videoId bigint NOT NULL, "
            + "tagId bigint NOT NULL, "
            + "createdTime datetime DEFAULT NULL"
            + ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;")
    void createVideoTagAssociationTable();

    @Update("DROP TABLE IF EXISTS t_video_like")
    void dropVideoLikeTable();

    @Update("CREATE TABLE t_video_like ( "
            + "id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY, "
            + "videoId bigint NOT NULL, "
            + "userId bigint NOT NULL, "
            + "createdTime datetime DEFAULT NULL"
            + ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;")
    void createVideoLikeTable();

    @Update("DROP TABLE IF EXISTS t_collection_group")
    void dropCollectionGroupTable();

    @Update("CREATE TABLE t_collection_group ( "
            + "id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY, "
            + "userId bigint NOT NULL, "
            + "name varchar(50) DEFAULT NULL, "
            + "type varchar(2) DEFAULT NULL COMMENT '0 default 1 customized', "
            + "createdTime datetime DEFAULT NULL"
            + "updatedTime datetime DEFAULT NULL"
            + ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;")
    void createCollectionGroupTable();

    @Update("DROP TABLE IF EXISTS t_video_collection")
    void dropVideoCollectionTable();

    @Update("CREATE TABLE t_video_collection ( "
            + "id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY, "
            + "videoId bigint NOT NULL, "
            + "userId bigint NOT NULL, "
            + "groupId bigint NOT NULL, "
            + "createdTime datetime DEFAULT NULL"
            + ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;")
    void createVideoCollectionTable();

    @Update("DROP TABLE IF EXISTS t_user_coin")
    void dropUserCoinTable();

    @Update("CREATE TABLE t_user_coin ( "
            + "id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY, "
            + "userId bigint NOT NULL, "
            + "amount bigint NOT NULL DEFAULT 0, "
            + "createdTime datetime DEFAULT NULL"
            + "updatedTime datetime DEFAULT NULL"
            + ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;")
    void createUserCoinTable();

    @Update("DROP TABLE IF EXISTS t_video_coin")
    void dropVideoCoinTable();

    @Update("CREATE TABLE t_video_coin ( "
            + "id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY, "
            + "videoId bigint NOT NULL, "
            + "userId bigint NOT NULL, "
            + "amount bigint NOT NULL DEFAULT 0, "
            + "createdTime datetime DEFAULT NULL"
            + "updatedTime datetime DEFAULT NULL"
            + ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;")
    void createVideoCoinTable();

    @Update("DROP TABLE IF EXISTS t_video_comment")
    void dropVideoCommentTable();

    @Update("CREATE TABLE t_video_comment ( "
            + "id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY, "
            + "videoId bigint NOT NULL, "
            + "userId bigint NOT NULL, "
            + "replyUserId bigint NOT NULL DEFAULT 0 COMMENT 'the user replied by this comment', "
            + "rootCommentId bigint DEFAULT NULL, "
            + "content text DEFAULT NULL, "
            + "createdTime datetime DEFAULT NULL"
            + "updatedTime datetime DEFAULT NULL"
            + ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;")
    void createVideoCommentTable();

    @Update("DROP TABLE IF EXISTS t_live_comment")
    void dropLiveCommentTable();

    @Update("CREATE TABLE t_live_comment ( "
            + "id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY, "
            + "videoId bigint NOT NULL, "
            + "userId bigint NOT NULL, "
            + "content text DEFAULT NULL, "
            + "appearingTime varchar(50) DEFAULT NULL"
            + "createdTime datetime DEFAULT NULL"
            + ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;")
    void createLiveCommentTable();
}
