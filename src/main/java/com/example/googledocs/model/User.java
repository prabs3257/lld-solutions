package com.example.googledocs.model;

import java.util.Objects;

/**
 * Represents a collaborator editing a document.
 */
public final class User {

    private final String userId;
    private final String name;

    public User(String userId, String name) {
        this.userId = Objects.requireNonNull(userId);
        this.name = Objects.requireNonNull(name);
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }
}
