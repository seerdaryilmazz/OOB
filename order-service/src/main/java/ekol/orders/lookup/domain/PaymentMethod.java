package ekol.orders.lookup.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.LookupEntity;
import ekol.model.IdNamePair;
import ekol.orders.order.domain.dto.json.IdCodeNameTrio;

import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "payment_method")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentMethod extends LookupEntity {

    @Id
    @SequenceGenerator(name = "seq_payment_method", sequenceName = "seq_payment_method")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_payment_method")
    private Long id;

    public static PaymentMethod with(IdNamePair idNamePair){
        return PaymentMethod.with(idNamePair.getId(), idNamePair.getName());
    }
    public static PaymentMethod with(IdCodeNameTrio idCodeNameTrio){
    	return PaymentMethod.with(idCodeNameTrio.getId(), idCodeNameTrio.getCode(), idCodeNameTrio.getName());
    }
    public static PaymentMethod with(Long id, String name){
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(id);
        paymentMethod.setName(name);
        return paymentMethod;
    }
    public static PaymentMethod with(Long id, String code, String name){
    	PaymentMethod paymentMethod = new PaymentMethod();
    	paymentMethod.setId(id);
    	paymentMethod.setCode(code);
    	paymentMethod.setName(name);
    	return paymentMethod;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

}
