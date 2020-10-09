package ekol.orders.order.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.IdNamePair;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SubsidiaryResponse {

    private Long id;
    private String name;
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

    public IdNamePair getDefaultInvoiceCompany() {
        return defaultInvoiceCompany;
    }

    public void setDefaultInvoiceCompany(IdNamePair defaultInvoiceCompany) {
        this.defaultInvoiceCompany = defaultInvoiceCompany;
    }
}
