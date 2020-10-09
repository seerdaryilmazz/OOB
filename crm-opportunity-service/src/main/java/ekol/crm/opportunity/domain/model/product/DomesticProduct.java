package ekol.crm.opportunity.domain.model.product;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by Dogukan Sahinturk on 9.01.2020
 */
@Entity
@DiscriminatorValue("DTR")
public class DomesticProduct extends FreightProduct {
}
