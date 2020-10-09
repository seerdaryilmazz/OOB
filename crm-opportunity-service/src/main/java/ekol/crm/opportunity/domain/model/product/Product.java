package ekol.crm.opportunity.domain.model.product;

import com.fasterxml.jackson.annotation.JsonBackReference;
import ekol.crm.opportunity.domain.dto.product.ProductJson;
import ekol.crm.opportunity.domain.enumaration.ExistenceType;
import ekol.crm.opportunity.domain.model.MonetaryAmount;
import ekol.crm.opportunity.domain.model.Opportunity;
import ekol.hibernate5.domain.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DiscriminatorFormula;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Created by Dogukan Sahinturk on 14.11.2019
 */
@Entity
@Table(name = "CrmOpportunityProduct")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "serviceArea")
//@DiscriminatorFormula("case when service_area in ('ROAD','SEA','AIR','DTR') then 'FREIGHT' when 'CCL' then 'CCL' else '' end")
@SequenceGenerator(name = "SEQ_CRMOPPORTUNITYPRODUCT", sequenceName = "SEQ_CRMOPPORTUNITYPRODUCT")
@Where(clause = "deleted = 0")
@Getter
@Setter
@NoArgsConstructor
public abstract class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CRMOPPORTUNITYPRODUCT")
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opportunity_id")
    private Opportunity opportunity;

    @Enumerated(EnumType.STRING)
    private ExistenceType existenceType;

    @Column(insertable = false, updatable = false)
    private String serviceArea;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "expTurnoverAmount")),
            @AttributeOverride(name = "currency", column = @Column(name = "expTurnoverCurrency"))
    })
    private MonetaryAmount expectedTurnoverPerYear;

    public abstract ProductJson toJson();
}
