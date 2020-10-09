package ekol.kartoteks.controller.lookup;

import ekol.hibernate5.domain.controller.BaseLookupApiController;
import ekol.kartoteks.domain.UsageType;
import ekol.kartoteks.repository.UsageTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Created by kilimci on 07/05/16.
 */
@RestController
@RequestMapping("/usage-type")
public class UsageTypeController extends BaseLookupApiController<UsageType> {

    @Autowired
    private UsageTypeRepository repository;

    @PostConstruct
    public void init(){
        setLookupRepository(repository);
    }
}
