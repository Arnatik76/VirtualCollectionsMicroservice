package kz.example.vccollections.client;

import kz.example.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "vc-users", path = "/users")
public interface UserServiceClient {

    @GetMapping("/{userId}")
    UserResponseDto getUserById(@PathVariable("userId") Long userId);

    @PostMapping("/batch")
    List<UserResponseDto> getUsersByIds(@RequestBody List<Long> userIds);
}