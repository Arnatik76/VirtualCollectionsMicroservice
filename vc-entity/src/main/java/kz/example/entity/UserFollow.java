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
@Table(name = "user_follows")
public class UserFollow {
    @EmbeddedId
    private UserFollowId id;

    @MapsId("followerId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    @MapsId("followedId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "followed_id", nullable = false)
    private User followed;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "followed_at")
    private OffsetDateTime followedAt;

}