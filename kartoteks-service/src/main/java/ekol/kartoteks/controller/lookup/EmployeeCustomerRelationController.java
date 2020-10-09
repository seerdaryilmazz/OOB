package ekol.kartoteks.controller.lookup;

import ekol.hibernate5.domain.controller.BaseLookupApiController;
import ekol.kartoteks.domain.EmployeeCustomerRelation;
import ekol.kartoteks.repository.EmployeeCustomerRelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Created by kilimci on 06/05/16.
 */
@RestController
@RequestMapping("/employee-customer-relation")
public class EmployeeCustomerRelationController extends BaseLookupApiController<EmployeeCustomerRelation> {

    @Autowired
    private EmployeeCustomerRelationRepository repository;

    @PostConstruct
    public void init(){
        setLookupRepository(repository);
    }
}
