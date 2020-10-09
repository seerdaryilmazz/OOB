package ekol.currency.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.currency.domain.ExchangeRate;
import ekol.currency.domain.ExchangeRatePublisher;
import ekol.hibernate5.domain.embeddable.UtcDateTime;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRateForUI {

    private Long id;

    private ExchangeRatePublisher publisher;

    private LocalDate publishDate;

    private Integer unit;

    private StringIdCodeNameTriple fromCurrency;

    private StringIdCodeNameTriple toCurrency;

    private BigDecimal value;

    private BigDecimal forexBuyingValue;

    private BigDecimal forexSellingValue;

    private BigDecimal banknoteBuyingValue;

    private BigDecimal banknoteSellingValue;
    
    private UtcDateTime lastUpdated;
    
    private String lastUpdatedBy;

    private boolean deleted;
    
    private UtcDateTime deletedAt;
    
    public static ExchangeRateForUI convert(ExchangeRate er) {
        ExchangeRateForUI obj = new ExchangeRateForUI();
        obj.setId(er.getId());
        obj.setPublisher(er.getPublisher());
        obj.setPublishDate(er.getPublishDate());
        obj.setUnit(er.getUnit());
        obj.setFromCurrency(new StringIdCodeNameTriple(er.getFromCurrency(), er.getFromCurrency(), er.getFromCurrency()));
        obj.setToCurrency(new StringIdCodeNameTriple(er.getToCurrency(), er.getToCurrency(), er.getToCurrency()));
        obj.setValue(er.getValue());
        obj.setForexBuyingValue(er.getForexBuyingValue());
        obj.setForexSellingValue(er.getForexSellingValue());
        obj.setBanknoteBuyingValue(er.getBanknoteBuyingValue());
        obj.setBanknoteSellingValue(er.getBanknoteSellingValue());
        obj.setLastUpdated(er.getLastUpdated());
        obj.setLastUpdatedBy(er.getLastUpdatedBy());
        obj.setDeleted(er.isDeleted());
        obj.setDeletedAt(er.getDeletedAt());
        return obj;
    }

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

    public StringIdCodeNameTriple getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(StringIdCodeNameTriple fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public StringIdCodeNameTriple getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(StringIdCodeNameTriple toCurrency) {
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

    public UtcDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public UtcDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
