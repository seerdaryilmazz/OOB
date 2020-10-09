package ekol.orders.order.domain.dto.json;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.model.IdNamePair;
import ekol.orders.order.domain.IdNameEmbeddable;
import ekol.orders.order.domain.ShipmentParty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipmentPartyJson {
	private IdNamePair company;
    private IdNamePair companyLocation;
    private IdNamePair companyContact;
    
    public static ShipmentPartyJson fromEntity(ShipmentParty party){
    	ShipmentPartyJson json = new ShipmentPartyJson();
        json.setCompany(Optional.ofNullable(party).map(ShipmentParty::getCompany).map(IdNameEmbeddable::toIdNamePair).orElse(null));
        json.setCompanyLocation(Optional.ofNullable(party).map(ShipmentParty::getCompanyLocation).map(IdNameEmbeddable::toIdNamePair).orElse(null));
        json.setCompanyContact(Optional.ofNullable(party).map(ShipmentParty::getCompanyContact).map(IdNameEmbeddable::toIdNamePair).orElse(null));
        return json;
    }
    
    public ShipmentParty toEntity(){
    	ShipmentParty entity = new ShipmentParty();
    	entity.setCompany(Optional.ofNullable(getCompany()).map(IdNameEmbeddable::with).orElse(null));
    	entity.setCompanyLocation(Optional.ofNullable(getCompanyLocation()).map(IdNameEmbeddable::with).orElse(null));
    	entity.setCompanyContact(Optional.ofNullable(getCompanyContact()).map(IdNameEmbeddable::with).orElse(null));
    	return entity;
    }
}
