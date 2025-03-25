package com.simplesocial.repository;

import com.simplesocial.entity.Group;
import com.simplesocial.entity.GroupMember;
import com.simplesocial.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    Optional<GroupMember> findByUserAndGroup(User user, Group group);

    boolean existsByUserAndGroup(User user, Group group);

    List<GroupMember> findByGroup(Group group);

    List<GroupMember> findByUser(User user);
}