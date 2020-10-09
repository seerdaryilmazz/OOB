package ekol.agreement.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.*;

import org.hibernate.annotations.Where;

import ekol.agreement.domain.dto.agreement.HistoryUnitPriceJson;
import ekol.agreement.domain.enumaration.*;
import ekol.agreement.service.QuoteService;
import ekol.agreement.util.BeanUtils;
import ekol.hibernate5.domain.entity.BaseEntity;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "HistoryUnitPrice")
@SequenceGenerator(name = "SEQ_HISTORYUNITPRICE", sequenceName = "SEQ_HISTORYUNITPRICE")
@Builder(toBuilder = true)
@Where(clause = "deleted = 0")
@NoArgsConstructor
@AllArgsConstructor
public class HistoryUnitPrice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_HISTORYUNITPRICE")
    private Long id;

    @Column
    private String billingItem;

    @Column(nullable = false)
    private String serviceName;

    @Column(precision = 6, scale = 2)
    private BigDecimal price;

    @Column(length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    private BasedOnType basedOn;

    @Column(scale = 2)
    private BigDecimal eurRef;

    @Column(scale = 2)
    private BigDecimal usdRef;

    @Column(scale = 2)
    private BigDecimal minimumWageRef;

    @Column(precision = 3)
    private Integer inflationRef;

    @Column(nullable = false)
    private LocalDate validityStartDate;

    @Column
    private Integer updatePeriod;

    @Enumerated(EnumType.STRING)
    private RenewalDateType renewalDateType;

    @Column(nullable = false)
    private LocalDate validityEndDate;

    @Column
    private Long priceModelId;

    @Column
    private String notes;

    @Column(nullable = false)
    private Long unitPriceId;

    public HistoryUnitPriceJson toJson(){
        HistoryUnitPriceJson json = new HistoryUnitPriceJson();
        json.setId(getId());
        json.setBillingItem(BeanUtils.getBean(QuoteService.class).getBillingItemByName(getBillingItem(),true));
        json.setServiceName(getServiceName());
        json.setPrice(getPrice());
        json.setCurrency(getCurrency());
        json.setBasedOn(getBasedOn());
        json.setEurRef(getEurRef());
        json.setUsdRef(getUsdRef());
        json.setMinimumWageRef(getMinimumWageRef());
        json.setInflationRef(getInflationRef());
        json.setValidityStartDate(getValidityStartDate());
        json.setUpdatePeriod(getUpdatePeriod());
        json.setRenewalDateType(getRenewalDateType());
        json.setValidityEndDate(getValidityEndDate());
        json.setNotes(getNotes());
        json.setUnitPriceId(getUnitPriceId());
        json.setLastUpdated(getLastUpdated());
        return json;
    }
}
