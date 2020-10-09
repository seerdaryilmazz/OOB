package ekol.orders.transportOrder.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.hibernate5.domain.embeddable.BigDecimalRange;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.orders.lookup.domain.PackageType;
import ekol.orders.transportOrder.domain.validator.BigDecimalRangeEmptyValidator;
import lombok.Data;
import lombok.EqualsAndHashCode;

@NamedEntityGraphs({
        @NamedEntityGraph(name = "PackageTypeRestriction.withPackageType", attributeNodes = {
                @NamedAttributeNode("packageType")
        })
})
@Entity
@Data
@Table(name = "PackageTypeRestriction")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper=true)
public class PackageTypeRestriction extends BaseEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5281583794027786139L;

	@Id
    @SequenceGenerator(name = "seq_package_type_restriction", sequenceName = "seq_package_type_restriction")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_package_type_restriction")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "packageTypeId")
    private PackageType packageType;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "minValue", column = @Column(name = "minGrossWeightKg")),
            @AttributeOverride(name = "maxValue", column = @Column(name = "maxGrossWeightKg"))
    })
    private BigDecimalRange grossWeightRangeInKilograms;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "minValue", column = @Column(name = "minNetWeightKg")),
            @AttributeOverride(name = "maxValue", column = @Column(name = "maxNetWeightKg"))
    })
    private BigDecimalRange netWeightRangeInKilograms;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "minValue", column = @Column(name = "minVolumeMtr3")),
            @AttributeOverride(name = "maxValue", column = @Column(name = "maxVolumeMtr3"))
    })
    private BigDecimalRange volumeRangeInCubicMeters;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "minValue", column = @Column(name = "minWidthCm")),
            @AttributeOverride(name = "maxValue", column = @Column(name = "maxWidthCm"))
    })
    private BigDecimalRange widthRangeInCentimeters;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "minValue", column = @Column(name = "minLengthCm")),
            @AttributeOverride(name = "maxValue", column = @Column(name = "maxLengthCm"))
    })
    private BigDecimalRange lengthRangeInCentimeters;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "minValue", column = @Column(name = "minHeightCm")),
            @AttributeOverride(name = "maxValue", column = @Column(name = "maxHeightCm"))
    })
    private BigDecimalRange heightRangeInCentimeters;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "minValue", column = @Column(name = "minLdm")),
            @AttributeOverride(name = "maxValue", column = @Column(name = "maxLdm"))
    })
    private BigDecimalRange ldmRange;

    @Column
    private boolean stackable;
    
    public boolean hasRestriction() {
    	return hasRestriction("grossWeightRangeInKilograms",
    			"netWeightRangeInKilograms",
    			"volumeRangeInCubicMeters",
    			"widthRangeInCentimeters",
    			"lengthRangeInCentimeters",
    			"heightRangeInCentimeters",
    			"ldmRange",
    			"stackable");
    }
    
    public boolean hasRestriction(String... fields) {
    	List<String> fieldList = Arrays.asList(fields);
    	boolean hasRestriction = fieldList.contains("stackable") && stackable;
    	for (String field : fieldList) {
			Object fieldObj = null;
			try{
				fieldObj = getClass().getDeclaredField(field).get(this);
			} catch(Exception e) {
				System.err.println(e.getMessage());
			}
			if(Objects.nonNull(fieldObj) && (fieldObj instanceof BigDecimalRange)) {
				hasRestriction = hasRestriction || BigDecimalRangeEmptyValidator.isNotEmpty((BigDecimalRange)fieldObj);
			} 
		}
    	return hasRestriction;
    			
    }
}
