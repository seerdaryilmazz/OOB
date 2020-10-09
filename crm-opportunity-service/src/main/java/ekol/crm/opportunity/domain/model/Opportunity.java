package ekol.crm.opportunity.domain.model;

import ekol.crm.opportunity.domain.enumaration.OpportunityStatus;
import ekol.crm.opportunity.domain.model.product.Product;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Created by Dogukan Sahinturk on 14.11.2019
 */
@Entity
@Table(name = "CrmOpportunity")
@SequenceGenerator(name = "SEQ_CRMOPPORTUNITY", sequenceName = "SEQ_CRMOPPORTUNITY")
@Where(clause = "deleted = 0")
@Getter
@Setter
@NoArgsConstructor
public class Opportunity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CRMOPPORTUNITY")
    private Long id;

    @Column(name = "OPPORTUNITY_NUMBER", nullable = false)
    private Long number;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OpportunityStatus status;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "code", column = @Column(name = "SERVICE_AREA_CODE", nullable = false)),
            @AttributeOverride(name = "name", column = @Column(name = "SERVICE_AREA_NAME", nullable = false))})
    private CodeNamePair serviceArea;

    @Column(nullable = false)
    private String opportunityOwner;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dateTime", column = @Column(name = "createdAt"))
    })
    private UtcDateTime createdAt;

    @CreatedBy
    private String createdBy;

    @Column(nullable = false)
    private String name;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "id", column = @Column(name = "ACCOUNT_ID", nullable = false)),
            @AttributeOverride(name = "name", column = @Column(name = "ACCOUNT_NAME", nullable = false))})
    private IdNamePair account;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="id", column=@Column(name="ACCOUNT_LOCATION_ID", nullable = false)),
            @AttributeOverride(name = "name", column=@Column(name="ACCOUNT_LOCATION_NAME", nullable = false))})
    private IdNamePair accountLocation;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="id", column=@Column(name="SUBSIDIARY_ID", nullable = false)),
            @AttributeOverride(name = "name", column=@Column(name="SUBSIDIARY_NAME", nullable = false))})
    private IdNamePair ownerSubsidiary;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "EXP_TURNOVER_AMOUNT", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "EXP_TURNOVER_CURRENCY", nullable = false))
    })
    private MonetaryAmount expectedTurnoverPerYear;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "COM_TURNOVER_AMOUNT")),
            @AttributeOverride(name = "currency", column = @Column(name = "COM_TURNOVER_CURRENCY"))
    })
    private MonetaryAmount committedTurnoverPerYear;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "QUOTE_TURNOVER_AMOUNT")),
            @AttributeOverride(name = "currency", column = @Column(name = "QUOTE_TURNOVER_CURRENCY"))
    })
    private MonetaryAmount quotedTurnoverPerYear;

    @OneToMany(mappedBy="opportunity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Product> products = new HashSet<>();

    @OneToOne(mappedBy = "opportunity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private CloseReason closeReason;

    private LocalDate expectedQuoteDate;

    @ElementCollection
    @JoinTable(name="crmOpportunityAttribute", joinColumns=@JoinColumn(name="opportunityId"))
    @MapKeyColumn (name="ATTR_KEY")
    @Column(name="ATTR_VALUE")
    @LazyCollection(LazyCollectionOption.FALSE)
    private Map<String, String> opportunityAttribute = new HashMap<>();

    @Transient
    private List<Note> notes = new ArrayList<>();

    @Transient
    private List<Document> documents = new ArrayList<>();

    @Override
    @PrePersist
    public void prePersist() {
        if (null == createdAt && null == id) {
            createdAt = new UtcDateTime(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime());
        }
        super.prePersist();
    }

    public void setProducts(Set<Product> products) {
        this.products.clear();
        if (products != null) {
            this.products.addAll(products);
        }
    }
}
