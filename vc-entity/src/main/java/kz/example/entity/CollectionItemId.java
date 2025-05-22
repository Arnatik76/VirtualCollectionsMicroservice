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
public class CollectionItemId implements Serializable {
    private static final long serialVersionUID = -6388254770189090138L;
    @NotNull
    @Column(name = "collection_id", nullable = false)
    private Long collectionId;

    @NotNull
    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CollectionItemId entity = (CollectionItemId) o;
        return Objects.equals(this.itemId, entity.itemId) &&
                Objects.equals(this.collectionId, entity.collectionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, collectionId);
    }

}