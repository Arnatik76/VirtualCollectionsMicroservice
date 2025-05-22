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
@Table(name = "collection_items")
public class CollectionItem {
    @EmbeddedId
    private CollectionItemId id;

    @MapsId("collectionId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "collection_id", nullable = false)
    private Collection collection;

    @Column(name = "added_by_user_id")
    private Long addedByUserId;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "added_at")
    private OffsetDateTime addedAt;

    @Column(name = "\"position\"")
    private Integer position;

    @Column(name = "notes", length = Integer.MAX_VALUE)
    private String notes;

}