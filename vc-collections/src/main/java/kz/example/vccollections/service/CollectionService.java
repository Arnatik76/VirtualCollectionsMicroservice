package kz.example.vccollections.service;

import kz.example.dto.UserResponseDto;
import kz.example.entity.*;
import kz.example.vccollections.client.ContentServiceClient;
import kz.example.vccollections.client.UserServiceClient;
import kz.example.vccollections.dto.*;
import kz.example.vccollections.exception.ForbiddenActionException;
import kz.example.vccollections.exception.ResourceAlreadyExistsException;
import kz.example.vccollections.exception.ResourceNotFoundException;
import kz.example.vccollections.repository.*;
import kz.example.vccollections.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CollectionService {

    private static final Logger log = LoggerFactory.getLogger(CollectionService.class);

    private final CollectionRepository collectionRepository;
    private final CollectionItemRepository collectionItemRepository;
    private final CollectionCommentRepository collectionCommentRepository;
    private final CollectionLikeRepository collectionLikeRepository;
    private final CollectionCollaboratorRepository collectionCollaboratorRepository;
    private final UserServiceClient userServiceClient;
    private final ContentServiceClient contentServiceClient;

    private UserResponseDto fetchUser(Long userId) {
        if (userId == null) return null;
        try {
            return userServiceClient.getUserById(userId);
        } catch (Exception e) {
            log.warn("Failed to fetch user with id {}: {}", userId, e.getMessage());
            UserResponseDto errorUser = new UserResponseDto();
            errorUser.setUserId(userId);
            errorUser.setUsername("Error: User not found");
            return errorUser;
        }
    }

    private MediaItemDto fetchMediaItem(Long mediaItemId) {
        if (mediaItemId == null) return null;
        try {
            return contentServiceClient.getMediaItemById(mediaItemId);
        } catch (Exception e) {
            log.warn("Failed to fetch media item with id {}: {}", mediaItemId, e.getMessage());
            MediaItemDto errorItem = new MediaItemDto();
            errorItem.setItemId(mediaItemId);
            errorItem.setTitle("Error: Media item not loaded");
            return errorItem;
        }
    }

    private CollectionItemEntryDto mapToCollectionItemEntryDto(CollectionItem item, Long collectionIdForContext) {
        UserResponseDto addedByUser = fetchUser(item.getAddedByUserId());
        Long mediaItemIdFromCollectionItem = item.getId() != null ? item.getId().getItemId() : null;
        MediaItemDto mediaItemDto = fetchMediaItem(mediaItemIdFromCollectionItem);


        String addedAtString = (item.getAddedAt() != null) ?
                item.getAddedAt().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) : null;

        CollectionItemEntryDto entryDto = new CollectionItemEntryDto();
        entryDto.setCollectionId(item.getId() != null ? item.getId().getCollectionId() : collectionIdForContext);
        entryDto.setItemId(mediaItemIdFromCollectionItem);
        entryDto.setAddedByUserId(item.getAddedByUserId() != null ? item.getAddedByUserId().intValue() : null);
        entryDto.setAddedAt(addedAtString);
        entryDto.setPosition(item.getPosition());
        entryDto.setNotes(item.getNotes());
        entryDto.setMediaItem(mediaItemDto);
        entryDto.setAddedByUser(addedByUser);
        return entryDto;
    }

    private CollectionCommentResponseDto mapToCollectionCommentResponseDto(CollectionComment comment) {
        UserResponseDto commentUser = fetchUser(comment.getUserId() != null ? comment.getUserId().longValue() : null);
        return new CollectionCommentResponseDto(
                comment.getId(),
                comment.getCollection() != null ? comment.getCollection().getId() : null,
                comment.getCommentText(),
                comment.getCreatedAt(),
                commentUser
        );
    }

    private CollectionCollaboratorResponseDto mapToCollectionCollaboratorResponseDto(CollectionCollaborator collaborator) {
        UserResponseDto collabUser = fetchUser(collaborator.getUserId());
        return new CollectionCollaboratorResponseDto(
                collaborator.getCollection().getId(),
                collaborator.getRole(),
                collaborator.getJoinedAt(),
                collabUser
        );
    }

    private CollectionResponseDto mapToCollectionResponseDto(Collection collection) {
        UserResponseDto owner = fetchUser(collection.getUserId());

        List<CollectionItemEntryDto> itemDtos = getFullCollectionItemsForDto(collection.getId(), Pageable.ofSize(5)).getContent(); // Preview 5 items

        List<CollectionCollaboratorResponseDto> collaboratorDtos = collectionCollaboratorRepository
                .findCollectionCollaboratorsByCollectionId(collection.getId()).stream()
                .map(this::mapToCollectionCollaboratorResponseDto)
                .collect(Collectors.toList());

        Integer likeCount = collectionLikeRepository.countCollectionLikeByCollectionId(collection.getId());
        Integer commentCount = collectionCommentRepository.countCollectionCommentByCollectionId(collection.getId());

        return new CollectionResponseDto(
                collection.getId(),
                collection.getTitle(),
                collection.getDescription(),
                collection.getCoverImageUrl(),
                collection.getCreatedAt(),
                collection.getUpdatedAt(),
                collection.getIsPublic(),
                collection.getViewCount(),
                owner,
                itemDtos,
                collaboratorDtos,
                likeCount,
                commentCount
        );
    }

    private void authorizeCollectionModification(Long collectionId, Long userId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + collectionId));
        if (!collection.getUserId().equals(userId)) {
            boolean isEditor = collectionCollaboratorRepository.findById(new CollectionCollaboratorId(collectionId, userId))
                    .map(cc -> "editor".equalsIgnoreCase(cc.getRole()))
                    .orElse(false);
            if (!isEditor) {
                throw new ForbiddenActionException("User does not have permission to modify this collection.");
            }
        }
    }

    private void authorizeCollectionOwnership(Long collectionId, Long userId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + collectionId));
        if (!collection.getUserId().equals(userId)) {
            throw new ForbiddenActionException("User is not the owner of this collection.");
        }
    }

    @Transactional(readOnly = true)
    public CollectionResponseDto getCollectionDetailsById(Long id) {
        Collection collection = collectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + id));
        return mapToCollectionResponseDto(collection);
    }

    @Transactional(readOnly = true)
    public Page<CollectionResponseDto> getPublicFeed(Pageable pageable) {
        Page<Collection> collectionsPage = collectionRepository.findPublicCollections(pageable);
        List<CollectionResponseDto> responseDtos = collectionsPage.getContent().stream()
                .map(this::mapToCollectionResponseDto)
                .collect(Collectors.toList());
        return new PageImpl<>(responseDtos, pageable, collectionsPage.getTotalElements());
    }

    @Transactional
    public CollectionResponseDto createCollection(CreateCollectionRequestDto createDto, Long userId) {
        if (userId == null) {
            throw new ForbiddenActionException("User ID must be provided to create a collection.");
        }
        Collection collection = new Collection();
        collection.setTitle(createDto.getTitle());
        collection.setDescription(createDto.getDescription());
        collection.setCoverImageUrl(createDto.getCoverImageUrl());
        collection.setUserId(userId);
        collection.setCreatedAt(OffsetDateTime.now());
        collection.setUpdatedAt(OffsetDateTime.now());
        collection.setIsPublic(createDto.getIsPublic());
        collection.setViewCount(0);

        Collection savedCollection = collectionRepository.save(collection);
        return mapToCollectionResponseDto(savedCollection);
    }

    @Transactional
    public CollectionResponseDto updateCollection(Long collectionId, UpdateCollectionRequestDto updateDto, Long currentUserId) {
        authorizeCollectionModification(collectionId, currentUserId);
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + collectionId));

        collection.setTitle(updateDto.getTitle());
        collection.setDescription(updateDto.getDescription());
        collection.setCoverImageUrl(updateDto.getCoverImageUrl());
        collection.setIsPublic(updateDto.getIsPublic());
        collection.setUpdatedAt(OffsetDateTime.now());

        Collection updatedCollection = collectionRepository.save(collection);
        return mapToCollectionResponseDto(updatedCollection);
    }

    @Transactional
    public void deleteCollection(Long collectionId, Long currentUserId) {
        authorizeCollectionOwnership(collectionId, currentUserId);
        if (!collectionRepository.existsById(collectionId)) {
            throw new ResourceNotFoundException("Collection not found with id: " + collectionId);
        }
        collectionRepository.deleteById(collectionId);
    }

    @Transactional(readOnly = true)
    public Page<CollectionItemEntryDto> getCollectionItems(Long collectionId, Pageable pageable) {
        if (!collectionRepository.existsById(collectionId)) {
            throw new ResourceNotFoundException("Collection not found with id: " + collectionId);
        }
        Page<CollectionItem> itemsPage = collectionItemRepository.findById_CollectionId(collectionId, pageable);
        List<CollectionItemEntryDto> itemEntryDtos = itemsPage.getContent().stream()
                .map(item -> mapToCollectionItemEntryDto(item, collectionId))
                .collect(Collectors.toList());
        return new PageImpl<>(itemEntryDtos, pageable, itemsPage.getTotalElements());
    }
    private Page<CollectionItemEntryDto> getFullCollectionItemsForDto(Long collectionId, Pageable pageable) {
        Page<CollectionItem> itemsPage = collectionItemRepository.findById_CollectionId(collectionId, pageable);
        List<CollectionItemEntryDto> itemEntryDtos = itemsPage.getContent().stream()
                .map(item -> mapToCollectionItemEntryDto(item, collectionId))
                .collect(Collectors.toList());
        return new PageImpl<>(itemEntryDtos, pageable, itemsPage.getTotalElements());
    }


    @Transactional
    public CollectionItemEntryDto addCollectionItem(Long collectionId, AddCollectionItemRequestDto itemDto, Long currentUserId) {
        authorizeCollectionModification(collectionId, currentUserId);
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + collectionId));

        MediaItemDto mediaItem = fetchMediaItem(itemDto.getMediaItemId());
        if (mediaItem == null || mediaItem.getTitle().startsWith("Error:")) {
            throw new ResourceNotFoundException("Media item not found with id: " + itemDto.getMediaItemId());
        }

        CollectionItemId collectionItemId = new CollectionItemId();
        collectionItemId.setCollectionId(collectionId);
        collectionItemId.setItemId(itemDto.getMediaItemId());

        if (collectionItemRepository.existsById(collectionItemId)) {
            throw new ResourceAlreadyExistsException("Media item " + itemDto.getMediaItemId() + " already in collection " + collectionId);
        }

        CollectionItem newItem = new CollectionItem();
        newItem.setId(collectionItemId);
        newItem.setCollection(collection);
        newItem.setAddedByUserId(currentUserId);
        newItem.setAddedAt(OffsetDateTime.now());
        newItem.setNotes(itemDto.getNotes());
        newItem.setPosition(itemDto.getPosition());

        CollectionItem savedItem = collectionItemRepository.save(newItem);
        return mapToCollectionItemEntryDto(savedItem, collectionId);
    }

    @Transactional
    public CollectionItemEntryDto updateCollectionItem(Long collectionId, Long mediaItemId, UpdateCollectionItemRequestDto itemDto, Long currentUserId) {
        authorizeCollectionModification(collectionId, currentUserId);
        CollectionItemId collectionItemId = new CollectionItemId(collectionId, mediaItemId);
        CollectionItem item = collectionItemRepository.findById(collectionItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found in collection. Collection ID: " + collectionId + ", Media Item ID: " + mediaItemId));

        item.setNotes(itemDto.getNotes());
        item.setPosition(itemDto.getPosition());
        // item.setUpdatedAt(OffsetDateTime.now());

        CollectionItem updatedItem = collectionItemRepository.save(item);
        return mapToCollectionItemEntryDto(updatedItem, collectionId);
    }

    @Transactional
    public void removeCollectionItem(Long collectionId, Long mediaItemId, Long currentUserId) {
        authorizeCollectionModification(collectionId, currentUserId);
        CollectionItemId collectionItemId = new CollectionItemId(collectionId, mediaItemId);
        if (!collectionItemRepository.existsById(collectionItemId)) {
            throw new ResourceNotFoundException("Item not found in collection. Collection ID: " + collectionId + ", Media Item ID: " + mediaItemId);
        }
        collectionItemRepository.deleteById(collectionItemId);
    }

    @Transactional(readOnly = true)
    public Page<CollectionCommentResponseDto> getCollectionComments(Long collectionId, Pageable pageable) {
        if (!collectionRepository.existsById(collectionId)) {
            throw new ResourceNotFoundException("Collection not found with id: " + collectionId);
        }
        Page<CollectionComment> commentsPage = collectionCommentRepository.findByCollection_Id(collectionId, pageable);
        List<CollectionCommentResponseDto> dtos = commentsPage.getContent().stream()
                .map(this::mapToCollectionCommentResponseDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, commentsPage.getTotalElements());
    }

    @Transactional
    public CollectionCommentResponseDto addCollectionComment(Long collectionId, CreateCommentRequestDto commentDto, Long currentUserId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + collectionId));
        if (currentUserId == null) {
            throw new ForbiddenActionException("User must be authenticated to comment.");
        }

        // UserResponseDto commenter = fetchUser(currentUserId);
        // if (commenter == null || commenter.getUsername().startsWith("Error:")) {
        //    throw new ResourceNotFoundException("Commenting user not found with id: " + currentUserId);
        // }

        CollectionComment comment = new CollectionComment();
        comment.setCollection(collection);
        comment.setUserId(currentUserId.intValue());
        comment.setCommentText(commentDto.getCommentText());
        comment.setCreatedAt(OffsetDateTime.now());

        CollectionComment savedComment = collectionCommentRepository.save(comment);
        return mapToCollectionCommentResponseDto(savedComment);
    }

    @Transactional
    public void deleteCollectionComment(Long collectionId, Integer commentId, Long currentUserId) {
        CollectionComment comment = collectionCommentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        if (!comment.getCollection().getId().equals(collectionId)) {
            throw new ResourceNotFoundException("Comment does not belong to collection " + collectionId);
        }
        boolean isCommentOwner = comment.getUserId().equals(currentUserId.intValue());
        boolean isCollectionOwner = comment.getCollection().getUserId().equals(currentUserId);

        if (!isCommentOwner && !isCollectionOwner) {
            throw new ForbiddenActionException("User does not have permission to delete this comment.");
        }
        collectionCommentRepository.delete(comment);
    }

    @Transactional
    public void likeCollection(Long collectionId, Long currentUserId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + collectionId));
        if (currentUserId == null) {
            throw new ForbiddenActionException("User must be authenticated to like a collection.");
        }

        CollectionLikeId likeId = new CollectionLikeId(currentUserId, collectionId);
        if (collectionLikeRepository.existsById(likeId)) {
            throw new ResourceAlreadyExistsException("Collection already liked by user.");
        }

        CollectionLike like = new CollectionLike();
        like.setId(likeId);
        like.setCollection(collection);
        like.setLikedAt(OffsetDateTime.now());
        collectionLikeRepository.save(like);
    }

    @Transactional
    public void unlikeCollection(Long collectionId, Long currentUserId) {
        if (!collectionRepository.existsById(collectionId)) {
            throw new ResourceNotFoundException("Collection not found with id: " + collectionId);
        }
        if (currentUserId == null) {
            throw new ForbiddenActionException("User must be authenticated to unlike a collection.");
        }
        CollectionLikeId likeId = new CollectionLikeId(currentUserId, collectionId);
        if (!collectionLikeRepository.existsById(likeId)) {
            throw new ResourceNotFoundException("Collection not liked by user.");
        }
        collectionLikeRepository.deleteById(likeId);
    }

    @Transactional(readOnly = true)
    public List<CollectionCollaboratorResponseDto> getCollectionCollaborators(Long collectionId) {
        if (!collectionRepository.existsById(collectionId)) {
            throw new ResourceNotFoundException("Collection not found with id: " + collectionId);
        }
        return collectionCollaboratorRepository.findCollectionCollaboratorsByCollectionId(collectionId).stream()
                .map(this::mapToCollectionCollaboratorResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CollectionCollaboratorResponseDto addCollectionCollaborator(Long collectionId, AddCollaboratorRequestDto collaboratorDto, Long ownerUserId) {
        authorizeCollectionOwnership(collectionId, ownerUserId);
        Collection collection = collectionRepository.findById(collectionId).get();

        UserResponseDto collaboratorUser = fetchUser(collaboratorDto.getUserId());
        if (collaboratorUser == null || collaboratorUser.getUsername().startsWith("Error:")) {
            throw new ResourceNotFoundException("Collaborator user not found with id: " + collaboratorDto.getUserId());
        }
        if (collection.getUserId().equals(collaboratorDto.getUserId())) {
            throw new IllegalArgumentException("Owner cannot be added as a collaborator.");
        }

        CollectionCollaboratorId ccId = new CollectionCollaboratorId(collectionId, collaboratorDto.getUserId());
        if (collectionCollaboratorRepository.existsById(ccId)) {
            throw new ResourceAlreadyExistsException("User is already a collaborator for this collection.");
        }

        CollectionCollaborator cc = new CollectionCollaborator();
        cc.setId(ccId);
        cc.setCollection(collection);
        // cc.setUserId(collaboratorDto.getUserId());
        cc.setRole(collaboratorDto.getRole());
        cc.setJoinedAt(OffsetDateTime.now());

        CollectionCollaborator savedCc = collectionCollaboratorRepository.save(cc);
        return mapToCollectionCollaboratorResponseDto(savedCc);
    }

    @Transactional
    public CollectionCollaboratorResponseDto updateCollectionCollaborator(Long collectionId, Long collaboratorUserId, UpdateCollaboratorRequestDto updateDto, Long ownerUserId) {
        authorizeCollectionOwnership(collectionId, ownerUserId);
        if (collectionRepository.findById(collectionId).get().getUserId().equals(collaboratorUserId)) {
            throw new IllegalArgumentException("Cannot change role of the collection owner.");
        }

        CollectionCollaboratorId ccId = new CollectionCollaboratorId(collectionId, collaboratorUserId);
        CollectionCollaborator cc = collectionCollaboratorRepository.findById(ccId)
                .orElseThrow(() -> new ResourceNotFoundException("Collaborator not found for this collection."));

        cc.setRole(updateDto.getRole());
        CollectionCollaborator updatedCc = collectionCollaboratorRepository.save(cc);
        return mapToCollectionCollaboratorResponseDto(updatedCc);
    }

    @Transactional
    public void removeCollectionCollaborator(Long collectionId, Long collaboratorUserId, Long ownerUserId) {
        authorizeCollectionOwnership(collectionId, ownerUserId);
        if (collectionRepository.findById(collectionId).get().getUserId().equals(collaboratorUserId)) {
            throw new IllegalArgumentException("Cannot remove the collection owner as a collaborator.");
        }
        CollectionCollaboratorId ccId = new CollectionCollaboratorId(collectionId, collaboratorUserId);
        if (!collectionCollaboratorRepository.existsById(ccId)) {
            throw new ResourceNotFoundException("Collaborator not found for this collection.");
        }
        collectionCollaboratorRepository.deleteById(ccId);
    }

    @Transactional
    public Page<CollectionResponseDto> getMyCollections(Long userId, Pageable pageable) {
        Page<Collection> collectionsPage = collectionRepository.findByUserId(userId, pageable);
        List<CollectionResponseDto> responseDtos = collectionsPage.getContent().stream()
                .map(this::mapToCollectionResponseDto)
                .collect(Collectors.toList());
        return new PageImpl<>(responseDtos, pageable, collectionsPage.getTotalElements());
    }
}