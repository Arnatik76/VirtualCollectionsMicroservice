package kz.example.vcachievement.controller;

import kz.example.vcachievement.service.AchievementTypeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/achievement-types")
public class AchievementTypeController {

    private AchievementTypeService achievementTypeService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getAchievementTypeById(@PathVariable Integer id) {
        return ResponseEntity.ok(achievementTypeService.getAchievementTypeById(id));
    }
}
