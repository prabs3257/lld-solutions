package com.example.splitwise.repository;

import com.example.splitwise.model.Group;

import java.util.Optional;

public interface GroupRepository {
    Group save(Group group);
    Optional<Group> findById(String groupId);
}
