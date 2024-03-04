package org.example.entity;

import java.time.LocalDateTime;

public class VideoCoin {
    private Long id;
    private Long userId;
    private Long videoId;
    private Long amount;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getVideoId() {
        return videoId;
    }

    public Long getAmount() {
        return amount;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
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

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }
}
