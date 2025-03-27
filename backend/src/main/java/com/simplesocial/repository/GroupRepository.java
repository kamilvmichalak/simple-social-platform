package com.simplesocial.repository;

import com.simplesocial.entity.Group;
import com.simplesocial.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Page<Group> findByCreator(User creator, Pageable pageable);

    @Query("SELECT g FROM Group g WHERE g.isPublic = true AND " +
            "(LOWER(g.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(g.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Group> searchPublicGroups(@Param("query") String query, Pageable pageable);

    @Query("SELECT g FROM Group g WHERE g.creator = :user OR " +
            "EXISTS (SELECT gm FROM GroupMember gm WHERE gm.group = g AND gm.user = :user)")
    Page<Group> findUserGroups(@Param("user") User user, Pageable pageable);
}