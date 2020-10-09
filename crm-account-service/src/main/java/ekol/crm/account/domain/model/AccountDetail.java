package ekol.crm.account.domain.model;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.annotations.Type;


@Embeddable
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AccountDetail{

    @Column
    private String totalLogisticsPotential;

    @Column(length = 300)
    private String strategicInformation;

    @Column(name = "FORTUNE_500")
    private Boolean fortune500;

    @Column
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean galaxy;

    @Column
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean global;

    @Column(length = 20)
    private Long globalAccountId;

    @Column(length = 100)
    private String globalAccountOwner;

}
