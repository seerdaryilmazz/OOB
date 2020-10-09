package ekol.crm.quote.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.quote.domain.dto.kartoteksservice.Company;
import ekol.crm.quote.domain.dto.kartoteksservice.CompanyLocation;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountCompanyAndLocation_InvoiceCompanyAndLocation {

    private Company accountCompany;

    private CompanyLocation accountCompanyLocation;

    private Company invoiceCompany;

    private CompanyLocation invoiceCompanyLocation;

    public AccountCompanyAndLocation_InvoiceCompanyAndLocation() {
    }

    public AccountCompanyAndLocation_InvoiceCompanyAndLocation(
            Company accountCompany, CompanyLocation accountCompanyLocation, Company invoiceCompany, CompanyLocation invoiceCompanyLocation) {
        this.accountCompany = accountCompany;
        this.accountCompanyLocation = accountCompanyLocation;
        this.invoiceCompany = invoiceCompany;
        this.invoiceCompanyLocation = invoiceCompanyLocation;
    }

    public Company getAccountCompany() {
        return accountCompany;
    }

    public void setAccountCompany(Company accountCompany) {
        this.accountCompany = accountCompany;
    }

    public CompanyLocation getAccountCompanyLocation() {
        return accountCompanyLocation;
    }

    public void setAccountCompanyLocation(CompanyLocation accountCompanyLocation) {
        this.accountCompanyLocation = accountCompanyLocation;
    }

    public Company getInvoiceCompany() {
        return invoiceCompany;
    }

    public void setInvoiceCompany(Company invoiceCompany) {
        this.invoiceCompany = invoiceCompany;
    }

    public CompanyLocation getInvoiceCompanyLocation() {
        return invoiceCompanyLocation;
    }

    public void setInvoiceCompanyLocation(CompanyLocation invoiceCompanyLocation) {
        this.invoiceCompanyLocation = invoiceCompanyLocation;
    }
}
