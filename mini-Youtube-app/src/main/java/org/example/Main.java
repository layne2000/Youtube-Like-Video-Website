package org.example;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.example.mapper.DBInitializer;
import org.example.mapper.auth.AuthRoleMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

import static java.lang.System.exit;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class Main {
    public static void main(String[] args) {
        //traditional mybatis approach to initialize DB
        String resource = "mybatis-config.xml";
        InputStream inputStream;
        SqlSessionFactory sqlSessionFactory;

        try {
            inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error initializing SqlSessionFactory", e);
        }
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            DBInitializer databaseInitializer = sqlSession.getMapper(DBInitializer.class);
//            databaseInitializer.dropUserTable();
//            databaseInitializer.createUserTable();
//            databaseInitializer.dropUserInfoTable();
//            databaseInitializer.createUserInfoTable();
//            databaseInitializer.dropFollowingGroupTable();
//            databaseInitializer.createFollowingGroupTable();
//            databaseInitializer.dropUserFollowingTable();
//            databaseInitializer.createUserFollowingTable();
//            databaseInitializer.dropUserMomentTable();
//            databaseInitializer.createUserMomentTable();
//            databaseInitializer.dropAuthRoleTable();
//            databaseInitializer.createAuthRoleTable();
//            databaseInitializer.dropUserRoleAssociationTable();
//            databaseInitializer.createUserRoleAssociationTable();
//            databaseInitializer.dropAuthElementOperationTable();
//            databaseInitializer.createAuthElementOperationTable();
//            databaseInitializer.dropElementOperationRoleAssociationTable();
//            databaseInitializer.createElementOperationRoleAssociationTable();
//            databaseInitializer.dropAuthMenuTable();
//            databaseInitializer.createAuthMenuTable();
//            databaseInitializer.dropMenuRoleAssociationTable();
//            databaseInitializer.createMenuRoleAssociationTable();
//            databaseInitializer.dropRefreshTokenTable();
//            databaseInitializer.createRefreshTokenTable();
//            databaseInitializer.dropFileTable();
//            databaseInitializer.createFileTable();
//            databaseInitializer.dropVideoTable();
//            databaseInitializer.createVideoTable();
//            databaseInitializer.dropTagTable();
//            databaseInitializer.createTagTable();
//            databaseInitializer.dropVideoTagAssociationTable();
//            databaseInitializer.createVideoTagAssociationTable();
//            AuthRoleMapper authRoleMapper = sqlSession.getMapper(AuthRoleMapper.class);
//            authRoleMapper.addAuthRole("freshman", "Lv0", LocalDateTime.now());
            sqlSession.commit();
            //sqlSession.close(); //sqlSession will be closed automatically after leaving try block
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Can't connect to the database");
            exit(1);
        }
        ApplicationContext app = SpringApplication.run(Main.class, args);
        WebSocketService.setApplicationContext(app);
    }

}