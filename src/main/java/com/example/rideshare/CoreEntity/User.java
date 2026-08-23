package com.example.rideshare.CoreEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class User implements RideObserver {

    private UUID userId;
    private String userName;
    private List<RideHistory> rideHistory;

    public User(UUID userId, String userName) {
        this.userId = userId;
        this.userName = userName;
        this.rideHistory = new ArrayList<>();
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<RideHistory> getRideHistory() {
        return rideHistory;
    }

    public void setRideHistory(List<RideHistory> rideHistory) {
        this.rideHistory = rideHistory;
    }
}
