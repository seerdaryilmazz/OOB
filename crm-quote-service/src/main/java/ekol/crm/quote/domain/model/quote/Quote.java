package ekol.crm.quote.domain.model.quote;

import java.time.*;
import java.util.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.*;
import org.hibernate.envers.*;
import org.springframework.data.annotation.CreatedBy;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import ekol.crm.quote.domain.dto.SupportedLocale;
import ekol.crm.quote.domain.dto.quote.QuoteJson;
import ekol.crm.quote.domain.enumaration.*;
import ekol.crm.quote.domain.model.*;
import ekol.crm.quote.domain.model.product.Product;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.model.IdNamePair;
import lombok.*;

@Entity
@Table(name = "CrmQuote")
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(name = "SEQ_CRMQUOTE", sequenceName = "SEQ_CRMQUOTE")
@Where(clause = "deleted = 0")
@Getter
@Setter
@NoArgsConstructor
@Audited
public abstract class Quote extends AuditedBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CRMQUOTE")
    private Long id;

    @Column(name = "QUOTE_NUMBER")
    private Long number;

    private String name;
    
    @Column(name = "DEFAULT_INVOICE_COUNTRY")
    private String defaultInvoiceCompanyCountry;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="id", column=@Column(name="ACCOUNT_ID")),
            @AttributeOverride(name = "name", column=@Column(name="ACCOUNT_NAME"))})
    private IdNamePair account;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="id", column=@Column(name="ACCOUNT_LOCATION_ID")),
            @AttributeOverride(name = "name", column=@Column(name="ACCOUNT_LOCATION_NAME"))})
    private IdNamePair accountLocation;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="id", column=@Column(name="SUBSIDIARY_ID")),
            @AttributeOverride(name = "name", column=@Column(name="SUBSIDIARY_NAME"))})
    private IdNamePair subsidiary;

    @Enumerated(EnumType.STRING)
    private QuoteType type;

    @Column
    private String serviceArea;

    @Enumerated(EnumType.STRING)
    private QuoteStatus status;

    @OneToMany(mappedBy="quote", fetch = FetchType.EAGER)
    @Where(clause="deleted=0")
    @JsonManagedReference
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Product> products = new HashSet<>();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dateTime", column = @Column(name = "createdAt"))
    })
    private UtcDateTime createdAt;

    @CreatedBy
    private String createdBy;

    @Column(nullable = false)
    private String quoteOwner;

    private Boolean holdingForCompanyTransfer = Boolean.FALSE;
    
    @Transient
    private List<Note> notes = new ArrayList<>();

    @Transient
    private List<Document> documents = new ArrayList<>();

    @Transient
    private boolean initial;
    
    @OneToMany(mappedBy="quote")
    @Where(clause="deleted=0")
    @NotAudited
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<QuoteIdMapping> mappedIds = new HashSet<>() ;
    
    @Transient
    private SupportedLocale pdfLanguage;
    
    @ElementCollection
    @JoinTable(name="crmQuoteAttribute", joinColumns=@JoinColumn(name="quoteId"))
    @MapKeyColumn (name="ATTR_KEY")
    @Column(name="ATTR_VALUE")
    @LazyCollection(LazyCollectionOption.FALSE)
    private Map<String, String> quoteAttribute = new HashMap<>();

    @OneToMany(mappedBy="quote")
    @Where(clause="deleted=0")
    @NotAudited
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<QuoteOrderMapping> orders = new HashSet<>() ;
    
    
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dateTime", column = @Column(name = "requestedDate"))
    })
    private UtcDateTime requestedDate;

    public abstract QuoteJson toJson();

    public abstract void adjustName();
    

    @Override
    @PrePersist
    public void prePersist() {
    	if (null == createdAt && null == id) {
    		createdAt = new UtcDateTime(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime());
        }
        super.prePersist();
    }

}
