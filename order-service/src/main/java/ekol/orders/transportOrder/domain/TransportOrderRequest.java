package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.json.serializers.LocalDateDeserializer;
import ekol.json.serializers.LocalDateSerializer;
import ekol.model.User;
import ekol.orders.transportOrder.common.domain.IdNamePair;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "TransportOrderRequest.allDetails",
                attributeNodes = {
                        @NamedAttributeNode("order"),
                        @NamedAttributeNode(value = "documents", subgraph = "TransportOrderRequestDocument.allDetails")
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "TransportOrderRequestDocument.allDetails",
                                attributeNodes = {
                                        @NamedAttributeNode("type")
                                }
                        )
                }
        )
})
@Entity
@Table(name = "transport_order_req")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransportOrderRequest extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_transport_order_req", sequenceName = "seq_transport_order_req")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_transport_order_req")
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "subsidiaryId")),
            @AttributeOverride(name = "name", column = @Column(name = "subsidiaryName"))
    })
    private IdNamePair subsidiary;

    @Column
    private Long customerId;

    @Transient
    private Company customer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId")
    @JsonManagedReference
    private TransportOrder order;

    /**
     * Türkçe: A/N Müşteri (A:Acente, N:Nakliyeci)
     * English: A/C Customer
     */
    @Column
    private Long acCustomerId;

    @Transient
    private Company acCustomer;

    @Enumerated(EnumType.STRING)
    @Column
    private OrderType orderType;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "contractId")),
            @AttributeOverride(name = "name", column = @Column(name = "contractName"))
    })
    private IdNamePair contract;

    @Column
    private String projectNo;

    @Column
    private String offerNo;

    //This is the shipment ready date. The place where the shipment will be loaded is not known yet
    //So we can not give a time zone for now. Time zones in the readydate of the shipments will be set while creating order
    @Column
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate readyAtDate;

    @Column
    private boolean confirmationRequired;

    @Column
    private Integer numberOfOrders;

    @Column
    private String customerNote;

    @Column
    private String taskId;

    /**
     * user-service'teki user.id'ye denk geliyor.
     */
    @Column
    private Long createdById;

    @Transient
    private User createdBy;

    @OneToMany(mappedBy="request")
    @Where(clause = "deleted = 0")
    private Set<TransportOrderRequestDocument> documents = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IdNamePair getSubsidiary() {
        return subsidiary;
    }

    public void setSubsidiary(IdNamePair subsidiary) {
        this.subsidiary = subsidiary;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Company getCustomer() {
        return customer;
    }

    public void setCustomer(Company customer) {
        this.customer = customer;
    }

    public Long getAcCustomerId() {
        return acCustomerId;
    }

    public void setAcCustomerId(Long acCustomerId) {
        this.acCustomerId = acCustomerId;
    }

    public Company getAcCustomer() {
        return acCustomer;
    }

    public void setAcCustomer(Company acCustomer) {
        this.acCustomer = acCustomer;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public IdNamePair getContract() {
        return contract;
    }

    public void setContract(IdNamePair contract) {
        this.contract = contract;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getOfferNo() {
        return offerNo;
    }

    public void setOfferNo(String offerNo) {
        this.offerNo = offerNo;
    }

    public LocalDate getReadyAtDate() {
        return readyAtDate;
    }

    public void setReadyAtDate(LocalDate readyAtDate) {
        this.readyAtDate = readyAtDate;
    }

    public boolean isConfirmationRequired() {
        return confirmationRequired;
    }

    public void setConfirmationRequired(boolean confirmationRequired) {
        this.confirmationRequired = confirmationRequired;
    }

    public Integer getNumberOfOrders() {
        return numberOfOrders;
    }

    public void setNumberOfOrders(Integer numberOfOrders) {
        this.numberOfOrders = numberOfOrders;
    }

    public String getCustomerNote() {
        return customerNote;
    }

    public void setCustomerNote(String customerNote) {
        this.customerNote = customerNote;
    }

    public Set<TransportOrderRequestDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<TransportOrderRequestDocument> documents) {
        this.documents = documents;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public TransportOrder getOrder() {
        return order;
    }

    public void setOrder(TransportOrder order) {
        this.order = order;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

}
