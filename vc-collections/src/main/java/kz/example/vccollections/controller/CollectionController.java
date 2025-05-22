package kz.example.vccollections.controller;

import jakarta.validation.Valid;
import kz.example.vccollections.dto.*;
import kz.example.vccollections.exception.UserNotAuthenticatedException;
import kz.example.vccollections.service.CollectionService;
import kz.example.vccollections.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;

@RestController
@RequestMapping("/collections")
@RequiredArgsConstructor
public class CollectionController {

    private static final Logger logger = LoggerFactory.getLogger(CollectionController.class);
    private final CollectionService collectionService;

    private Long getAuthenticatedUserIdOrThrow() {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            throw new UserNotAuthenticatedException("User not authenticated. Please login.");
        }
        return userId;
    }

    // Collections
    @GetMapping("/{id}")
    public ResponseEntity<CollectionResponseDto> getCollectionById(@PathVariable Long id) {
        return ResponseEntity.ok(collectionService.getCollectionDetailsById(id));
    }

    @GetMapping("/public-feed")
    public ResponseEntity<Page<CollectionResponseDto>> getPublicFeed(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(collectionService.getPublicFeed(pageable));
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<CollectionResponseDto>> getMyCollections(@PageableDefault(size = 10, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Long userId = getAuthenticatedUserIdOrThrow();
        return ResponseEntity.ok(collectionService.getMyCollections(userId, pageable));

    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CollectionResponseDto> createCollection(@Valid @RequestBody CreateCollectionRequestDto requestDto) {
        Long userId = getAuthenticatedUserIdOrThrow();
        CollectionResponseDto collectionResponseDto = collectionService.createCollection(requestDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(collectionResponseDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CollectionResponseDto> updateCollection(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCollectionRequestDto requestDto) {
        Long userId = getAuthenticatedUserIdOrThrow();
        CollectionResponseDto updatedCollection = collectionService.updateCollection(id, requestDto, userId);
        return ResponseEntity.ok(updatedCollection);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteCollection(@PathVariable Long id) {
        Long userId = getAuthenticatedUserIdOrThrow();
        collectionService.deleteCollection(id, userId);
        return ResponseEntity.noContent().build();
    }


    // Collection Items
    @GetMapping("/{collectionId}/items")
    public ResponseEntity<Page<CollectionItemEntryDto>> getCollectionItems(
            @PathVariable Long collectionId,
            @PageableDefault(size = 20, sort = "position", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(collectionService.getCollectionItems(collectionId, pageable));
    }

    @PostMapping("/{collectionId}/items")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CollectionItemEntryDto> addCollectionItem(
            @PathVariable Long collectionId,
            @Valid @RequestBody AddCollectionItemRequestDto itemDto) {
        Long userId = getAuthenticatedUserIdOrThrow();
        CollectionItemEntryDto newItem = collectionService.addCollectionItem(collectionId, itemDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newItem);
    }

    @PutMapping("/{collectionId}/items/{mediaItemId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CollectionItemEntryDto> updateCollectionItem(
            @PathVariable Long collectionId,
            @PathVariable Long mediaItemId,
            @Valid @RequestBody UpdateCollectionItemRequestDto itemDto) {
        Long userId = getAuthenticatedUserIdOrThrow();
        CollectionItemEntryDto updatedItem = collectionService.updateCollectionItem(collectionId, mediaItemId, itemDto, userId);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{collectionId}/items/{mediaItemId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removeCollectionItem(
            @PathVariable Long collectionId,
            @PathVariable Long mediaItemId) {
        Long userId = getAuthenticatedUserIdOrThrow();
        collectionService.removeCollectionItem(collectionId, mediaItemId, userId);
        return ResponseEntity.noContent().build();
    }


    // Collection Comments
    @GetMapping("/{collectionId}/comments")
    public ResponseEntity<Page<CollectionCommentResponseDto>> getCollectionComments(
            @PathVariable Long collectionId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(collectionService.getCollectionComments(collectionId, pageable));
    }

    @PostMapping("/{collectionId}/comments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CollectionCommentResponseDto> addCollectionComment(
            @PathVariable Long collectionId,
            @Valid @RequestBody CreateCommentRequestDto commentDto) {
        Long userId = getAuthenticatedUserIdOrThrow();
        CollectionCommentResponseDto newComment = collectionService.addCollectionComment(collectionId, commentDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newComment);
    }

    @DeleteMapping("/{collectionId}/comments/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteCollectionComment(
            @PathVariable Long collectionId,
            @PathVariable Integer commentId) {
        Long userId = getAuthenticatedUserIdOrThrow();
        collectionService.deleteCollectionComment(collectionId, commentId, userId);
        return ResponseEntity.noContent().build();
    }

    // Collection Likes
    @PostMapping("/{collectionId}/like")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> likeCollection(@PathVariable Long collectionId) {
        Long userId = getAuthenticatedUserIdOrThrow();
        collectionService.likeCollection(collectionId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{collectionId}/like")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> unlikeCollection(@PathVariable Long collectionId) {
        Long userId = getAuthenticatedUserIdOrThrow();
        collectionService.unlikeCollection(collectionId, userId);
        return ResponseEntity.noContent().build();
    }

    // Collection Collaborators
    @GetMapping("/{collectionId}/collaborators")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CollectionCollaboratorResponseDto>> getCollectionCollaborators(@PathVariable Long collectionId) {
        return ResponseEntity.ok(collectionService.getCollectionCollaborators(collectionId));
    }

    @PostMapping("/{collectionId}/collaborators")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CollectionCollaboratorResponseDto> addCollectionCollaborator(
            @PathVariable Long collectionId,
            @Valid @RequestBody AddCollaboratorRequestDto collaboratorDto) {
        Long userId = getAuthenticatedUserIdOrThrow();
        CollectionCollaboratorResponseDto newCollaborator = collectionService.addCollectionCollaborator(collectionId, collaboratorDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCollaborator);
    }

    @PutMapping("/{collectionId}/collaborators/{collaboratorUserId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CollectionCollaboratorResponseDto> updateCollectionCollaborator(
            @PathVariable Long collectionId,
            @PathVariable Long collaboratorUserId,
            @Valid @RequestBody UpdateCollaboratorRequestDto updateDto) {
        Long userId = getAuthenticatedUserIdOrThrow();
        CollectionCollaboratorResponseDto updatedCollaborator = collectionService.updateCollectionCollaborator(collectionId, collaboratorUserId, updateDto, userId);
        return ResponseEntity.ok(updatedCollaborator);
    }

    @DeleteMapping("/{collectionId}/collaborators/{collaboratorUserId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removeCollectionCollaborator(
            @PathVariable Long collectionId,
            @PathVariable Long collaboratorUserId) {
        Long userId = getAuthenticatedUserIdOrThrow();
        collectionService.removeCollectionCollaborator(collectionId, collaboratorUserId, userId);
        return ResponseEntity.noContent().build();
    }
}