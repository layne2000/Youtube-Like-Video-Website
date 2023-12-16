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
            + "id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
            + "userId bigint DEFAULT NULL, "
            + "nickname varchar(100) DEFAULT NULL, "
            + "avatar varchar(255) DEFAULT NULL, "
            + "sign text, "
            + "gender varchar(2) DEFAULT NULL, "
            + "birthday varchar(20) DEFAULT NULL, "
            + "createdTime DATETIME DEFAULT NULL, "
            + "updatedTime DATETIME DEFAULT NULL"
            + ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;")
    void createUserInfoTable();
}
