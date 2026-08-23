package com.example.splitwise.manager;

import com.example.splitwise.model.User;
import com.example.splitwise.repository.UserRepository;
import com.example.splitwise.util.IdGenerator;

public final class UserManager {
    private final UserRepository userRepository;
    private final IdGenerator idGenerator;

    public UserManager(UserRepository userRepository, IdGenerator idGenerator) {
        this.userRepository = userRepository;
        this.idGenerator = idGenerator;
    }

    public User createUser(String name, String email) {
        return userRepository.save(
                new User(idGenerator.nextId(), name, email)
        );
    }

    public User getUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
    }
}
