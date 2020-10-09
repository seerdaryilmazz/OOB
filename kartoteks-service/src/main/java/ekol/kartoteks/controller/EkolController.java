package ekol.kartoteks.controller;

import ekol.kartoteks.domain.CompanyLocation;
import ekol.kartoteks.repository.CompanyLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ekol")
public class EkolController {

    @Autowired
    private CompanyLocationRepository companyLocationRepository;

    @RequestMapping(value = {"/locations", "/locations/"}, method = RequestMethod.GET)
    public List<CompanyLocation> getLocations() {
        return companyLocationRepository.findByCompanyOwnedByEkolIsTrue();
    }
}
