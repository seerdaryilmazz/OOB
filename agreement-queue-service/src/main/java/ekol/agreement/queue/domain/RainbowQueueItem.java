package ekol.agreement.queue.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.agreement.queue.domain.dto.AgreementJson;
import ekol.agreement.queue.enums.Status;
import ekol.agreement.queue.wscbfunitprice.wsdl.WSCBFUNITPRICEInput;
import ekol.agreement.queue.wscbfunitprice.wsdl.WSCBFUNITPRICEOutput;
import ekol.mongodb.domain.entity.BaseEntity;
import lombok.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by Dogukan Sahinturk on 24.09.2019
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Document(collection = "QueueItem")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RainbowQueueItem extends BaseEntity implements Persistable<String> {
    private Long agreementNumber;
    private AgreementJson agreementJson;
    private WSCBFUNITPRICEInput rawRequest;
    private Status status;
    private String request;
    private LocalDateTime requestDate;
    private String response;
    private LocalDateTime responseDate;

    @Override
    public boolean isNew() {
        return Objects.isNull(getId());
    }
}
