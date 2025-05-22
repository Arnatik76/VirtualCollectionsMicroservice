package kz.example.repository;

import kz.example.entity.UserFollow;
import kz.example.entity.UserFollowId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserFollowRepository extends JpaRepository<UserFollow, UserFollowId> {

    @Query("SELECT uf FROM UserFollow uf JOIN FETCH uf.followed WHERE uf.id.followerId = :followerId")
    Page<UserFollow> findByFollowerIdWithFollowedUser(@Param("followerId") Integer followerId, Pageable pageable);

    @Query("SELECT uf FROM UserFollow uf JOIN FETCH uf.follower WHERE uf.id.followedId = :followedId")
    Page<UserFollow> findByFollowedIdWithFollowerUser(@Param("followedId") Integer followedId, Pageable pageable);

    Optional<UserFollow> findById_FollowerIdAndId_FollowedId(Integer followerId, Integer followedId);

    boolean existsById_FollowerIdAndId_FollowedId(Integer followerId, Integer followedId);
}