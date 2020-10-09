package ekol.kartoteks.controller.lookup;

import ekol.hibernate5.domain.controller.BaseLookupApiController;
import ekol.kartoteks.domain.PhoneType;
import ekol.kartoteks.repository.PhoneTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Created by fatmaozyildirim on 5/4/16.
 */
@RestController
@RequestMapping("/phone-type")
public class PhoneTypeController extends BaseLookupApiController<PhoneType> {

    @Autowired
    private PhoneTypeRepository phoneTypeRepository;

    @PostConstruct
    public void init(){
        setLookupRepository(phoneTypeRepository);
    }

}
