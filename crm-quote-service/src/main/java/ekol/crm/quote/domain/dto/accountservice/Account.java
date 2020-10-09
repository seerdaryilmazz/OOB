package ekol.crm.quote.domain.dto.accountservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.IdNamePair;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {

    private Long id;

    private String name;

    private IdNamePair company;

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

    public IdNamePair getCompany() {
        return company;
    }

    public void setCompany(IdNamePair company) {
        this.company = company;
    }
}
