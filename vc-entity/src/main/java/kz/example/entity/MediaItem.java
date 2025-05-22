package kz.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "media_items")
public class MediaItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private ContentType type;

    @Size(max = 255)
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Size(max = 255)
    @Column(name = "creator")
    private String creator;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Size(max = 255)
    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Size(max = 255)
    @Column(name = "external_url")
    private String externalUrl;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "added_at")
    private OffsetDateTime addedAt;

}