package com.simplesocial.repository;

import com.simplesocial.entity.Friendship;
import com.simplesocial.entity.FriendshipStatus;
import com.simplesocial.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
        Optional<Friendship> findByUserAndFriend(User user, User friend);

        boolean existsByUserAndFriend(User user, User friend);

        @Query("SELECT f FROM Friendship f JOIN FETCH f.user JOIN FETCH f.friend WHERE f.user = :user AND f.status = :status")
        List<Friendship> findUserFriendshipsByStatus(
                        @Param("user") User user,
                        @Param("status") FriendshipStatus status);

        @Query("SELECT f.friend FROM Friendship f WHERE f.user = :user AND f.status = :status")
        List<User> findUserFriendsByStatus(
                        @Param("user") User user,
                        @Param("status") FriendshipStatus status);
}