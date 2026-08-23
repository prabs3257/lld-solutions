package com.example.splitwise.repository;

import com.example.splitwise.model.Group;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class InMemoryGroupRepository implements GroupRepository {
    private final ConcurrentMap<String, Group> groups = new ConcurrentHashMap<>();

    @Override
    public Group save(Group group) {
        if (groups.putIfAbsent(group.getId(), group) != null) {
            throw new IllegalArgumentException("Group already exists: " + group.getId());
        }
        return group;
    }

    @Override
    public Optional<Group> findById(String groupId) {
        return Optional.ofNullable(groups.get(groupId));
    }
}
