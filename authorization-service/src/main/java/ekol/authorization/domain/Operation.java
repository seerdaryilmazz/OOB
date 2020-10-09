package ekol.authorization.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.json.serializers.LocalDateDeserializer;
import ekol.json.serializers.LocalDateSerializer;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by ozer on 27/02/2017.
 */
@Entity
@Table(name = "operation")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Operation extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_operation", sequenceName = "seq_operation")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_operation")
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate createdAt;

    public Operation() {
        this.createdAt = LocalDate.now();
    }

    public Operation(String name) {
        this();
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}
