package ekol.kartoteks.controller;

import ekol.exceptions.ResourceNotFoundException;
import ekol.kartoteks.domain.Company;
import ekol.kartoteks.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kilimci on 14/03/16.
 */

@RestController
@RequestMapping("/relation")
public class CompanyRelationsController {

    @Autowired
    private CompanyRepository companyRepository;

    @RequestMapping(value="/{companyId}/agentsOrLogisticsPartners",method = RequestMethod.GET)
    public List<Company> findAgentOrLogisticPartner(@PathVariable Long companyId) {
        Company c = companyRepository.findById(companyId);
        if(c == null) {
            throw new ResourceNotFoundException();
        }
        return
                c.getPassiveRelations().stream().filter(companyRelation ->
                    companyRelation.getRelationType().isAgentRelation() ||
                    companyRelation.getRelationType().isLogisticsPartnerRelation()
                ).map(companyRelation -> companyRelation.getActiveCompany()).collect(Collectors.toList());
    }
}
