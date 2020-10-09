package ekol.kartoteks.repository;

import ekol.kartoteks.testdata.SomePersistedData;
import ekol.kartoteks.domain.Company;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static ekol.kartoteks.testdata.SomeData.someCompany;
import static org.junit.Assert.assertNotNull;

/**
 * Created by kilimci on 04/10/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
public class CompanyRepositoryIntegrationTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private SomePersistedData persistedData;

    private List<Long> cleanupIds = new ArrayList<>();

    @Before
    public void init(){
        cleanup();
    }
    @After
    public void cleanup(){
        cleanupIds.forEach(id -> {
            Company c = companyRepository.findOne(id);
            c.setDeleted(true);
            companyRepository.save(c);
        });
        cleanupIds.clear();
    }

    private Company prepareCompany(){
        Company someCompany = someCompany().build();
        someCompany.setCountry(persistedData.someCountry());
        someCompany.setSegmentType(persistedData.someCompanySegmentType());
        someCompany.setType(persistedData.someCompanyType());
        someCompany.setTaxOffice(persistedData.someTaxOffice());
        return someCompany;
    }

    @Test
    public void shouldSaveCompany() {
        Company c = companyRepository.save(prepareCompany());
        assertNotNull(c.getId());
        cleanupIds.add(c.getId());
    }

}
