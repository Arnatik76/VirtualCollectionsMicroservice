package kz.example.vccontent.service;

import kz.example.entity.ContentType;
import kz.example.vccontent.dto.ContentTypeDto;
import kz.example.vccontent.repository.ContentTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ContentTypeService {

    private final ContentTypeRepository contentTypeRepository;

    @Transactional(readOnly = true)
    public List<ContentTypeDto> getAllContentTypes() {
        return contentTypeRepository.findAll().stream()
                .map(this::mapToContentTypeDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ContentTypeDto getContentTypeById(Long id) {
        ContentType contentType = contentTypeRepository.findById(id.intValue())
                .orElseThrow(() -> new jakarta.ws.rs.NotFoundException("ContentType not found with id: " + id));
        return mapToContentTypeDto(contentType);
    }

    private ContentTypeDto mapToContentTypeDto(ContentType contentType) {
        if (contentType == null) return null;
        return new ContentTypeDto(
                contentType.getId(),
                contentType.getTypeName(),
                contentType.getTypeIcon()
        );
    }
}