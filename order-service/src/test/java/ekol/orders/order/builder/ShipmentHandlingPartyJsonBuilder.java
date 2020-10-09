package ekol.orders.order.builder;

import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import ekol.orders.order.domain.dto.json.ShipmentHandlingPartyJson;

public final class ShipmentHandlingPartyJsonBuilder {
    private IdNamePair company;
    private IdNamePair companyContact;
    private IdNamePair handlingCompany;
    private IdNamePair handlingLocation;
    private IdNamePair handlingContact;
    private IdNamePair handlingRegion;
    private CodeNamePair handlingRegionCategory;
    private IdNamePair handlingOperationRegion;
    private boolean handlingAtCrossDock;
    private String handlingLocationCountryCode;
    private String handlingLocationPostalCode;
    private String handlingLocationTimezone;

    private ShipmentHandlingPartyJsonBuilder() {
    }

    public static ShipmentHandlingPartyJsonBuilder aShipmentHandlingPartyJson() {
        return new ShipmentHandlingPartyJsonBuilder();
    }

    public ShipmentHandlingPartyJsonBuilder withCompany(IdNamePair company) {
        this.company = company;
        return this;
    }

    public ShipmentHandlingPartyJsonBuilder withCompanyContact(IdNamePair companyContact) {
        this.companyContact = companyContact;
        return this;
    }

    public ShipmentHandlingPartyJsonBuilder withHandlingCompany(IdNamePair handlingCompany) {
        this.handlingCompany = handlingCompany;
        return this;
    }

    public ShipmentHandlingPartyJsonBuilder withHandlingLocation(IdNamePair handlingLocation) {
        this.handlingLocation = handlingLocation;
        return this;
    }

    public ShipmentHandlingPartyJsonBuilder withHandlingContact(IdNamePair handlingContact) {
        this.handlingContact = handlingContact;
        return this;
    }

    public ShipmentHandlingPartyJsonBuilder withHandlingRegion(IdNamePair handlingRegion) {
        this.handlingRegion = handlingRegion;
        return this;
    }

    public ShipmentHandlingPartyJsonBuilder withHandlingRegionCategory(CodeNamePair handlingRegionCategory) {
        this.handlingRegionCategory = handlingRegionCategory;
        return this;
    }

    public ShipmentHandlingPartyJsonBuilder withHandlingOperationRegion(IdNamePair handlingOperationRegion) {
        this.handlingOperationRegion = handlingOperationRegion;
        return this;
    }

    public ShipmentHandlingPartyJsonBuilder withHandlingAtCrossDock(boolean handlingAtCrossDock) {
        this.handlingAtCrossDock = handlingAtCrossDock;
        return this;
    }
    public ShipmentHandlingPartyJsonBuilder withHandlingLocationCountryCode(String handlingLocationCountryCode) {
        this.handlingLocationCountryCode = handlingLocationCountryCode;
        return this;
    }
    public ShipmentHandlingPartyJsonBuilder withHandlingLocationPostalCode(String handlingLocationPostalCode) {
        this.handlingLocationPostalCode = handlingLocationPostalCode;
        return this;
    }
    public ShipmentHandlingPartyJsonBuilder withHandlingLocationTimezone(String handlingLocationTimezone) {
        this.handlingLocationTimezone = handlingLocationTimezone;
        return this;
    }

    public ShipmentHandlingPartyJsonBuilder but() {
        return aShipmentHandlingPartyJson().withCompany(company).withCompanyContact(companyContact).withHandlingCompany(handlingCompany)
                .withHandlingLocation(handlingLocation).withHandlingContact(handlingContact).withHandlingRegion(handlingRegion)
                .withHandlingRegionCategory(handlingRegionCategory).withHandlingOperationRegion(handlingOperationRegion)
                .withHandlingAtCrossDock(handlingAtCrossDock).withHandlingLocationCountryCode(handlingLocationCountryCode)
                .withHandlingLocationPostalCode(handlingLocationPostalCode).withHandlingLocationTimezone(handlingLocationTimezone);
    }

    public ShipmentHandlingPartyJson build() {
        ShipmentHandlingPartyJson shipmentHandlingPartyJson = new ShipmentHandlingPartyJson();
        shipmentHandlingPartyJson.setCompany(company);
        shipmentHandlingPartyJson.setCompanyContact(companyContact);
        shipmentHandlingPartyJson.setHandlingCompany(handlingCompany);
        shipmentHandlingPartyJson.setHandlingLocation(handlingLocation);
        shipmentHandlingPartyJson.setHandlingContact(handlingContact);
        shipmentHandlingPartyJson.setHandlingRegion(handlingRegion);
        shipmentHandlingPartyJson.setHandlingRegionCategory(handlingRegionCategory);
        shipmentHandlingPartyJson.setHandlingOperationRegion(handlingOperationRegion);
        shipmentHandlingPartyJson.setHandlingAtCrossDock(handlingAtCrossDock);
        shipmentHandlingPartyJson.setHandlingLocationCountryCode(handlingLocationCountryCode);
        shipmentHandlingPartyJson.setHandlingLocationPostalCode(handlingLocationPostalCode);
        shipmentHandlingPartyJson.setHandlingLocationTimezone(handlingLocationTimezone);
        return shipmentHandlingPartyJson;
    }
}
