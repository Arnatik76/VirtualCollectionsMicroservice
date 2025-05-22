package kz.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class MediaTagId implements Serializable {
    private static final long serialVersionUID = -6820192692278412817L;
    @NotNull
    @Column(name = "media_id", nullable = false)
    private Integer mediaId;

    @NotNull
    @Column(name = "tag_id", nullable = false)
    private Integer tagId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MediaTagId entity = (MediaTagId) o;
        return Objects.equals(this.tagId, entity.tagId) &&
                Objects.equals(this.mediaId, entity.mediaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagId, mediaId);
    }

}