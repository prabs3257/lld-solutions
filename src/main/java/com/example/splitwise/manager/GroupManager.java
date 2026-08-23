package com.example.splitwise.manager;

import com.example.splitwise.model.Group;
import com.example.splitwise.repository.GroupRepository;
import com.example.splitwise.util.IdGenerator;

import java.util.HashSet;
import java.util.List;

public final class GroupManager {
    private final GroupRepository groupRepository;
    private final UserManager userManager;
    private final IdGenerator idGenerator;

    public GroupManager(
            GroupRepository groupRepository,
            UserManager userManager,
            IdGenerator idGenerator
    ) {
        this.groupRepository = groupRepository;
        this.userManager = userManager;
        this.idGenerator = idGenerator;
    }

    public Group createGroup(String name, List<String> memberIds) {
        if (memberIds == null || memberIds.isEmpty()) {
            throw new IllegalArgumentException("A group needs at least one member");
        }

        if (new HashSet<>(memberIds).size() != memberIds.size()) {
            throw new IllegalArgumentException("Duplicate group member");
        }

        memberIds.forEach(userManager::getUser);

        Group group = new Group(idGenerator.nextId(), name);
        memberIds.forEach(group::addMember);

        return groupRepository.save(group);
    }

    public Group getGroup(String groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found: " + groupId));
    }

    public void addMember(String groupId, String userId) {
        userManager.getUser(userId);
        getGroup(groupId).addMember(userId);
    }

    public void removeMember(String groupId, String userId) {
        getGroup(groupId).removeMember(userId);
    }

    public void validateMember(String groupId, String userId) {
        if (!getGroup(groupId).hasMember(userId)) {
            throw new IllegalArgumentException(
                    "User " + userId + " is not a member of group " + groupId
            );
        }
    }
}
