package ekol.orders.order.builder;

import ekol.model.IdNamePair;
import ekol.orders.lookup.domain.ServiceType;
import ekol.orders.lookup.domain.TruckLoadType;
import ekol.orders.order.domain.Status;
import ekol.orders.order.domain.dto.json.OrderJson;
import ekol.orders.order.domain.dto.json.OrderShipmentJson;

import java.util.ArrayList;
import java.util.List;

public final class OrderJsonBuilder {
    private Long id;
    private IdNamePair customer;
    private IdNamePair originalCustomer;
    private IdNamePair subsidiary;
    private TruckLoadType truckLoadType;
    private ServiceType serviceType;
    private Status status;
    private String countryCode;
    private String templateId;
    private String templateName;
    private List<OrderShipmentJson> shipments = new ArrayList<>();

    private OrderJsonBuilder() {
    }

    public static OrderJsonBuilder anOrderJson() {
        return new OrderJsonBuilder();
    }

    public OrderJsonBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public OrderJsonBuilder withCustomer(IdNamePair customer) {
        this.customer = customer;
        return this;
    }

    public OrderJsonBuilder withOriginalCustomer(IdNamePair originalCustomer) {
        this.originalCustomer = originalCustomer;
        return this;
    }

    public OrderJsonBuilder withSubsidiary(IdNamePair subsidiary) {
        this.subsidiary = subsidiary;
        return this;
    }

    public OrderJsonBuilder withTruckLoadType(TruckLoadType truckLoadType) {
        this.truckLoadType = truckLoadType;
        return this;
    }

    public OrderJsonBuilder withServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
        return this;
    }

    public OrderJsonBuilder withStatus(Status status) {
        this.status = status;
        return this;
    }

    public OrderJsonBuilder withCountryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }
    public OrderJsonBuilder withTemplateId(String templateId) {
        this.templateId = templateId;
        return this;
    }
    public OrderJsonBuilder withTemplateName(String templateName) {
        this.templateName = templateName;
        return this;
    }

    public OrderJsonBuilder withShipments(List<OrderShipmentJson> shipments) {
        this.shipments = shipments;
        return this;
    }

    public OrderJsonBuilder but() {
        return anOrderJson().withId(id).withCustomer(customer).withSubsidiary(subsidiary)
                .withTruckLoadType(truckLoadType).withServiceType(serviceType)
                .withOriginalCustomer(originalCustomer).withCountryCode(countryCode)
                .withTemplateId(templateId).withTemplateName(templateName)
                .withStatus(status).withShipments(shipments);
    }

    public OrderJson build() {
        OrderJson orderJson = new OrderJson();
        orderJson.setId(id);
        orderJson.setCustomer(customer);
        orderJson.setOriginalCustomer(originalCustomer);
        orderJson.setSubsidiary(subsidiary);
        orderJson.setTruckLoadType(truckLoadType);
        orderJson.setServiceType(serviceType);
        orderJson.setStatus(status);
        orderJson.setShipments(shipments);
        orderJson.setCountryCode(countryCode);
        orderJson.setTemplateId(templateId);
        orderJson.setTemplateName(templateName);
        return orderJson;
    }
}
