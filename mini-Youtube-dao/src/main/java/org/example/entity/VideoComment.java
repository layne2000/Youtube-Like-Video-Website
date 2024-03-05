package org.example.entity;

import java.time.LocalDateTime;
import java.util.List;

public class VideoComment {
    private Long id;
    private Long videoId;
    private Long userId;
    private Long replyUserId;
    private Long rootCommentId;
    private String content;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private List<VideoComment> childList;
    private UserInfo userInfo;
    private UserInfo replyUserInfo;
    public Long getId() {
        return id;
    }

    public Long getVideoId() {
        return videoId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getReplyUserId() {
        return replyUserId;
    }

    public Long getRootCommentId() {
        return rootCommentId;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public List<VideoComment> getChildList() {
        return childList;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public UserInfo getReplyUserInfo() {
        return replyUserInfo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setReplyUserId(Long replyUserId) {
        this.replyUserId = replyUserId;
    }

    public void setRootCommentId(Long rootCommentId) {
        this.rootCommentId = rootCommentId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public void setChildList(List<VideoComment> childList) {
        this.childList = childList;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public void setReplyUserInfo(UserInfo replyUserInfo) {
        this.replyUserInfo = replyUserInfo;
    }
}
