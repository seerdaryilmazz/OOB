package ekol.orders.order.validator;

import ekol.exceptions.ValidationException;
import ekol.orders.lookup.domain.AdrClassDetails;
import ekol.orders.lookup.repository.AdrClassDetailsRepository;
import ekol.orders.lookup.repository.AdrPackageTypeRepository;
import ekol.orders.order.domain.OrderShipmentAdr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class OrderShipmentAdrValidator {

    private AdrClassDetailsRepository adrClassDetailsRepository;
    private AdrPackageTypeRepository adrPackageTypeRepository;

    @Autowired
    public OrderShipmentAdrValidator(AdrClassDetailsRepository adrClassDetailsRepository,
                                     AdrPackageTypeRepository adrPackageTypeRepository){
        this.adrClassDetailsRepository = adrClassDetailsRepository;
        this.adrPackageTypeRepository = adrPackageTypeRepository;
    }

    public void validate(OrderShipmentAdr orderShipmentAdr){
        if(orderShipmentAdr.getAdrClassDetails() == null || orderShipmentAdr.getAdrClassDetails().getId() == null){
            throw new ValidationException("order shipment should have ADR class details");
        }
        Optional<AdrClassDetails> adrClassDetails =
                adrClassDetailsRepository.findById(orderShipmentAdr.getAdrClassDetails().getId());
        if(!adrClassDetails.isPresent()){
            throw new ValidationException("ADR class detail with id {0} does not exist",
                    orderShipmentAdr.getAdrClassDetails().getId().toString());
        }
        if(orderShipmentAdr.getQuantity() == null || orderShipmentAdr.getQuantity().equals(0)){
            throw new ValidationException("ADR details should have a quantity");
        }
        if(orderShipmentAdr.getPackageType() == null){
            throw new ValidationException("ADR details should have a package type");
        }
        if(!adrPackageTypeRepository.findById(orderShipmentAdr.getPackageType().getId()).isPresent()){
            throw new ValidationException("ADR package type with id {0} does not exist",
                    orderShipmentAdr.getPackageType().getId().toString());
        }
        if(orderShipmentAdr.getAmount() == null || orderShipmentAdr.getAmount().equals(BigDecimal.ZERO)){
            throw new ValidationException("ADR details should have an amount");
        }
        if(orderShipmentAdr.getUnit() == null){
            throw new ValidationException("ADR details should have an amount unit");
        }


        if(orderShipmentAdr.getInnerPackageType() != null && orderShipmentAdr.getInnerPackageType().getId() != null){
            if(!adrPackageTypeRepository.findById(orderShipmentAdr.getInnerPackageType().getId()).isPresent()){
                throw new ValidationException("ADR inner package type with id {0} does not exist",
                        orderShipmentAdr.getInnerPackageType().getId().toString());
            }
        }
    }
}
