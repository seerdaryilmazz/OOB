package ekol.orders.order.domain.dto.json.updateOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.IdNamePair;
import ekol.orders.order.domain.dto.json.IdCodeNameTrio;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateHealthCertificateRequestJson {

    private Set<IdCodeNameTrio> healthCertificateTypes = new HashSet<>();
    private IdNamePair borderCustoms;
    private Boolean borderCrossingHealthCheck;

}
