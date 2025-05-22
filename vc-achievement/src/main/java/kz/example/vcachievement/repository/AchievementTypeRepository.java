package kz.example.vcachievement.repository;

import kz.example.entity.AchievementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementTypeRepository extends JpaRepository<AchievementType, Integer> {

    AchievementType getAchievementTypeById(Integer id);
}
