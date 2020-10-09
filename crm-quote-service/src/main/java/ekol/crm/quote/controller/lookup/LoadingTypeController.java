package ekol.crm.quote.controller.lookup;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ekol.crm.quote.domain.enumaration.LoadingType;

@RestController
@RequestMapping("/lookup/loading-type")
public class LoadingTypeController {

    @GetMapping
    public List<LoadingType> list(@RequestParam String serviceArea) {
        return Stream.of(LoadingType.values())
        			.filter(t->Stream.of(t.getServiceArea()).collect(Collectors.toSet()).contains(serviceArea))
        			.collect(Collectors.toList());
    }
}
