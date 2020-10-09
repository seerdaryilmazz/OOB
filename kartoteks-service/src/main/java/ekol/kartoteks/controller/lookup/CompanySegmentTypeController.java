package ekol.kartoteks.controller.lookup;

import ekol.hibernate5.domain.controller.BaseLookupApiController;
import ekol.kartoteks.domain.CompanySegmentType;
import ekol.kartoteks.repository.CompanySegmentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Created by kilimci on 09/09/16.
 */
@RestController
@RequestMapping("/company-segment-type")
public class CompanySegmentTypeController extends BaseLookupApiController<CompanySegmentType> {

    @Autowired
    private CompanySegmentTypeRepository repository;

    @PostConstruct
    public void init(){
        setLookupRepository(repository);
    }
}
