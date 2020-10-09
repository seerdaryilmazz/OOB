package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.orders.transportOrder.common.domain.IdNamePair;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private Country country;

    private Contact contact;

    private CompanySegmentType segmentType;

    private String portfolioOwner;

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

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public CompanySegmentType getSegmentType() {
        return segmentType;
    }

    public void setSegmentType(CompanySegmentType segmentType) {
        this.segmentType = segmentType;
    }

    public String getPortfolioOwner() {
        return portfolioOwner;
    }

    public void setPortfolioOwner(String portfolioOwner) {
        this.portfolioOwner = portfolioOwner;
    }

    public IdNamePair asIdNamePair(){
        return IdNamePair.createWith(getId(), getName());
    }
}
