package ekol.crm.quote.domain.dto.authorizationservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.IdNamePair;

import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Subsidiary {

    private Long id;

    private String name;

    private Set<IdNamePair> companies = new HashSet<>();

    private IdNamePair defaultInvoiceCompany;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<IdNamePair> getCompanies() {
        return companies;
    }

    public void setCompanies(Set<IdNamePair> companies) {
        this.companies = companies;
    }

    public IdNamePair getDefaultInvoiceCompany() {
        return defaultInvoiceCompany;
    }

    public void setDefaultInvoiceCompany(IdNamePair defaultInvoiceCompany) {
        this.defaultInvoiceCompany = defaultInvoiceCompany;
    }
}
