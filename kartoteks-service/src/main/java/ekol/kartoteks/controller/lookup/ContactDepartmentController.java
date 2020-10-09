package ekol.kartoteks.controller.lookup;

import ekol.hibernate5.domain.controller.BaseLookupApiController;
import ekol.kartoteks.domain.ContactDepartment;
import ekol.kartoteks.repository.ContactDepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Created by kilimci on 06/05/16.
 */
@RestController
@RequestMapping("/contact-department")
public class ContactDepartmentController extends BaseLookupApiController<ContactDepartment> {

    @Autowired
    private ContactDepartmentRepository repository;

    @PostConstruct
    public void init(){
        setLookupRepository(repository);
    }

}
