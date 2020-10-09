package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Order extends BaseEntity {


    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
