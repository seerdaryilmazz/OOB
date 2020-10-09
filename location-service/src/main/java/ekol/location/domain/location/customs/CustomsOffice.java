package ekol.location.domain.location.customs;

import java.util.*;

import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.location.domain.Country;
import ekol.location.domain.location.comnon.ExternalSystemId;
import ekol.location.domain.location.comnon.ExternalSystemId.CollectionDeserializer;
import ekol.location.domain.location.comnon.ExternalSystemId.EntityNameValue;
import lombok.Data;
import lombok.EqualsAndHashCode;

@NamedEntityGraphs({
        @NamedEntityGraph(name = "CustomsOffice.withLocationsAndContacts",
                attributeNodes = {
                        @NamedAttributeNode("locations"),
                        @NamedAttributeNode("contacts")
                }
        )
})
@Entity
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper=false)
public class CustomsOffice extends BaseEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = -7198700208550805455L;

	@Id
    @SequenceGenerator(name = "seq_customs_office", sequenceName = "seq_customs_office")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_customs_office")
    private Long id;

    @Column
    private String name;

    @Column
    private String localName;

    @Column
    private String shortName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "countryId")
    private Country country;

    @Column
    private String customsCode;

    @Column
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean active;

    @Column
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean freeZone;

    @Column
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean borderCustoms;

    @OneToMany(mappedBy = "customsOffice", fetch = FetchType.LAZY)
    @Where(clause = "deleted = 0")
    @JsonManagedReference("locations")
    private Set<CustomsOfficeLocation> locations = new HashSet<>();

    @OneToMany(mappedBy = "customsOffice", fetch = FetchType.LAZY)
    @Where(clause = "deleted = 0")
    @JsonManagedReference("contacts")
    private Set<CustomsOfficeContact> contacts = new HashSet<>();
    
    @Embedded
    @ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "ExternalSystemId", joinColumns = {@JoinColumn(name = "parentId")})
    @AttributeOverrides({
    	@AttributeOverride(name="entityName", column=@Column(name="ENTITY_NAME")),
    	@AttributeOverride(name="externalSystem", column=@Column(name="EXTERNAL_SYSTEM")),
    	@AttributeOverride(name="externalId", column=@Column(name="EXTERNAL_ID"))
    })
    @Where(clause="ENTITY_NAME = 'CustomsOffice'")
    @EntityNameValue("CustomsOffice")
    @JsonDeserialize(using=CollectionDeserializer.class)
    private Collection<ExternalSystemId> externalIds;
    
    public Specification<CustomsOffice> toNameSpacifition(){
		Builder builder = new Builder();
		if(StringUtils.isNotEmpty(getName())){
			builder.or(equals(getName(), CustomsOffice_.name));
		}
		if(StringUtils.isNotEmpty(getLocalName())){
			builder.or(equals(getLocalName(), CustomsOffice_.localName));
		}
		if(StringUtils.isNotEmpty(getShortName())){
			builder.or(equals(getShortName(), CustomsOffice_.shortName));
		}
		return builder.build();
	}
	
	private <T> Specification<CustomsOffice> equals(T name, SingularAttribute<CustomsOffice, T> attribute) {
		return (Root<CustomsOffice> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->  criteriaBuilder.equal(root.get(attribute),name);
	}

	private static class Builder{

		Specifications<CustomsOffice> result = null;

		public void or(Specification<CustomsOffice> spec){
			result = result == null ? Specifications.where(spec) : result.or(spec);
		}

		public Specification<CustomsOffice> build(){
			return result;
		}
	}

}
