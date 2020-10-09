package ekol.kartoteks.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.hibernate5.domain.entity.LookupEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Created by fatmaozyildirim on 5/5/16.
 */
@Entity
@Getter
@Setter
@Table(name ="BusinessSegmentType")
@Where(clause = "deleted = 0")
@SequenceGenerator(name = "SEQ_BUSINESSSEGMENTTYPE",sequenceName = "SEQ_BUSINESSSEGMENTTYPE")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "deletedAt", "lastUpdated", "lastUpdatedBy"})
public class BusinessSegmentType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_BUSINESSSEGMENTTYPE")
    private Long id;

    private String code;

    private String name;

    private Integer rank;

}
