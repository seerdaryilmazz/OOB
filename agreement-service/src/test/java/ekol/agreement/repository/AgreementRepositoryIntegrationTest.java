package ekol.agreement.repository;

import ekol.agreement.builder.AgreementBuilder;
import ekol.agreement.domain.enumaration.AgreementType;
import ekol.agreement.domain.enumaration.ServiceArea;
import ekol.agreement.domain.model.agreement.Agreement;
import ekol.agreement.domain.model.agreement.AgreementLogistic;
import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
public class AgreementRepositoryIntegrationTest {

    @Autowired
    private AgreementRepository agreementRepository;

    @Test
    public void shouldSaveAgreement() {
//        AgreementLogistic agreement = AgreementBuilder.anAgreement()
//                .withNumber(0000001L)
//                .withName("UNILEVER WAREHOUSE CONTRACT")
//                .withType(AgreementType.CONTRACT)
//                .withAccount(IdNamePair.with(12L, "DenemeAcc"))
//                .withStartDate(LocalDate.now())
//                .withEndDate(LocalDate.now().plus(1, ChronoUnit.DAYS))
//                .withServiceAreas(new HashSet<>(Arrays.asList(ServiceArea.AIR, ServiceArea.BOUNDED_WAREHOUSE))).build();
//        AgreementLogistic savedAgreement = agreementRepository.save(agreement);
//        assertNotNull(savedAgreement.getId());
    }
}
