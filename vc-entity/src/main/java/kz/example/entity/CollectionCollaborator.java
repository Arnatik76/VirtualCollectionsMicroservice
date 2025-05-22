package kz.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "collection_collaborators")
public class CollectionCollaborator {
    @EmbeddedId
    private CollectionCollaboratorId id;

    @MapsId("collectionId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "collection_id", nullable = false)
    private Collection collection;

    @Size(max = 20)
    @NotNull
    @ColumnDefault("'editor'")
    @Column(name = "role", nullable = false, length = 20)
    private String role;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "joined_at")
    private OffsetDateTime joinedAt;

    public Long getUserId() {
        return this.id.getUserId();
    }
}