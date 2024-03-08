package org.example.entity;


import java.time.LocalDateTime;

public class VideoView {
    private Long id;
    private Long videoId;
    private Long userId;
    private String clientId;
    private String ip;
    private LocalDateTime createdTime;

    public Long getId() {
        return id;
    }

    public Long getVideoId() {
        return videoId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getIp() {
        return ip;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
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

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}
