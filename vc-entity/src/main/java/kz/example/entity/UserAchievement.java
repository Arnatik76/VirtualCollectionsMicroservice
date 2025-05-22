package kz.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_achievements")
public class UserAchievement {
    @EmbeddedId
    private UserAchievementId id;

    @MapsId("achievementId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "achievement_id", nullable = false)
    private AchievementType achievement;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "achieved_at")
    private OffsetDateTime achievedAt;

}