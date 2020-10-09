package ekol.location.controller.lookup;

import ekol.resource.controller.BaseEnumApiController;
import ekol.location.domain.location.enumeration.TerminalRegistrationMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Created by kilimci on 28/04/2017.
 */
@RestController
@RequestMapping("/lookup/terminal-registration-method")
public class TerminalRegistrationMethodController extends BaseEnumApiController<TerminalRegistrationMethod> {

    @PostConstruct
    public void init() {
        setType(TerminalRegistrationMethod.class);
    }
}
