package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.orders.lookup.domain.Incoterm;
import ekol.orders.lookup.domain.PaymentMethod;
import ekol.orders.lookup.domain.ServiceType;
import ekol.orders.lookup.domain.TruckLoadType;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by burak on 09/09/16.
 */

@Entity
@Table(name = "order_template")
@Where(clause = "deleted = 0")
@SequenceGenerator(name = "SEQ_ORDERTEMPLATE", sequenceName = "SEQ_ORDERTEMPLATE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderTemplate extends BaseEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_ORDERTEMPLATE")
    private Long id;

    private String code;

    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable()
    private Set<ServiceType> serviceTypes = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable()
    private Set<Incoterm> incoterms = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="order_template_tl_types")
    private Set<TruckLoadType> truckLoadTypes = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable()
    private Set<PaymentMethod> paymentMethods = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable()
    private Set<CurrencyType> currencyTypes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ServiceType> getServiceTypes() {
        return serviceTypes;
    }

    public void setServiceTypes(Set<ServiceType> serviceTypes) {
        this.serviceTypes = serviceTypes;
    }

    public Set<Incoterm> getIncoterms() {
        return incoterms;
    }

    public void setIncoterms(Set<Incoterm> incoterms) {
        this.incoterms = incoterms;
    }

    public Set<TruckLoadType> getTruckLoadTypes() {
        return truckLoadTypes;
    }

    public void setTruckLoadTypes(Set<TruckLoadType> truckLoadTypes) {
        this.truckLoadTypes = truckLoadTypes;
    }

    public Set<PaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(Set<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public Set<CurrencyType> getCurrencyTypes() {
        return currencyTypes;
    }

    public void setCurrencyTypes(Set<CurrencyType> currencyTypes) {
        this.currencyTypes = currencyTypes;
    }
}
