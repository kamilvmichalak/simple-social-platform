package com.simplesocial.service;

import com.simplesocial.entity.Group;
import com.simplesocial.entity.GroupMember;
import com.simplesocial.entity.GroupRole;
import com.simplesocial.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GroupService {
    Group createGroup(Group group);

    Group findById(Long id);

    Group updateGroup(Long id, Group groupDetails);

    void deleteGroup(Long id);

    Page<Group> findByCreator(User creator, Pageable pageable);

    Page<Group> searchPublicGroups(String query, Pageable pageable);

    Page<Group> findUserGroups(User user, Pageable pageable);

    GroupMember addMember(GroupMember member);

    void removeMember(Long memberId);

    GroupMember updateMemberRole(Long memberId, GroupRole role);
}