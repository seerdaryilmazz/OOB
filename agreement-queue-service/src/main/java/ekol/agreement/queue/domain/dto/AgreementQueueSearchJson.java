package ekol.agreement.queue.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Created by Dogukan Sahinturk on 24.09.2019
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgreementQueueSearchJson {
    private Long agreementNumber;

    @DateTimeFormat(pattern="dd/MM/yyyy")
    private LocalDate minRequestDate;

    @DateTimeFormat(pattern="dd/MM/yyyy")
    private LocalDate maxRequestDate;

    private int page = 0;
    private int pageSize = 10;

}
