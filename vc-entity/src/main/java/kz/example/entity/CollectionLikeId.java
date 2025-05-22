package kz.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CollectionLikeId implements Serializable {
    private static final long serialVersionUID = -246805645127378855L;
    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "collection_id", nullable = false)
    private Long collectionId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CollectionLikeId entity = (CollectionLikeId) o;
        return Objects.equals(this.userId, entity.userId) &&
                Objects.equals(this.collectionId, entity.collectionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, collectionId);
    }

}