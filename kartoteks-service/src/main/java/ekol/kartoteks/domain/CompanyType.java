package ekol.kartoteks.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.LookupEntity;

import javax.persistence.*;

/**
 * Created by fatmaozyildirim on 3/22/16.
 */
@Entity
@Table(name ="CompanyType")
@SequenceGenerator(name = "SEQ_COMPANYTYPE",sequenceName = "SEQ_COMPANYTYPE")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "deletedAt", "lastUpdated", "lastUpdatedBy"})
public class CompanyType extends LookupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_COMPANYTYPE")
    private Long id;

    @Override
    public boolean equals(Object other){
        if(other == null || !(other instanceof CompanyType)){
            return false;
        }
        return getCode().equals(((CompanyType) other).getCode());
    }
    @Override
    public int hashCode(){
        return getCode().hashCode();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}

