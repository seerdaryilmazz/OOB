package ekol.crm.account.controller.lookup;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.*;

import ekol.crm.account.domain.enumaration.TotalLogisticsPotential;
import ekol.model.StringIdCodeName;

@RestController
@RequestMapping("/lookup/total-logistics-potential")
public class TotalLogisticsPotentialController {

    @GetMapping
    public Set<StringIdCodeName> list() {
        return Arrays.stream(TotalLogisticsPotential.values())
        		.flatMap(t->t.getOptions().stream())
        		.map(t->StringIdCodeName.with(t, t, t))
        		.collect(Collectors.toCollection(LinkedHashSet::new));
    }

}
