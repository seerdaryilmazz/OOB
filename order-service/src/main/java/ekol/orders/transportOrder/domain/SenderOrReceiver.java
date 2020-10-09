package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.orders.transportOrder.common.domain.IdNamePair;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@Access(AccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SenderOrReceiver implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long companyId;

    @Transient
    private Company company;

    private Long companyContactId;

    @Transient
    private Contact companyContact;

    private Long locationOwnerCompanyId;

    @Transient
    private Company locationOwnerCompany;

    private Long locationId;

    @Transient
    private Location location;

    private Long locationContactId;

    @Transient
    private Contact locationContact;

    private Long locationRegionId;

    private String locationRegionCategoryId;

    private Long locationOperationRegionId;

    /**
     * Yukarıdaki locationId bir depoya işaret ediyorsa bu alan true, aksi halde false olur.
     */
    private Boolean locationIsAWarehouse;

    /**
     * Yukarıdaki locationId bir depoya işaret ediyorsa bu alan hangi depo olduğunu gösterir.
     */
    @Embedded
    private IdNamePair warehouse;

    public boolean isCompanyEqualsLoadingLocationOwner() {
        return getCompanyId().equals(getLocationOwnerCompanyId());
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Long getCompanyContactId() {
        return companyContactId;
    }

    public void setCompanyContactId(Long companyContactId) {
        this.companyContactId = companyContactId;
    }

    public Contact getCompanyContact() {
        return companyContact;
    }

    public void setCompanyContact(Contact companyContact) {
        this.companyContact = companyContact;
    }

    public Long getLocationOwnerCompanyId() {
        return locationOwnerCompanyId;
    }

    public void setLocationOwnerCompanyId(Long locationOwnerCompanyId) {
        this.locationOwnerCompanyId = locationOwnerCompanyId;
    }

    public Company getLocationOwnerCompany() {
        return locationOwnerCompany;
    }

    public void setLocationOwnerCompany(Company locationOwnerCompany) {
        this.locationOwnerCompany = locationOwnerCompany;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Long getLocationContactId() {
        return locationContactId;
    }

    public void setLocationContactId(Long locationContactId) {
        this.locationContactId = locationContactId;
    }

    public Contact getLocationContact() {
        return locationContact;
    }

    public void setLocationContact(Contact locationContact) {
        this.locationContact = locationContact;
    }

    public Long getLocationRegionId() {
        return locationRegionId;
    }

    public void setLocationRegionId(Long locationRegionId) {
        this.locationRegionId = locationRegionId;
    }

    public String getLocationRegionCategoryId() {
        return locationRegionCategoryId;
    }

    public void setLocationRegionCategoryId(String locationRegionCategoryId) {
        this.locationRegionCategoryId = locationRegionCategoryId;
    }

    public Long getLocationOperationRegionId() {
        return locationOperationRegionId;
    }

    public void setLocationOperationRegionId(Long locationOperationRegionId) {
        this.locationOperationRegionId = locationOperationRegionId;
    }

    public Boolean getLocationIsAWarehouse() {
        return locationIsAWarehouse;
    }

    public void setLocationIsAWarehouse(Boolean locationIsAWarehouse) {
        this.locationIsAWarehouse = locationIsAWarehouse;
    }

    public IdNamePair getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(IdNamePair warehouse) {
        this.warehouse = warehouse;
    }
}
