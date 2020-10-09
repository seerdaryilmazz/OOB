package ekol.crm.opportunity.domain.model.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by Dogukan Sahinturk on 9.01.2020
 */
@Entity
@DiscriminatorValue("AIR")
@Getter
@Setter
@NoArgsConstructor
public class AirProduct extends FreightProduct {
}
