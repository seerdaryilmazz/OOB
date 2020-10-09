package ekol.orders.search.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Created by ozer on 24/10/16.
 */
@Entity
@Table(name = "saved_search")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SavedSearch extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_saved_search", sequenceName = "seq_saved_search")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_saved_search")
    private Long id;

    @Column
    private Long userId;

    @Column
    private String name;

    @Column
    private String filter;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}
