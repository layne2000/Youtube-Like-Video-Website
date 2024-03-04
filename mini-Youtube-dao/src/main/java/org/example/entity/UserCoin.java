package org.example.entity;

import java.time.LocalDateTime;

public class UserCoin {
    private Long id;
    private Long userId;
    private long amount;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public long getAmount() {
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

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }
}
