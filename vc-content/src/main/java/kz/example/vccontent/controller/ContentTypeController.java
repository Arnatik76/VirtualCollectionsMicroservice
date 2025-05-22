package kz.example.vccontent.controller;

import kz.example.vccontent.dto.ContentTypeDto;
import kz.example.vccontent.service.ContentTypeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/content-types")
public class ContentTypeController {

    private final ContentTypeService contentTypeService;

    @GetMapping
    public ResponseEntity<List<ContentTypeDto>> getAllContentTypes() {
        List<ContentTypeDto> contentTypes = contentTypeService.getAllContentTypes();
        return ResponseEntity.ok(contentTypes);
    }
}