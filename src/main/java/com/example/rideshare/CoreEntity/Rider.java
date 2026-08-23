package com.example.rideshare.CoreEntity;

import java.util.UUID;

public class Rider extends User {

    private Location currLocation;

    public Rider(String name, Location currLocation) {
        super(UUID.randomUUID(), name);
        this.currLocation = currLocation;
    }

    public Location getCurrLocation() {
        return currLocation;
    }

    public void setCurrLocation(Location currLocation) {
        this.currLocation = currLocation;
    }

    @Override
    public void onUpdate(Ride ride) {

    }
}
