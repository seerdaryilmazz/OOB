package ekol.kartoteks.controller.lookup;

import ekol.hibernate5.domain.controller.BaseLookupApiController;
import ekol.kartoteks.domain.CompanyRoleType;
import ekol.kartoteks.repository.CompanyRoleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Created by kilimci on 24/06/16.
 */
@RestController
@RequestMapping("/company-role-type")
public class CompanyRoleTypeController extends BaseLookupApiController<CompanyRoleType> {

    @Autowired
    private CompanyRoleTypeRepository repository;

    @PostConstruct
    public void init(){
        setLookupRepository(repository);
    }
}
