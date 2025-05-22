package kz.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Data
@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class CollectionCollaboratorId implements Serializable {
    private static final long serialVersionUID = -7220198896444755302L;
    @NotNull
    @Column(name = "collection_id", nullable = false)
    private Long collectionId;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CollectionCollaboratorId entity = (CollectionCollaboratorId) o;
        return Objects.equals(this.collectionId, entity.collectionId) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collectionId, userId);
    }

}