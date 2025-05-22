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
@Table(name = "content_types")
public class ContentType {
    @Id
    @ColumnDefault("nextval('content_types_type_id_seq')")
    @Column(name = "type_id", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "type_name", nullable = false, length = 50)
    private String typeName;

    @Size(max = 100)
    @Column(name = "type_icon", length = 100)
    private String typeIcon;

}