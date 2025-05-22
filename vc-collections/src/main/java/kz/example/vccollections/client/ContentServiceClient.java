package kz.example.vccollections.client;

import kz.example.vccollections.dto.MediaItemDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "vc-content", path = "/media-items")
public interface ContentServiceClient {

    @GetMapping("/{id}")
    MediaItemDto getMediaItemById(@PathVariable("id") Long id);
}