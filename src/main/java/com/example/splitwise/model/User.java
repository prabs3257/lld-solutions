package com.example.splitwise.model;

import java.util.Objects;

public final class User {
    private final String id;
    private final String name;
    private final String email;

    public User(String id, String name, String email) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("User id is required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name is required");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email is required");
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    @Override
    public boolean equals(Object other) {
        return other instanceof User user && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name + "(" + id + ")";
    }
}
