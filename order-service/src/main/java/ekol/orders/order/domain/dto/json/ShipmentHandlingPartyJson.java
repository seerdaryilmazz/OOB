package ekol.orders.order.domain.dto.json;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import ekol.orders.order.domain.CodeNameEmbeddable;
import ekol.orders.order.domain.CompanyType;
import ekol.orders.order.domain.IdNameEmbeddable;
import ekol.orders.order.domain.ShipmentHandlingParty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipmentHandlingPartyJson {

    private IdNamePair company;
    private IdNamePair companyLocation;
    private IdNamePair companyContact;
    private IdNamePair handlingCompany;
    private IdNamePair handlingLocation;
    private IdNamePair handlingContact;
    private IdNamePair handlingRegion;
    private CodeNamePair handlingRegionCategory;
    private IdNamePair handlingOperationRegion;
    private boolean handlingAtCrossDock;
    private String handlingLocationCountryCode;
    private String handlingLocationPostalCode;
    private String handlingLocationTimezone;
    private CompanyType handlingCompanyType;

    public static ShipmentHandlingPartyJson fromEntity(ShipmentHandlingParty handlingParty){
        ShipmentHandlingPartyJson json = new ShipmentHandlingPartyJson();
        json.setCompany(handlingParty.getCompany().toIdNamePair());
        json.setCompanyLocation(Optional.ofNullable(handlingParty.getCompanyLocation()).map(IdNameEmbeddable::toIdNamePair).orElse(null));
        if(handlingParty.getCompanyContact() != null){
            json.setCompanyContact(handlingParty.getCompanyContact().toIdNamePair());
        }
        json.setHandlingCompany(handlingParty.getHandlingCompany().toIdNamePair());
        json.setHandlingLocation(handlingParty.getHandlingLocation().toIdNamePair());
        json.setHandlingCompanyType(handlingParty.getHandlingCompanyType());
        if(handlingParty.getHandlingContact() != null){
            json.setHandlingContact(handlingParty.getHandlingContact().toIdNamePair());
        }
        if(handlingParty.getHandlingRegion() != null){
            json.setHandlingRegion(handlingParty.getHandlingRegion().toIdNamePair());
        }
        if(handlingParty.getHandlingRegionCategory() != null){
            json.setHandlingRegionCategory(handlingParty.getHandlingRegionCategory().toCodeNamePair());
        }
        if(handlingParty.getHandlingOperationRegion() != null){
            json.setHandlingOperationRegion(handlingParty.getHandlingOperationRegion().toIdNamePair());
        }
        json.setHandlingAtCrossDock(handlingParty.isHandlingAtCrossDock());
        json.setHandlingLocationCountryCode(handlingParty.getHandlingLocationCountryCode());
        json.setHandlingLocationPostalCode(handlingParty.getHandlingLocationPostalCode());
        json.setHandlingLocationTimezone(handlingParty.getHandlingLocationTimezone());
        return json;
    }

    public ShipmentHandlingParty toEntity(){
        ShipmentHandlingParty handlingParty = new ShipmentHandlingParty();
        handlingParty.setCompany(IdNameEmbeddable.with(getCompany()));
        handlingParty.setCompanyLocation(Optional.ofNullable(getCompanyLocation()).map(IdNameEmbeddable::with).orElse(null));
        if(getCompanyContact() != null && getCompanyContact().getId() != null){
            handlingParty.setCompanyContact(IdNameEmbeddable.with(getCompanyContact()));
        }
        handlingParty.setHandlingCompany(IdNameEmbeddable.with(getHandlingCompany()));
        handlingParty.setHandlingLocation(IdNameEmbeddable.with(getHandlingLocation()));
        handlingParty.setHandlingCompanyType(getHandlingCompanyType());
        if(getHandlingContact() != null && getHandlingContact().getId() != null){
            handlingParty.setHandlingContact(IdNameEmbeddable.with(getHandlingContact()));
        }
        if(getHandlingRegion() != null && getHandlingRegion().getId() != null){
            handlingParty.setHandlingRegion(IdNameEmbeddable.with(getHandlingRegion()));
        }
        if(getHandlingRegionCategory() != null && getHandlingRegionCategory().getCode() != null){
            handlingParty.setHandlingRegionCategory(CodeNameEmbeddable.with(getHandlingRegionCategory()));
        }
        if(getHandlingOperationRegion() != null && getHandlingOperationRegion().getId() != null){
            handlingParty.setHandlingOperationRegion(IdNameEmbeddable.with(getHandlingOperationRegion()));
        }
        handlingParty.setHandlingAtCrossDock(isHandlingAtCrossDock());
        handlingParty.setHandlingLocationCountryCode(getHandlingLocationCountryCode());
        handlingParty.setHandlingLocationPostalCode(getHandlingLocationPostalCode());
        handlingParty.setHandlingLocationTimezone(getHandlingLocationTimezone());
        return handlingParty;
    }

}
