package ekol.crm.quote.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import ekol.crm.quote.domain.model.quote.Quote;
import ekol.hibernate5.domain.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@Table(name = "CrmQuoteIdMapping")
@Where(clause = "deleted = 0")
@SequenceGenerator(name = "SEQ_CRMQUOTEIDMAPPING", sequenceName = "SEQ_CRMQUOTEIDMAPPING")
public class QuoteIdMapping extends BaseEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CRMQUOTEIDMAPPING")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "quote_id")
    private Quote quote;

    @Column(nullable = false, length = 20)
    private String application;

    @Column(nullable = false, length = 20)
    private String applicationQuoteId;

    public static QuoteIdMapping withApplication(String application, String id){
    	QuoteIdMapping mapping = new QuoteIdMapping();
        mapping.setApplication(application);
        mapping.setApplicationQuoteId(id);
        return mapping;
    }
}
