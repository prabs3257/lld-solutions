package com.example.splitwise.repository;

import com.example.splitwise.model.User;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class InMemoryUserRepository implements UserRepository {
    private final ConcurrentMap<String, User> users = new ConcurrentHashMap<>();

    @Override
    public User save(User user) {
        if (users.putIfAbsent(user.getId(), user) != null) {
            throw new IllegalArgumentException("User already exists: " + user.getId());
        }
        return user;
    }

    @Override
    public Optional<User> findById(String userId) {
        return Optional.ofNullable(users.get(userId));
    }
}
