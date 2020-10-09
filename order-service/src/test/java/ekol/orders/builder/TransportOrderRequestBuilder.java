package ekol.orders.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.model.User;
import ekol.orders.transportOrder.common.domain.IdNamePair;
import ekol.orders.transportOrder.domain.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public final class TransportOrderRequestBuilder {

    private Long id;
    private IdNamePair subsidiary;
    private Long customerId;
    private Company customer;
    private TransportOrder order;
    private Long acCustomerId;
    private Company acCustomer;
    private OrderType orderType;
    private IdNamePair contract;
    private String projectNo;
    private String offerNo;
    private LocalDate readyAtDate;
    private boolean confirmationRequired;
    private Integer numberOfOrders;
    private String customerNote;
    private String taskId;
    private Long createdById;
    private User createdBy;
    private Set<TransportOrderRequestDocument> documents = new HashSet<>();
    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;

    private TransportOrderRequestBuilder() {
    }

    public static TransportOrderRequestBuilder aTransportOrderRequest() {
        return new TransportOrderRequestBuilder();
    }

    public TransportOrderRequestBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public TransportOrderRequestBuilder withSubsidiary(IdNamePair subsidiary) {
        this.subsidiary = subsidiary;
        return this;
    }

    public TransportOrderRequestBuilder withCustomerId(Long customerId) {
        this.customerId = customerId;
        return this;
    }

    public TransportOrderRequestBuilder withCustomer(Company customer) {
        this.customer = customer;
        return this;
    }

    public TransportOrderRequestBuilder withOrder(TransportOrder order) {
        this.order = order;
        return this;
    }

    public TransportOrderRequestBuilder withAcCustomerId(Long acCustomerId) {
        this.acCustomerId = acCustomerId;
        return this;
    }

    public TransportOrderRequestBuilder withAcCustomer(Company acCustomer) {
        this.acCustomer = acCustomer;
        return this;
    }

    public TransportOrderRequestBuilder withOrderType(OrderType orderType) {
        this.orderType = orderType;
        return this;
    }

    public TransportOrderRequestBuilder withContract(IdNamePair contract) {
        this.contract = contract;
        return this;
    }

    public TransportOrderRequestBuilder withProjectNo(String projectNo) {
        this.projectNo = projectNo;
        return this;
    }

    public TransportOrderRequestBuilder withOfferNo(String offerNo) {
        this.offerNo = offerNo;
        return this;
    }

    public TransportOrderRequestBuilder withReadyAtDate(LocalDate readyAtDate) {
        this.readyAtDate = readyAtDate;
        return this;
    }

    public TransportOrderRequestBuilder withConfirmationRequired(boolean confirmationRequired) {
        this.confirmationRequired = confirmationRequired;
        return this;
    }

    public TransportOrderRequestBuilder withNumberOfOrders(Integer numberOfOrders) {
        this.numberOfOrders = numberOfOrders;
        return this;
    }

    public TransportOrderRequestBuilder withCustomerNote(String customerNote) {
        this.customerNote = customerNote;
        return this;
    }

    public TransportOrderRequestBuilder withTaskId(String taskId) {
        this.taskId = taskId;
        return this;
    }

    public TransportOrderRequestBuilder withCreatedById(Long createdById) {
        this.createdById = createdById;
        return this;
    }

    public TransportOrderRequestBuilder withCreatedBy(User createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public TransportOrderRequestBuilder withDocuments(Set<TransportOrderRequestDocument> documents) {
        this.documents = documents;
        return this;
    }

    public TransportOrderRequestBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public TransportOrderRequestBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public TransportOrderRequestBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public TransportOrderRequestBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public TransportOrderRequest build() {
        TransportOrderRequest transportOrderRequest = new TransportOrderRequest();
        transportOrderRequest.setId(id);
        transportOrderRequest.setSubsidiary(subsidiary);
        transportOrderRequest.setCustomerId(customerId);
        transportOrderRequest.setCustomer(customer);
        transportOrderRequest.setOrder(order);
        transportOrderRequest.setAcCustomerId(acCustomerId);
        transportOrderRequest.setAcCustomer(acCustomer);
        transportOrderRequest.setOrderType(orderType);
        transportOrderRequest.setContract(contract);
        transportOrderRequest.setProjectNo(projectNo);
        transportOrderRequest.setOfferNo(offerNo);
        transportOrderRequest.setReadyAtDate(readyAtDate);
        transportOrderRequest.setConfirmationRequired(confirmationRequired);
        transportOrderRequest.setNumberOfOrders(numberOfOrders);
        transportOrderRequest.setCustomerNote(customerNote);
        transportOrderRequest.setTaskId(taskId);
        transportOrderRequest.setDocuments(documents);
        transportOrderRequest.setDeleted(deleted);
        transportOrderRequest.setDeletedAt(deletedAt);
        transportOrderRequest.setLastUpdated(lastUpdated);
        transportOrderRequest.setLastUpdatedBy(lastUpdatedBy);
        return transportOrderRequest;
    }
}
