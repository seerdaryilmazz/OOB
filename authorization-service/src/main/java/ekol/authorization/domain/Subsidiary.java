package ekol.authorization.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.authorization.dto.IdNamePair;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Subsidiary")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Subsidiary extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_subsidiary", sequenceName = "seq_subsidiary")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_subsidiary")
    private Long id;

    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "SubsidiaryCompany", joinColumns = @JoinColumn(name = "subsidiaryId"))
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "companyId")),
            @AttributeOverride(name = "name", column = @Column(name = "companyName"))
    })
    private Set<IdNamePair> companies = new HashSet<>();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "defaultInvoiceCompanyId")),
            @AttributeOverride(name = "name", column = @Column(name = "defaultInvoiceCompanyName"))
    })
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
