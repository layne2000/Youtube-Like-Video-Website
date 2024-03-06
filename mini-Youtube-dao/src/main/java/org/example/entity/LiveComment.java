package org.example.entity;

import java.time.LocalDateTime;

public class LiveComment {
    private Long id;
    private Long userId;
    private Long videoId;
    private String content;
    private String appearingTime;
    private LocalDateTime createdTime;

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getVideoId() {
        return videoId;
    }

    public String getContent() {
        return content;
    }

    public String getAppearingTime() {
        return appearingTime;
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

    public void setContent(String content) {
        this.content = content;
    }

    public void setAppearingTime(String appearingTime) {
        this.appearingTime = appearingTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}
