package com.example.movieBooking.CoreEntityClasses;

import java.util.UUID;

public class User {

    private UUID id;
    private String name;
    public User(final UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
