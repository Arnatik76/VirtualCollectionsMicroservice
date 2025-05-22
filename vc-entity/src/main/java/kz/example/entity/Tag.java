package kz.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @ColumnDefault("nextval('tags_tag_id_seq')")
    @Column(name = "tag_id", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "tag_name", nullable = false, length = 50)
    private String tagName;

}