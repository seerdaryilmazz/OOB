package ekol.crm.quote.domain.model;

import lombok.*;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Table(name = "CrmCustoms")
@SequenceGenerator(name = "SEQ_CRMCUSTOMS", sequenceName = "SEQ_CRMCUSTOMS")
@Where(clause = "deleted = 0")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class Customs extends AuditedBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CRMCUSTOMS")
    private Long id;
    
    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="clearanceResponsible", column=@Column(name="DEP_CLEARANCE_RESPONSIBLE")),
            @AttributeOverride(name="clearanceType", column=@Column(name="DEP_CLEARANCE_TYPE")),
            @AttributeOverride(name="location.id", column=@Column(name="DEP_LOCATION_ID")),
            @AttributeOverride(name = "location.name", column=@Column(name="DEP_LOCATION_NAME")),
            @AttributeOverride(name="office.id", column=@Column(name="DEP_OFFICE_ID")),
            @AttributeOverride(name = "office.name", column=@Column(name="DEP_OFFICE_NAME")),
            @AttributeOverride(name = "customsLocationCountry.iso", column=@Column(name="DEP_COUNTRY_ISO")),
            @AttributeOverride(name = "customsLocationCountry.name", column=@Column(name="DEP_COUNTRY_NAME")),
            @AttributeOverride(name = "customsLocationPostal.id", column=@Column(name="DEP_POINT_ID")),
            @AttributeOverride(name = "customsLocationPostal.name", column=@Column(name="DEP_POINT_NAME"))})
    private CustomsPoint departure;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="clearanceResponsible", column=@Column(name="ARR_CLEARANCE_RESPONSIBLE")),
            @AttributeOverride(name="clearanceType", column=@Column(name="ARR_CLEARANCE_TYPE")),
            @AttributeOverride(name="location.id", column=@Column(name="ARR_LOCATION_ID")),
            @AttributeOverride(name = "location.name", column=@Column(name="ARR_LOCATION_NAME")),
            @AttributeOverride(name="office.id", column=@Column(name="ARR_OFFICE_ID")),
            @AttributeOverride(name = "office.name", column=@Column(name="ARR_OFFICE_NAME")),
            @AttributeOverride(name = "customsLocationCountry.iso", column=@Column(name="ARR_COUNTRY_ISO")),
            @AttributeOverride(name = "customsLocationCountry.name", column=@Column(name="ARR_COUNTRY_NAME")),
            @AttributeOverride(name = "customsLocationPostal.id", column=@Column(name="ARR_POINT_ID")),
            @AttributeOverride(name = "customsLocationPostal.name", column=@Column(name="ARR_POINT_NAME"))})
    private CustomsPoint arrival;

}
