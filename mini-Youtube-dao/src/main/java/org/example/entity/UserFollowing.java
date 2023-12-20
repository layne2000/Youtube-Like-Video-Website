package org.example.entity;

import java.time.LocalDateTime;

public class UserFollowing {
    private Long id;
    private Long userId;
    private Long followingId;
    private Long groupId;
    private LocalDateTime createdTime;
    // can be the current user's info or the following user's info
    private UserInfo userInfo;

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getFollowingId() {
        return followingId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setFollowingId(Long followingId) {
        this.followingId = followingId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
