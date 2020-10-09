package ekol.kartoteks.controller.lookup;

import ekol.hibernate5.domain.controller.BaseLookupApiController;
import ekol.kartoteks.domain.CompanyType;
import ekol.kartoteks.repository.CompanyTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Created by kilimci on 07/05/16.
 */
@RestController
@RequestMapping("/company-type")
public class CompanyTypeController extends BaseLookupApiController<CompanyType> {
    @Autowired
    private CompanyTypeRepository repository;

    @PostConstruct
    public void init(){
        setLookupRepository(repository);
    }
}
