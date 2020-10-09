package ekol.orders.order.domain;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.hibernate.annotations.Type;

import lombok.Data;

@Data
@Embeddable
public class ShipmentHandlingParty {

    @Embedded
    private IdNameEmbeddable company;

    @Embedded
    private IdNameEmbeddable companyLocation;
    
    @Embedded
    private IdNameEmbeddable companyContact;

    @Embedded
    private IdNameEmbeddable handlingCompany;

    @Embedded
    private IdNameEmbeddable handlingLocation;

    @Embedded
    private IdNameEmbeddable handlingContact;

    @Embedded
    private IdNameEmbeddable handlingRegion;

    @Embedded
    private CodeNameEmbeddable handlingRegionCategory;

    @Embedded
    private IdNameEmbeddable handlingOperationRegion;

    private String handlingLocationCountryCode;

    private String handlingLocationPostalCode;

    private String handlingLocationTimezone;

    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean handlingAtCrossDock;

    @Enumerated(EnumType.STRING)
    private CompanyType handlingCompanyType;
    
    public boolean isHandlingLocationTR(){
        final String tr = "TR";
        return tr.equalsIgnoreCase(getHandlingLocationCountryCode());
    }
}
