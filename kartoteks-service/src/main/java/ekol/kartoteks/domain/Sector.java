package ekol.kartoteks.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Created by fatmaozyildirim on 3/14/16.
 */

@Entity
@Table(name = "Sector")
@SequenceGenerator(name = "SEQ_SECTOR", sequenceName = "SEQ_SECTOR")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "deletedAt", "lastUpdated", "lastUpdatedBy"})
public class Sector extends BaseEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_SECTOR")
    private Long id ;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private Sector parent;


    @Column(nullable = false)
    private String name;


    @Column(nullable = false, length = 10)
    private String code;

    public Sector() {
        //default constructor
    }

    public Sector(Long id, Sector parent, String name) {
        this.id = id;
        this.parent = parent;
        this.name = name;
    }

    public Sector getParent() {
        return parent;
    }

    public void setParent(Sector parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
