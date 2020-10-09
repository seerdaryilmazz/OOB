package ekol.crm.quote.domain.model;

import ekol.crm.quote.domain.enumaration.ClearanceResponsible;
import ekol.crm.quote.domain.enumaration.CustomsClearanceType;
import ekol.model.IdNamePair;
import ekol.model.IsoNamePair;
import lombok.*;

import javax.persistence.*;

@Embeddable
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CustomsPoint {

    @Enumerated(EnumType.STRING)
    private ClearanceResponsible clearanceResponsible;

    @Enumerated(EnumType.STRING)
    private CustomsClearanceType clearanceType;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="id", column=@Column(name="LOCATION_ID")),
            @AttributeOverride(name = "name", column=@Column(name="LOCATION_NAME"))})
    private IdNamePair location;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="id", column=@Column(name="OFFICE_ID")),
            @AttributeOverride(name = "name", column=@Column(name="OFFICE_NAME"))})
    private IdNamePair office;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "iso", column = @Column(name = "COUNTRY_ISO")),
            @AttributeOverride(name = "name", column = @Column(name = "COUNTRY_NAME"))})
    private IsoNamePair customsLocationCountry;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "id", column = @Column(name = "POSTAL_CODE_ID")),
            @AttributeOverride(name = "name", column = @Column(name = "POSTAL_CODE_NAME"))
    })
    private IdNamePair customsLocationPostal;
}
