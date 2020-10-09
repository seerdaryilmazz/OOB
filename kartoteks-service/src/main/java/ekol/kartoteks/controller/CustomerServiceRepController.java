package ekol.kartoteks.controller;

import ekol.exceptions.ResourceNotFoundException;
import ekol.hibernate5.domain.embeddable.DateWindow;
import ekol.kartoteks.domain.CompanyEmployeeRelation;
import ekol.kartoteks.domain.CompanyRole;
import ekol.kartoteks.domain.CompanyRoleType;
import ekol.kartoteks.domain.EmployeeCustomerRelation;
import ekol.kartoteks.domain.dto.CustomerServiceRepAuthorizedCompany;
import ekol.kartoteks.repository.CompanyEmployeeRelationRepository;
import ekol.kartoteks.repository.CompanyRoleRepository;
import ekol.kartoteks.repository.CompanyRoleTypeRepository;
import ekol.kartoteks.repository.EmployeeCustomerRelationRepository;
import ekol.kartoteks.service.UserService;
import ekol.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by kilimci on 19/09/16.
 */
@RestController
@RequestMapping("/customer-service-rep")
public class CustomerServiceRepController {

    @Autowired
    private CompanyEmployeeRelationRepository companyEmployeeRelationRepository;

    @Autowired
    private EmployeeCustomerRelationRepository employeeCustomerRelationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyRoleRepository companyRoleRepository;

    @Autowired
    private CompanyRoleTypeRepository companyRoleTypeRepository;

    @RequestMapping(value = "/my-companies", method = RequestMethod.GET)
    public Set<CustomerServiceRepAuthorizedCompany> findCompaniesOfSessionCustomerServiceRep() {
        EmployeeCustomerRelation employeeCustomerRelation = getCustomerServiceRepRelation();
        User sessionUser = userService.getSessionUser();
        if (sessionUser == null) {
            throw new ResourceNotFoundException("Session user does not exist");
        }
        return findEmployeeRelatedCompanies(sessionUser.getUsername(), employeeCustomerRelation);
    }

    @RequestMapping(value = "/{employeeAccount}/companies", method = RequestMethod.GET)
    public Set<CustomerServiceRepAuthorizedCompany> findCompaniesOfCustomerServiceRep(@PathVariable String employeeAccount) {
        EmployeeCustomerRelation employeeCustomerRelation = getCustomerServiceRepRelation();
        User serviceRepUser = userService.getUserByAccountName(employeeAccount);
        if (serviceRepUser == null) {
            throw new ResourceNotFoundException("User account {0} does not exist", employeeAccount);
        }
        return findEmployeeRelatedCompanies(serviceRepUser.getUsername(), employeeCustomerRelation);
    }

    private EmployeeCustomerRelation getCustomerServiceRepRelation() {
        EmployeeCustomerRelation employeeCustomerRelation = employeeCustomerRelationRepository.findByCode("CUSTOMER_SERVICE_REP");
        if (employeeCustomerRelation == null) {
            throw new ResourceNotFoundException("Customer service representative relation does not exist");
        }
        return employeeCustomerRelation;
    }

    private Set<CustomerServiceRepAuthorizedCompany> findEmployeeRelatedCompanies(String employeeAccount, EmployeeCustomerRelation employeeCustomerRelation) {
        List<CompanyEmployeeRelation> relations =
                companyEmployeeRelationRepository.findByEmployeeAccountAndRelation(employeeAccount, employeeCustomerRelation);
        return relations.stream().filter(each -> {
            DateWindow validDates = each.getValidDates();
            if (validDates == null) {
                validDates = each.getCompanyRole().getDateRange();
            }
            return validDates.contains(LocalDate.now(), true, true);
        }).map(CustomerServiceRepAuthorizedCompany::withCompanyEmployeeRelation).collect(Collectors.toSet());
    }

    @RequestMapping(value = "/relations/{relationId}", method = RequestMethod.DELETE)
    public void deleteCompanyRelation(@PathVariable Long relationId) {
        CompanyEmployeeRelation relation = companyEmployeeRelationRepository.findOne(relationId);
        if (relation == null) {
            throw new ResourceNotFoundException("Can not find company employee relation");
        }
        relation.setDeleted(true);
        companyEmployeeRelationRepository.save(relation);
    }

    @RequestMapping(value = "/{employeeAccount}/companies", method = RequestMethod.PUT)
    public void saveCompanyRelation(@PathVariable String employeeAccount, @RequestBody CustomerServiceRepAuthorizedCompany customerServiceRepAuthorizedCompany) {
        CompanyRole companyRole = null;
        if (customerServiceRepAuthorizedCompany.getCompany().getId() != null) {
            companyRole = findCustomerCompanyRole(customerServiceRepAuthorizedCompany.getCompany().getId());
        }

        CompanyEmployeeRelation relation = customerServiceRepAuthorizedCompany.getRelationId() == null ? new CompanyEmployeeRelation() : companyEmployeeRelationRepository.findOne(customerServiceRepAuthorizedCompany.getRelationId());
        relation.setEmployeeAccount(employeeAccount);
        if (relation.getId() == null) {
            relation.setRelation(getCustomerServiceRepRelation());
        }
        relation.setValidDates(customerServiceRepAuthorizedCompany.getValidDates());
        if (companyRole != null) {
            relation.setCompanyRole(companyRole);
        }
        companyEmployeeRelationRepository.save(relation);
    }

    private CompanyRoleType findCustomerRoleType(){
        CompanyRoleType companyRoleType = companyRoleTypeRepository.findByCode("CUSTOMER");
        if (companyRoleType == null) {
            throw new ResourceNotFoundException("Can not find CUSTOMER company role type");
        }
        return companyRoleType;
    }
    private List<CompanyRole> findCompanyRoles(Long companyId){
        List<CompanyRole> companyRoles = companyRoleRepository.findByCompanyId(companyId);
        if (companyRoles == null || companyRoles.isEmpty()) {
            throw new ResourceNotFoundException("Can not find company role");
        }
        return companyRoles;
    }
    private CompanyRole findCustomerCompanyRole(Long companyId){
        CompanyRoleType companyRoleType = findCustomerRoleType();
        List<CompanyRole> companyRoles = findCompanyRoles(companyId);

        List<CompanyRole> filteredCompanyRoles = companyRoles.stream().filter(companyRoleFiltered ->
                companyRoleFiltered.getRoleType().getId() == companyRoleType.getId()
        ).collect(Collectors.toList());

        if (filteredCompanyRoles == null || filteredCompanyRoles.isEmpty()) {
            throw new ResourceNotFoundException("Can not find company role.");
        }

        return companyRoles.get(0);
    }
}
