package kz.example.vccontent.controller;

import jakarta.validation.Valid;
import kz.example.vccontent.dto.CreateMediaItemRequestDto;
import kz.example.vccontent.dto.MediaItemDto;
import kz.example.vccontent.dto.PagedResponseDto;
import kz.example.vccontent.dto.UpdateMediaItemRequestDto;
import kz.example.vccontent.service.MediaItemService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/media-items")
public class MediaItemController {

    private final MediaItemService mediaItemService;

    @GetMapping("/{id}")
    public ResponseEntity<MediaItemDto> getMediaItemById(@PathVariable("id") Long id) {
        MediaItemDto mediaItemDto = mediaItemService.getMediaItemDetailsById(id);
        return ResponseEntity.ok(mediaItemDto);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MediaItemDto> createMediaItem(@Valid @RequestBody CreateMediaItemRequestDto requestDto) {
        // Long authenticatedUserId = SecurityUtil.getCurrentUserId();
        MediaItemDto createdMediaItem = mediaItemService.createMediaItem(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMediaItem);
    }

    @PutMapping("/{itemId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MediaItemDto> updateMediaItem(
            @PathVariable("itemId") Long itemId,
            @Valid @RequestBody UpdateMediaItemRequestDto requestDto) {
        // Long authenticatedUserId = SecurityUtil.getCurrentUserId();
        MediaItemDto updatedMediaItem = mediaItemService.updateMediaItem(itemId, requestDto);
        return ResponseEntity.ok(updatedMediaItem);
    }

    @DeleteMapping("/{itemId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteMediaItem(@PathVariable("itemId") Long itemId) {
        // Long authenticatedUserId = SecurityUtil.getCurrentUserId();
        mediaItemService.deleteMediaItem(itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<PagedResponseDto<MediaItemDto>> searchMediaItems(
            @RequestParam("query") String query,
            @RequestParam(required = false) String type,
            @PageableDefault(size = 15) Pageable pageable) {
        PagedResponseDto<MediaItemDto> results = mediaItemService.searchMediaItems(query, type, pageable);
        return ResponseEntity.ok(results);
    }
}