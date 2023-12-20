package org.example.entity;

import java.time.LocalDateTime;
import java.util.List;

// each user have their own followingGroup records
public class FollowingGroup {
    private Long id;
    private Long userId;
    private String name;
    private String type;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private List<UserInfo> followingUserInfoList;

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public List<UserInfo> getFollowingUserInfoList() {
        return followingUserInfoList;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public void setFollowingUserInfoList(List<UserInfo> followingUserInfoList) {
        this.followingUserInfoList = followingUserInfoList;
    }
}
