package ekol.orders.lookup.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.hibernate5.domain.entity.LookupEntity;
import ekol.model.IdNamePair;
import ekol.orders.order.domain.dto.json.IdCodeNameTrio;

/**
 * Created by burak on 25/01/17.
 */
@Entity
@Table(name = "PackageGroup")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PackageGroup extends LookupEntity {

    @Id
    @SequenceGenerator(name = "seq_packagegroup", sequenceName = "seq_packagegroup")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_packagegroup")
    private Long id;

    @OneToMany(mappedBy = "packageGroup")
    @Where(clause = "deleted = 0")
    @JsonIgnore
    private Set<PackageType> packageTypes = new HashSet<>();

    public static PackageGroup with(Long id, String name){
        PackageGroup packageGroup = new PackageGroup();
        packageGroup.setId(id);
        packageGroup.setName(name);
        return packageGroup;
    }
    public static PackageGroup with(IdNamePair idName){
        return PackageGroup.with(idName.getId(), idName.getName());
    }

    public static PackageGroup with(Long id, String code, String name){
    	PackageGroup packageGroup = new PackageGroup();
    	packageGroup.setId(id);
    	packageGroup.setCode(code);
    	packageGroup.setName(name);
    	return packageGroup;
    }
    public static PackageGroup with(IdCodeNameTrio idCodeName){
    	return PackageGroup.with(idCodeName.getId(), idCodeName.getCode(), idCodeName.getName());
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }


    public Set<PackageType> getPackageTypes() {
        return packageTypes;
    }

    public void setPackageTypes(Set<PackageType> packageTypes) {
        this.packageTypes = packageTypes;
    }
}
