package kz.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "collections")
public class Collection {
    @Id
    @ColumnDefault("nextval('collections_collection_id_seq')")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "collection_id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Size(max = 255)
    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Column(name = "user_id")
    private Long userId;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @ColumnDefault("false")
    @Column(name = "is_public")
    private Boolean isPublic;

    @ColumnDefault("0")
    @Column(name = "view_count")
    private Integer viewCount;

}