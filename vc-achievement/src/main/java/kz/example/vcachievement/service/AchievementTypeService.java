package kz.example.vcachievement.service;

import kz.example.entity.AchievementType;
import kz.example.vcachievement.repository.AchievementTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AchievementTypeService {

    private AchievementTypeRepository repo;

    public AchievementType getAchievementTypeById(Integer id) {
        return repo.getAchievementTypeById(id);
    }
}
