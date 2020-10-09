package ekol.kartoteks.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Created by fatmaozyildirim on 3/17/16.
 */
@Entity
@Table(name = "TaxOffice")
@SequenceGenerator(name = "SEQ_TAXOFFICE", sequenceName = "SEQ_TAXOFFICE")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "deletedAt", "lastUpdated", "lastUpdatedBy"})
public class TaxOffice extends BaseEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_TAXOFFICE")
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 6)
    private String code;

    @Column(nullable = false, length = 100)
    private String cityCode;

    @Column(nullable = false, length = 2)
    private String countryCode;

    public TaxOffice() {
        //Default Constructor
    }

    @Override
    public boolean equals(Object other){
        if(other == null || !(other instanceof TaxOffice)){
            return false;
        }
        return getCode().equals(((TaxOffice) other).getCode());
    }
    @Override
    public int hashCode(){
        return getCode().hashCode();
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

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
