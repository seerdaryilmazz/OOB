package ekol.crm.quote.controller.lookup;

import ekol.crm.quote.domain.enumaration.LoadType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/lookup/load-type")
public class LoadTypeController {

    @RequestMapping(value="/{serviceArea}", method= RequestMethod.GET)
    public List<LoadType> list(@PathVariable String serviceArea) {

        return Arrays.stream(LoadType.values())
                .filter(loadType -> loadType.getServiceArea().equals(serviceArea)).collect(Collectors.toList());
    }

}
