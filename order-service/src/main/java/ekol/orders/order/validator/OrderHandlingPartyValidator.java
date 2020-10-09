package ekol.orders.order.validator;

import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ekol.exceptions.ValidationException;
import ekol.orders.order.domain.CompanyType;
import ekol.orders.order.domain.ShipmentHandlingParty;
import ekol.orders.order.service.KartoteksServiceClient;
import ekol.orders.order.service.LocationServiceClient;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class OrderHandlingPartyValidator {

    private KartoteksServiceClient kartoteksServiceClient;
    private LocationServiceClient locationServiceClient;

    public void validateSender(ShipmentHandlingParty handlingParty){
        validate(handlingParty, "sender");
    }
    public void validateConsignee(ShipmentHandlingParty handlingParty){
        validate(handlingParty, "consignee");
    }
    private void validate(ShipmentHandlingParty handlingParty, String type){
        if(handlingParty == null){
            throw new ValidationException("Order shipment should have a {0}", type);
        }
        if(handlingParty.getCompany() == null || handlingParty.getCompany().getId() == null || StringUtils.isBlank(handlingParty.getCompany().getName())){
            throw new ValidationException("Order shipment should have a {0} company", type);
        }
        if(handlingParty.getHandlingCompany() == null || handlingParty.getHandlingCompany().getId() == null || StringUtils.isBlank(handlingParty.getHandlingCompany().getName())){
            throw new ValidationException("Order shipment should have a {0} handling company", type);
        }
        if(handlingParty.getHandlingLocation() == null || handlingParty.getHandlingLocation().getId() == null || StringUtils.isBlank(handlingParty.getHandlingLocation().getName())){
            throw new ValidationException("Order shipment should have a {0} handling location", type);
        }
        if(StringUtils.isBlank(handlingParty.getHandlingLocationTimezone())){
            throw new ValidationException("Order shipment should have a {0} handling location timezone", type);
        }
        if(!kartoteksServiceClient.isCompanyExists(handlingParty.getCompany().getId())){
            throw new ValidationException("Company ''{0}'' does not exist", handlingParty.getCompany().getName());
        }
        if(CompanyType.COMPANY == handlingParty.getHandlingCompanyType()) {
	        if(!kartoteksServiceClient.isCompanyExists(handlingParty.getHandlingCompany().getId())){
	            throw new ValidationException("Company ''{0}'' does not exist", handlingParty.getHandlingCompany().getName());
	        }
	        if(!kartoteksServiceClient.isLocationExists(handlingParty.getHandlingLocation().getId())){
	            throw new ValidationException("Location ''{0}'' does not exist", handlingParty.getHandlingLocation().getName());
	        }
	        if(Objects.nonNull(handlingParty.getHandlingContact()) && Objects.nonNull(handlingParty.getHandlingContact().getId()) && !kartoteksServiceClient.isContactExists(handlingParty.getHandlingContact().getId())){
	        	throw new ValidationException("Contact ''{0}'' does not exist", handlingParty.getHandlingContact().getName());
	        }
        } else if(CompanyType.CUSTOMS == handlingParty.getHandlingCompanyType()) {
        	if(!locationServiceClient.isCustomsOfficeExists(handlingParty.getHandlingCompany().getId())){
	            throw new ValidationException("Customs ''{0}'' does not exist", handlingParty.getHandlingCompany().getName());
	        }
	        if(!locationServiceClient.isCustomsOfficeLocationExists(handlingParty.getHandlingLocation().getId())){
	            throw new ValidationException("Location ''{0}'' does not exist", handlingParty.getHandlingLocation().getName());
	        }
        } else {
        	throw new ValidationException("Handling company type must not be emtpy");
        }
        if(Objects.nonNull(handlingParty.getCompanyContact()) && Objects.nonNull(handlingParty.getCompanyContact().getId()) && !kartoteksServiceClient.isContactExists(handlingParty.getCompanyContact().getId())){
            throw new ValidationException("Contact ''{0}'' does not exist", handlingParty.getCompanyContact().getName());
        }

    }
}
