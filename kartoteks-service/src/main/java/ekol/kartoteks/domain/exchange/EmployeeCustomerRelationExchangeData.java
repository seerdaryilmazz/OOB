package ekol.kartoteks.domain.exchange;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.kartoteks.domain.CompanyEmployeeRelation;

/**
 * Created by kilimci on 05/05/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeCustomerRelationExchangeData {

    private String employeeAccount;
    private String employeeRole;

    public static EmployeeCustomerRelationExchangeData fromCompanyRelation(CompanyEmployeeRelation employeeRelation){
        EmployeeCustomerRelationExchangeData exchangeData = new EmployeeCustomerRelationExchangeData();
        exchangeData.setEmployeeAccount(employeeRelation.getEmployeeAccount());
        exchangeData.setEmployeeRole(employeeRelation.getRelation() != null ? employeeRelation.getRelation().getCode() : null);
        return exchangeData;
    }

    public String getEmployeeAccount() {
        return employeeAccount;
    }

    public void setEmployeeAccount(String employeeAccount) {
        this.employeeAccount = employeeAccount;
    }

    public String getEmployeeRole() {
        return employeeRole;
    }

    public void setEmployeeRole(String employeeRole) {
        this.employeeRole = employeeRole;
    }
}
