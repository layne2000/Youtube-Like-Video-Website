package org.example.entity;

import java.time.LocalDateTime;

public class VideoLike {
    private Long id;
    private Long userId;
    private Long videoId;
    private LocalDateTime createdTime;

    public VideoLike(Long userId, Long videoId, LocalDateTime createdTime) {
        this.userId = userId;
        this.videoId = videoId;
        this.createdTime = createdTime;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getVideoId() {
        return videoId;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}
