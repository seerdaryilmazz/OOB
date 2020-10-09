package ekol.orders.order.builder;

import ekol.orders.order.domain.CodeNameEmbeddable;
import ekol.orders.order.domain.CompanyType;
import ekol.orders.order.domain.IdNameEmbeddable;
import ekol.orders.order.domain.ShipmentHandlingParty;

public final class ShipmentHandlingPartyBuilder {
    private IdNameEmbeddable company;
    private IdNameEmbeddable companyContact;
    private IdNameEmbeddable handlingCompany;
    private IdNameEmbeddable handlingLocation;
    private IdNameEmbeddable handlingContact;
    private IdNameEmbeddable handlingRegion;
    private CodeNameEmbeddable handlingRegionCategory;
    private IdNameEmbeddable handlingOperationRegion;
    private boolean handlingAtCrossDock;
    private String handlingLocationCountryCode;
    private String handlingLocationPostalCode;
    private String handlingLocationTimezone;
    private CompanyType handlingCompanyType;

    private ShipmentHandlingPartyBuilder() {
    }

    public static ShipmentHandlingPartyBuilder aShipmentHandlingParty() {
        return new ShipmentHandlingPartyBuilder();
    }

    public ShipmentHandlingPartyBuilder withCompany(IdNameEmbeddable company) {
        this.company = company;
        return this;
    }

    public ShipmentHandlingPartyBuilder withCompanyContact(IdNameEmbeddable companyContact) {
        this.companyContact = companyContact;
        return this;
    }

    public ShipmentHandlingPartyBuilder withHandlingCompany(IdNameEmbeddable handlingCompany) {
        this.handlingCompany = handlingCompany;
        return this;
    }

    public ShipmentHandlingPartyBuilder withHandlingLocation(IdNameEmbeddable handlingLocation) {
        this.handlingLocation = handlingLocation;
        return this;
    }

    public ShipmentHandlingPartyBuilder withHandlingContact(IdNameEmbeddable handlingContact) {
        this.handlingContact = handlingContact;
        return this;
    }

    public ShipmentHandlingPartyBuilder withHandlingRegion(IdNameEmbeddable handlingRegion) {
        this.handlingRegion = handlingRegion;
        return this;
    }

    public ShipmentHandlingPartyBuilder withHandlingRegionCategory(CodeNameEmbeddable handlingRegionCategory) {
        this.handlingRegionCategory = handlingRegionCategory;
        return this;
    }

    public ShipmentHandlingPartyBuilder withHandlingOperationRegion(IdNameEmbeddable handlingOperationRegion) {
        this.handlingOperationRegion = handlingOperationRegion;
        return this;
    }

    public ShipmentHandlingPartyBuilder withHandlingAtCrossDock(boolean handlingAtCrossDock) {
        this.handlingAtCrossDock = handlingAtCrossDock;
        return this;
    }

    public ShipmentHandlingPartyBuilder withHandlingLocationCountryCode(String handlingLocationCountryCode) {
        this.handlingLocationCountryCode = handlingLocationCountryCode;
        return this;
    }

    public ShipmentHandlingPartyBuilder withHandlingLocationPostalCode(String handlingLocationPostalCode) {
        this.handlingLocationPostalCode = handlingLocationPostalCode;
        return this;
    }

    public ShipmentHandlingPartyBuilder withHandlingLocationTimezone(String handlingLocationTimezone) {
        this.handlingLocationTimezone = handlingLocationTimezone;
        return this;
    }
    
    public ShipmentHandlingPartyBuilder withHandlingCompanyType(CompanyType handlingCompanyType) {
    	this.handlingCompanyType = handlingCompanyType;
    	return this;
    }

    public ShipmentHandlingPartyBuilder but() {
        return aShipmentHandlingParty().withCompany(company).withCompanyContact(companyContact).withHandlingCompany(handlingCompany)
                .withHandlingLocation(handlingLocation).withHandlingContact(handlingContact).withHandlingRegion(handlingRegion)
                .withHandlingRegionCategory(handlingRegionCategory).withHandlingOperationRegion(handlingOperationRegion)
                .withHandlingAtCrossDock(handlingAtCrossDock).withHandlingLocationCountryCode(handlingLocationCountryCode)
                .withHandlingLocationPostalCode(handlingLocationPostalCode).withHandlingLocationTimezone(handlingLocationTimezone);
    }

    public ShipmentHandlingParty build() {
        ShipmentHandlingParty shipmentHandlingParty = new ShipmentHandlingParty();
        shipmentHandlingParty.setCompany(company);
        shipmentHandlingParty.setCompanyContact(companyContact);
        shipmentHandlingParty.setHandlingCompany(handlingCompany);
        shipmentHandlingParty.setHandlingLocation(handlingLocation);
        shipmentHandlingParty.setHandlingContact(handlingContact);
        shipmentHandlingParty.setHandlingRegion(handlingRegion);
        shipmentHandlingParty.setHandlingRegionCategory(handlingRegionCategory);
        shipmentHandlingParty.setHandlingOperationRegion(handlingOperationRegion);
        shipmentHandlingParty.setHandlingAtCrossDock(handlingAtCrossDock);
        shipmentHandlingParty.setHandlingLocationCountryCode(handlingLocationCountryCode);
        shipmentHandlingParty.setHandlingLocationPostalCode(handlingLocationPostalCode);
        shipmentHandlingParty.setHandlingLocationTimezone(handlingLocationTimezone);
        shipmentHandlingParty.setHandlingCompanyType(handlingCompanyType);
        return shipmentHandlingParty;
    }
}
