package ekol.agreement.domain.model;

import ekol.agreement.domain.enumaration.StampTaxPayer;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Embeddable
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class FinancialInfo {

    @Column(precision = 11, scale = 2)
    private BigDecimal contractAmount;

    @Column(length = 3)
    private String contractAmountCurrency;

    private Integer paymentDueDays;

    @Enumerated(EnumType.STRING)
    private StampTaxPayer stampTaxPayer;

    @Column(precision = 11, scale = 2)
    private BigDecimal stampTaxAmount;

    @Column(length = 3)
    private String stampTaxCurrency;

    private LocalDate stampTaxDueDate;

    private Boolean paid;
}
