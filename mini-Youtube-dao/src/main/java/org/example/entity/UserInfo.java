package org.example.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Document(indexName = "user-infos")
public class UserInfo {
    @Id
    private Long id;
    private Long userId;
    @Field(type = FieldType.Text)
    private String nickname;
    private String avatar;
    private String sign;
    private String gender;
    private String birthday;
    @Field(type = FieldType.Date)
    private LocalDateTime createdTime;
    @Field(type = FieldType.Date)
    private LocalDateTime updatedTime;
    private Boolean followed;

    public UserInfo(){
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getSign() {
        return sign;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public Boolean getFollowed() {
        return followed;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public void setFollowed(Boolean followed) {
        this.followed = followed;
    }
}
