package ekol.kartoteks.controller.lookup;

import ekol.hibernate5.domain.controller.BaseLookupApiController;
import ekol.kartoteks.domain.ContactTitle;
import ekol.kartoteks.repository.ContactTitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Created by kilimci on 06/05/16.
 */
@RestController
@RequestMapping("/contact-title")
public class ContactTitleController extends BaseLookupApiController<ContactTitle> {

    @Autowired
    private ContactTitleRepository repository;

    @PostConstruct
    public void init(){
        setLookupRepository(repository);
    }

}
