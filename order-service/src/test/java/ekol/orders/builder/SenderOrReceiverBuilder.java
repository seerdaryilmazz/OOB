package ekol.orders.builder;

import ekol.orders.transportOrder.common.domain.IdNamePair;
import ekol.orders.transportOrder.domain.Company;
import ekol.orders.transportOrder.domain.Contact;
import ekol.orders.transportOrder.domain.Location;
import ekol.orders.transportOrder.domain.SenderOrReceiver;


public final class SenderOrReceiverBuilder {

    private Long companyId;
    private Company company;
    private Long companyContactId;
    private Contact companyContact;
    private Long locationOwnerCompanyId;
    private Company locationOwnerCompany;
    private Long locationId;
    private Location location;
    private Long locationContactId;
    private Contact locationContact;
    private Long locationRegionId;
    private String locationRegionCategoryId;
    private Long locationOperationRegionId;
    private Boolean locationIsAWarehouse;
    private IdNamePair warehouse;

    private SenderOrReceiverBuilder() {
    }

    public static SenderOrReceiverBuilder aSenderOrReceiver() {
        return new SenderOrReceiverBuilder();
    }

    public SenderOrReceiverBuilder withCompanyId(Long companyId) {
        this.companyId = companyId;
        return this;
    }

    public SenderOrReceiverBuilder withCompany(Company company) {
        this.company = company;
        return this;
    }

    public SenderOrReceiverBuilder withCompanyContactId(Long companyContactId) {
        this.companyContactId = companyContactId;
        return this;
    }

    public SenderOrReceiverBuilder withCompanyContact(Contact companyContact) {
        this.companyContact = companyContact;
        return this;
    }

    public SenderOrReceiverBuilder withLocationOwnerCompanyId(Long locationOwnerCompanyId) {
        this.locationOwnerCompanyId = locationOwnerCompanyId;
        return this;
    }

    public SenderOrReceiverBuilder withLocationOwnerCompany(Company locationOwnerCompany) {
        this.locationOwnerCompany = locationOwnerCompany;
        return this;
    }

    public SenderOrReceiverBuilder withLocationId(Long locationId) {
        this.locationId = locationId;
        return this;
    }

    public SenderOrReceiverBuilder withLocation(Location location) {
        this.location = location;
        return this;
    }

    public SenderOrReceiverBuilder withLocationContactId(Long locationContactId) {
        this.locationContactId = locationContactId;
        return this;
    }

    public SenderOrReceiverBuilder withLocationContact(Contact locationContact) {
        this.locationContact = locationContact;
        return this;
    }

    public SenderOrReceiverBuilder withLocationRegionCategoryId(String locationRegionCategoryId) {
        this.locationRegionCategoryId = locationRegionCategoryId;
        return this;
    }

    public SenderOrReceiverBuilder withLocationRegionId(Long locationRegionId) {
        this.locationRegionId = locationRegionId;
        return this;
    }

    public SenderOrReceiverBuilder withLocationOperationRegionId(Long locationOperationRegionId) {
        this.locationOperationRegionId = locationOperationRegionId;
        return this;
    }

    public SenderOrReceiverBuilder withLocationIsAWarehouse(Boolean locationIsAWarehouse) {
        this.locationIsAWarehouse = locationIsAWarehouse;
        return this;
    }

    public SenderOrReceiverBuilder withWarehouse(IdNamePair warehouse) {
        this.warehouse = warehouse;
        return this;
    }

    public SenderOrReceiverBuilder withCompanyEqualsLoadingLocationOwner(boolean b){
        return this;
    }

    public SenderOrReceiver build() {
        SenderOrReceiver senderOrReceiver = new SenderOrReceiver();
        senderOrReceiver.setCompanyId(companyId);
        senderOrReceiver.setCompany(company);
        senderOrReceiver.setCompanyContactId(companyContactId);
        senderOrReceiver.setCompanyContact(companyContact);
        senderOrReceiver.setLocationOwnerCompanyId(locationOwnerCompanyId);
        senderOrReceiver.setLocationOwnerCompany(locationOwnerCompany);
        senderOrReceiver.setLocationId(locationId);
        senderOrReceiver.setLocation(location);
        senderOrReceiver.setLocationContactId(locationContactId);
        senderOrReceiver.setLocationContact(locationContact);
        senderOrReceiver.setLocationRegionId(locationRegionId);
        senderOrReceiver.setLocationRegionCategoryId(locationRegionCategoryId);
        senderOrReceiver.setLocationOperationRegionId(locationOperationRegionId);
        senderOrReceiver.setLocationIsAWarehouse(locationIsAWarehouse);
        senderOrReceiver.setWarehouse(warehouse);
        return senderOrReceiver;
    }
}
