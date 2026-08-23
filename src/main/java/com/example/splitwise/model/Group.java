package com.example.splitwise.model;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public final class Group {
    private final String id;
    private final String name;
    private final Set<String> memberIds = new LinkedHashSet<>();

    public Group(String id, String name) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Group id is required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Group name is required");
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }

    public synchronized void addMember(String userId) {
        memberIds.add(userId);
    }

    public synchronized void removeMember(String userId) {
        memberIds.remove(userId);
    }

    public synchronized boolean hasMember(String userId) {
        return memberIds.contains(userId);
    }

    public synchronized Set<String> getMemberIds() {
        return Collections.unmodifiableSet(new LinkedHashSet<>(memberIds));
    }

    @Override
    public String toString() {
        return "Group{id='%s', name='%s', members=%s}".formatted(id, name, memberIds);
    }
}
