package ekol.orders.lookup.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.LookupEntity;
import ekol.model.IdNamePair;
import ekol.orders.order.domain.dto.json.IdCodeNameTrio;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Created by kilimci on 18/08/16.
 */

@Entity
@Table(name = "PackageType")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PackageType extends LookupEntity {

    @Id
    @SequenceGenerator(name = "seq_packagetype", sequenceName = "seq_packagetype")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_packagetype")
    private Long id;

    @Transient
    private boolean hasRestriction;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "packageGroupId")
    private PackageGroup packageGroup;

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    private Integer rank;

    public static PackageType with(Long id, String name){
        PackageType packageType = new PackageType();
        packageType.setId(id);
        packageType.setName(name);
        return packageType;
    }
    public static PackageType with(IdNamePair idName){
        return PackageType.with(idName.getId(), idName.getName());
    }

    public static PackageType with(Long id, String code, String name){
    	PackageType packageType = new PackageType();
    	packageType.setId(id);
    	packageType.setCode(code);
    	packageType.setName(name);
    	return packageType;
    }
    public static PackageType with(IdCodeNameTrio idCodeName){
    	return PackageType.with(idCodeName.getId(), idCodeName.getCode(), idCodeName.getName());
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public boolean getHasRestriction() {
        return hasRestriction;
    }

    public void setHasRestriction(boolean hasRestriction) {
        this.hasRestriction = hasRestriction;
    }

    public PackageGroup getPackageGroup() {
        return packageGroup;
    }

    public void setPackageGroup(PackageGroup packageGroup) {
        this.packageGroup = packageGroup;
    }

    public boolean isHangingPackageType(){
        return getCode() != null && getCode().equalsIgnoreCase("stange");
    }
}
