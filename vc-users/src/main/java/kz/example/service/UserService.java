package kz.example.service;

import kz.example.dto.UpdateUserRequestDto;
import kz.example.dto.UserResponseDto;
import kz.example.entity.User;
import kz.example.entity.UserFollow;
import kz.example.entity.UserFollowId;
import kz.example.exception.ResourceAlreadyExistsException;
import kz.example.exception.ResourceNotFoundException;
import kz.example.repository.UserFollowRepository;
import kz.example.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserFollowRepository userFollowRepository;

    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapToUserResponseDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getUsersByIds(List<Long> userIds) {
        List<User> users = userRepository.findAllById(userIds);
        return users.stream()
                .map(this::mapToUserResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponseDto updateUser(Long userId, UpdateUserRequestDto updateUserDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (updateUserDto.getDisplayName() != null) {
            user.setDisplayName(updateUserDto.getDisplayName());
        }
        if (updateUserDto.getBio() != null) {
            user.setBio(updateUserDto.getBio());
        }
        if (updateUserDto.getAvatarUrl() != null) {
            user.setAvatarUrl(updateUserDto.getAvatarUrl());
        }

        User updatedUser = userRepository.save(user);
        return mapToUserResponseDto(updatedUser);
    }

    @Transactional
    public void followUser(Long followerId, Long followedId) {
        if (followerId.equals(followedId)) {
            throw new IllegalArgumentException("User cannot follow themselves.");
        }
        Integer followerIntId = followerId.intValue();
        Integer followedIntId = followedId.intValue();

        if (followerId > Integer.MAX_VALUE || followedId > Integer.MAX_VALUE) {
            System.err.println("CRITICAL WARNING: User ID exceeds Integer max value. Data truncation for UserFollow. Follower: " + followerId + ", Followed: " + followedId);
        }


        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new ResourceNotFoundException("Follower user not found with id: " + followerId));
        User followed = userRepository.findById(followedId)
                .orElseThrow(() -> new ResourceNotFoundException("User to be followed not found with id: " + followedId));

        if (userFollowRepository.existsById_FollowerIdAndId_FollowedId(followerIntId, followedIntId)) {
            throw new ResourceAlreadyExistsException("User " + followerId + " is already following user " + followedId);
        }

        UserFollowId userFollowId = new UserFollowId();
        userFollowId.setFollowerId(followerIntId);
        userFollowId.setFollowedId(followedIntId);

        UserFollow userFollow = new UserFollow();
        userFollow.setId(userFollowId);
        userFollow.setFollower(follower);
        userFollow.setFollowed(followed);
        userFollow.setFollowedAt(OffsetDateTime.now());

        userFollowRepository.save(userFollow);
    }

    @Transactional
    public void unfollowUser(Long followerId, Long followedId) {
        Integer followerIntId = followerId.intValue();
        Integer followedIntId = followedId.intValue();

        if (followerId > Integer.MAX_VALUE || followedId > Integer.MAX_VALUE) {
            System.err.println("CRITICAL WARNING: User ID exceeds Integer max value during unfollow. Data truncation for UserFollow. Follower: " + followerId + ", Followed: " + followedId);
        }


        UserFollowId userFollowId = new UserFollowId();
        userFollowId.setFollowerId(followerIntId);
        userFollowId.setFollowedId(followedIntId);

        UserFollow userFollow = userFollowRepository.findById(userFollowId)
                .orElseThrow(() -> new ResourceNotFoundException("Follow relationship not found."));

        userFollowRepository.delete(userFollow);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDto> getFollowers(Long userId, Pageable pageable) {
        if (userId > Integer.MAX_VALUE) {
            System.err.println("CRITICAL WARNING: User ID " + userId + " exceeds Integer max value. Cannot fetch followers accurately.");
            return Page.empty(pageable);
        }
        Page<UserFollow> userFollowsPage = userFollowRepository.findByFollowedIdWithFollowerUser(userId.intValue(), pageable);
        List<UserResponseDto> followersDto = userFollowsPage.getContent().stream()
                .map(userFollow -> mapToUserResponseDto(userFollow.getFollower()))
                .collect(Collectors.toList());
        return new PageImpl<>(followersDto, pageable, userFollowsPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDto> getFollowing(Long userId, Pageable pageable) {
        if (userId > Integer.MAX_VALUE) {
            System.err.println("CRITICAL WARNING: User ID " + userId + " exceeds Integer max value. Cannot fetch following accurately.");
            return Page.empty(pageable);
        }
        Page<UserFollow> userFollowsPage = userFollowRepository.findByFollowerIdWithFollowedUser(userId.intValue(), pageable);
        List<UserResponseDto> followingDto = userFollowsPage.getContent().stream()
                .map(userFollow -> mapToUserResponseDto(userFollow.getFollowed()))
                .collect(Collectors.toList());
        return new PageImpl<>(followingDto, pageable, userFollowsPage.getTotalElements());
    }


    private UserResponseDto mapToUserResponseDto(User user) {
        if (user == null) return null;
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUserId(user.getId());
        userResponseDto.setUsername(user.getUsername());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setDisplayName(user.getDisplayName());
        userResponseDto.setBio(user.getBio());
        userResponseDto.setAvatarUrl(user.getAvatarUrl());
        userResponseDto.setCreatedAt(user.getCreatedAt());
        userResponseDto.setLastLogin(user.getLastLogin());
        userResponseDto.setIsActive(user.getIsActive());
        return userResponseDto;
    }
}