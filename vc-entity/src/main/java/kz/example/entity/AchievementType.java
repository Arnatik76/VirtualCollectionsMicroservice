package kz.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "achievement_types")
public class AchievementType {
    @Id
    @ColumnDefault("nextval('achievement_types_achievement_id_seq')")
    @Column(name = "achievement_id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Size(max = 255)
    @Column(name = "icon_url")
    private String iconUrl;

    @Size(max = 255)
    @Column(name = "requirement")
    private String requirement;

}