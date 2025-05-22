package kz.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
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
@AllArgsConstructor
@NoArgsConstructor
public class UserFollowId implements Serializable {
    private static final long serialVersionUID = -6121776076760067252L;
    @jakarta.validation.constraints.NotNull
    @Column(name = "follower_id", nullable = false)
    private Integer followerId;

    @jakarta.validation.constraints.NotNull
    @Column(name = "followed_id", nullable = false)
    private Integer followedId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserFollowId entity = (UserFollowId) o;
        return Objects.equals(this.followerId, entity.followerId) &&
                Objects.equals(this.followedId, entity.followedId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(followerId, followedId);
    }

}