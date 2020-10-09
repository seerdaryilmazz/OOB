package ekol.orders.transportOrder.controller;

import ekol.hibernate5.domain.controller.BaseLookupApiController;
import ekol.orders.transportOrder.domain.EquipmentType;
import ekol.orders.transportOrder.repository.EquipmentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/lookup/equipment-type")
public class EquipmentTypeApiController extends BaseLookupApiController<EquipmentType> {

    @Autowired
    private EquipmentTypeRepository equipmentTypeRepository;

    @PostConstruct
    public void init() {
        setLookupRepository(equipmentTypeRepository);
    }
}
