package kz.example.vccontent.service;

import kz.example.entity.*;
import kz.example.vccontent.dto.*;
import kz.example.vccontent.repository.ContentTypeRepository;
import kz.example.vccontent.repository.MediaItemsRepository;
import kz.example.vccontent.repository.MediaTagsRepository;
import kz.example.vccontent.repository.TagRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Date;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MediaItemService {

    private static final Logger log = LoggerFactory.getLogger(MediaItemService.class);
    private final MediaItemsRepository mediaItemsRepository;
    private final MediaTagsRepository mediaTagsRepository;
    private final ContentTypeRepository contentTypeRepository;
    private final TagRepository tagRepository;


    private MediaItemDto mapToMediaItemDto(MediaItem mediaItem) {
        if (mediaItem == null) return null;

        ContentTypeDto contentTypeDto = null;
        if (mediaItem.getType() != null) {
            ContentType ct = mediaItem.getType();
            contentTypeDto = new ContentTypeDto(ct.getId(), ct.getTypeName(), ct.getTypeIcon());
        }

        List<TagDto> tagDtos = Collections.emptyList();
        try {
            List<MediaTag> mediaTags = mediaTagsRepository.findByMedia_Id(mediaItem.getId());
            if (mediaTags != null && !mediaTags.isEmpty()) {
                tagDtos = mediaTags.stream()
                        .map(MediaTag::getTag)
                        .map(tag -> new TagDto(tag.getId(), tag.getTagName()))
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.error("Error fetching tags for media item {}: {}", mediaItem.getId(), e.getMessage());
        }

        Date releaseDateSql = mediaItem.getReleaseDate() != null ? Date.valueOf(mediaItem.getReleaseDate()) : null;

        return new MediaItemDto(
                mediaItem.getId().longValue(),
                mediaItem.getType() != null ? mediaItem.getType().getId() : null,
                mediaItem.getTitle(),
                mediaItem.getCreator(),
                mediaItem.getDescription(),
                mediaItem.getThumbnailUrl(),
                mediaItem.getExternalUrl(),
                releaseDateSql,
                mediaItem.getAddedAt(),
                contentTypeDto,
                tagDtos
        );
    }

    @Transactional(readOnly = true)
    public MediaItemDto getMediaItemDetailsById(Long mediaItemId) {
        log.debug("Fetching media item details for ID: {}", mediaItemId);
        MediaItem mediaItem = mediaItemsRepository.findById(mediaItemId)
                .orElseThrow(() -> {
                    log.warn("MediaItem not found with id: {}", mediaItemId);
                    return new NotFoundException("MediaItem not found with id: " + mediaItemId);
                });
        return mapToMediaItemDto(mediaItem);
    }

    @Transactional
    public MediaItemDto createMediaItem(CreateMediaItemRequestDto requestDto) {
        ContentType contentType = contentTypeRepository.findById(requestDto.getTypeId().intValue())
                .orElseThrow(() -> new NotFoundException("ContentType not found with id: " + requestDto.getTypeId()));

        MediaItem mediaItem = new MediaItem();
        mediaItem.setTitle(requestDto.getTitle());
        mediaItem.setType(contentType);
        mediaItem.setCreator(requestDto.getCreator());
        mediaItem.setDescription(requestDto.getDescription());
        mediaItem.setThumbnailUrl(requestDto.getThumbnailUrl());
        mediaItem.setExternalUrl(requestDto.getExternalUrl());
        mediaItem.setReleaseDate(requestDto.getReleaseDate());
        mediaItem.setAddedAt(OffsetDateTime.now());
        // mediaItem.setCreatedByUserId(authenticatedUserId);

        MediaItem savedMediaItem = mediaItemsRepository.save(mediaItem);

        if (requestDto.getTags() != null && !requestDto.getTags().isEmpty()) {
            updateMediaItemTags(savedMediaItem, requestDto.getTags());
        }

        log.info("Created media item with ID: {}", savedMediaItem.getId());
        return mapToMediaItemDto(savedMediaItem);
    }

    @Transactional
    public MediaItemDto updateMediaItem(Long itemId, UpdateMediaItemRequestDto requestDto) {
        MediaItem mediaItem = mediaItemsRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("MediaItem not found with id: " + itemId));

        boolean updated = false;
        if (StringUtils.hasText(requestDto.getTitle()) && !requestDto.getTitle().equals(mediaItem.getTitle())) {
            mediaItem.setTitle(requestDto.getTitle());
            updated = true;
        }
        if (requestDto.getCreator() != null && !requestDto.getCreator().equals(mediaItem.getCreator())) {
            mediaItem.setCreator(requestDto.getCreator());
            updated = true;
        }
        if (requestDto.getDescription() != null && !requestDto.getDescription().equals(mediaItem.getDescription())) {
            mediaItem.setDescription(requestDto.getDescription());
            updated = true;
        }
        if (requestDto.getThumbnailUrl() != null && !requestDto.getThumbnailUrl().equals(mediaItem.getThumbnailUrl())) {
            mediaItem.setThumbnailUrl(requestDto.getThumbnailUrl());
            updated = true;
        }
        if (requestDto.getExternalUrl() != null && !requestDto.getExternalUrl().equals(mediaItem.getExternalUrl())) {
            mediaItem.setExternalUrl(requestDto.getExternalUrl());
            updated = true;
        }
        if (requestDto.getReleaseDate() != null && !requestDto.getReleaseDate().equals(mediaItem.getReleaseDate())) {
            mediaItem.setReleaseDate(requestDto.getReleaseDate());
            updated = true;
        }

        if (requestDto.getTags() != null) {
            updateMediaItemTags(mediaItem, requestDto.getTags());
            updated = true;
        }


        if (updated) {
            // mediaItem.setUpdatedAt(OffsetDateTime.now());
            MediaItem updatedMediaItem = mediaItemsRepository.save(mediaItem);
            log.info("Updated media item with ID: {}", updatedMediaItem.getId());
            return mapToMediaItemDto(updatedMediaItem);
        }
        log.info("No changes detected for media item with ID: {}", itemId);
        return mapToMediaItemDto(mediaItem);
    }

    @Transactional
    public void deleteMediaItem(Long itemId) {
        MediaItem mediaItem = mediaItemsRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("MediaItem not found with id: " + itemId));

        List<MediaTag> mediaTags = mediaTagsRepository.findByMedia_Id(mediaItem.getId());
        mediaTagsRepository.deleteAll(mediaTags);

        mediaItemsRepository.delete(mediaItem);
        log.info("Deleted media item with ID: {}", itemId);
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<MediaItemDto> searchMediaItems(String query, String typeName, Pageable pageable) {
        Page<MediaItem> page;
        if (StringUtils.hasText(typeName)) {
            log.warn("Search by typeName ('{}') is not fully implemented in this basic example, searching by title only.", typeName);
            page = mediaItemsRepository.findAll(pageable);
        } else {
            // page = mediaItemsRepository.findByTitleContainingIgnoreCase(query, pageable);
            page = mediaItemsRepository.findAll(pageable);
        }

        if (query.equalsIgnoreCase("testall")) {
            page = mediaItemsRepository.findAll(pageable);
        } else {
            log.warn("Search query '{}' not specifically handled, returning empty page for demo. Implement proper search in repository.", query);
            List<MediaItemDto> emptyList = Collections.emptyList();
            page = new PageImpl<>(Collections.emptyList(), pageable, 0);
        }


        List<MediaItemDto> dtoList = page.getContent().stream()
                .map(this::mapToMediaItemDto)
                .collect(Collectors.toList());
        return new PagedResponseDto<>(new PageImpl<>(dtoList, pageable, page.getTotalElements()));
    }

    private void updateMediaItemTags(MediaItem mediaItem, List<String> tagNames) {
        List<MediaTag> existingMediaTags = mediaTagsRepository.findByMedia_Id(mediaItem.getId());
        mediaTagsRepository.deleteAll(existingMediaTags);

        List<MediaTag> newMediaTags = new ArrayList<>();
        for (String tagName : tagNames) {
            Tag tag = tagRepository.findByTagNameIgnoreCase(tagName)
                    .orElseGet(() -> {
                        Tag newTag = new Tag();
                        newTag.setTagName(tagName);
                        return tagRepository.save(newTag);
                    });
            MediaTag mediaTag = new MediaTag();
            mediaTag.setId(new MediaTagId(mediaItem.getId(), tag.getId().intValue()));
            mediaTag.setMedia(mediaItem);
            mediaTag.setTag(tag);
            newMediaTags.add(mediaTag);
        }
        if (!newMediaTags.isEmpty()) {
            mediaTagsRepository.saveAll(newMediaTags);
        }
    }
}