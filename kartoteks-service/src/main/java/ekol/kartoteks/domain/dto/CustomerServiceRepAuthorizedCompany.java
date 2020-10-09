package ekol.kartoteks.domain.dto;

import ekol.hibernate5.domain.embeddable.DateWindow;
import ekol.kartoteks.domain.Company;
import ekol.kartoteks.domain.CompanyEmployeeRelation;

/**
 * Created by kilimci on 23/09/16.
 */
public class CustomerServiceRepAuthorizedCompany {

    private Long relationId;
    private Company company;
    private DateWindow validDates;

    public static CustomerServiceRepAuthorizedCompany withCompanyEmployeeRelation(CompanyEmployeeRelation relation){
        CustomerServiceRepAuthorizedCompany authorizedCompany = new CustomerServiceRepAuthorizedCompany();
        authorizedCompany.setCompany(relation.getCompanyRole().getCompany());
        authorizedCompany.setRelationId(relation.getId());
        DateWindow validDates = relation.getValidDates();
        if(validDates == null){
            validDates = relation.getCompanyRole().getDateRange();
        }
        authorizedCompany.setValidDates(validDates);
        return authorizedCompany;
    }

    public Long getRelationId() {
        return relationId;
    }

    public void setRelationId(Long relationId) {
        this.relationId = relationId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public DateWindow getValidDates() {
        return validDates;
    }

    public void setValidDates(DateWindow validDates) {
        this.validDates = validDates;
    }
}
