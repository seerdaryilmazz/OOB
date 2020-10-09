package ekol.crm.account.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.account.domain.enumaration.AccountType;
import ekol.crm.account.domain.enumaration.SegmentType;
import lombok.*;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor


public class AccountSearchCriteria {
    private Integer page;
    private String name;
    private String countryCode;
    private Long id;
    private AccountType accountType;
    private SegmentType segmentType;
    private String accountOwner;
    private String parentSectorCode;
    private String subSectorCode;
    private String city;
    private String district;
    private LocalDate minCreationDate;
    private LocalDate maxCreationDate;
}
