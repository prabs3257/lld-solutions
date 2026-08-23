package com.example.splitwise.repository;

import com.example.splitwise.model.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(String userId);
}
