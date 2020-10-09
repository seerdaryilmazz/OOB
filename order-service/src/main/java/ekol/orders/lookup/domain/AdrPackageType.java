package ekol.orders.lookup.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.LookupEntity;
import ekol.model.IdNamePair;
import ekol.orders.order.domain.dto.json.IdCodeNameTrio;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "AdrPackageType")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdrPackageType extends LookupEntity {

    @Id
    @SequenceGenerator(name = "seq_adrpackagetype", sequenceName = "seq_adrpackagetype")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_adrpackagetype")
    private Long id;

    public static AdrPackageType with(Long id, String name){
        AdrPackageType packageType = new AdrPackageType();
        packageType.setId(id);
        packageType.setName(name);
        return packageType;
    }
    public static AdrPackageType with(IdNamePair idName){
        return AdrPackageType.with(idName.getId(), idName.getName());
    }

    public static AdrPackageType with(Long id, String code, String name){
        AdrPackageType packageType = new AdrPackageType();
        packageType.setId(id);
        packageType.setCode(code);
        packageType.setName(name);
        return packageType;
    }
    public static AdrPackageType with(IdCodeNameTrio idCodeName){
        return AdrPackageType.with(idCodeName.getId(), idCodeName.getCode(), idCodeName.getName());
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
