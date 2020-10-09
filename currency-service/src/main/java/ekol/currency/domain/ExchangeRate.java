package ekol.currency.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "ExchangeRate")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRate extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_exchange_rate", sequenceName = "seq_exchange_rate")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_exchange_rate")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ExchangeRatePublisher publisher;

    private LocalDate publishDate;

    private Integer unit;

    private String fromCurrency;

    private String toCurrency;

    private BigDecimal value;

    private BigDecimal forexBuyingValue;

    private BigDecimal forexSellingValue;

    private BigDecimal banknoteBuyingValue;

    private BigDecimal banknoteSellingValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExchangeRatePublisher getPublisher() {
        return publisher;
    }

    public void setPublisher(ExchangeRatePublisher publisher) {
        this.publisher = publisher;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getForexBuyingValue() {
        return forexBuyingValue;
    }

    public void setForexBuyingValue(BigDecimal forexBuyingValue) {
        this.forexBuyingValue = forexBuyingValue;
    }

    public BigDecimal getForexSellingValue() {
        return forexSellingValue;
    }

    public void setForexSellingValue(BigDecimal forexSellingValue) {
        this.forexSellingValue = forexSellingValue;
    }

    public BigDecimal getBanknoteBuyingValue() {
        return banknoteBuyingValue;
    }

    public void setBanknoteBuyingValue(BigDecimal banknoteBuyingValue) {
        this.banknoteBuyingValue = banknoteBuyingValue;
    }

    public BigDecimal getBanknoteSellingValue() {
        return banknoteSellingValue;
    }

    public void setBanknoteSellingValue(BigDecimal banknoteSellingValue) {
        this.banknoteSellingValue = banknoteSellingValue;
    }
}
