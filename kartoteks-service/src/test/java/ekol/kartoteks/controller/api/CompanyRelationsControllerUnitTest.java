package ekol.kartoteks.controller.api;

import ekol.kartoteks.controller.CompanyRelationsController;
import ekol.kartoteks.domain.Company;
import ekol.kartoteks.domain.CompanyRelation;
import ekol.kartoteks.domain.CompanyRelationType;
import ekol.kartoteks.repository.CompanyRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by burak on 25/07/16.
 */
@RunWith(SpringRunner.class)
public class CompanyRelationsControllerUnitTest {

    @InjectMocks
    private CompanyRelationsController companyRelationsApiController;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CompanyRelationType companyRelationType;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void shouldIncludeActiveLogisticRelations() {

        CompanyRelationType crtype = new CompanyRelationType();
        crtype.setCode("ZCRME2");

        Company c1 = new Company();
        c1.setName("c1");

        Company c2 = new Company();
        c2.setName("c2");

        CompanyRelation cr2 = new CompanyRelation();
        cr2.setRelationType(crtype);
        cr2.setActiveCompany(c2);
        cr2.setPassiveCompany(c1);

        Set<CompanyRelation> pasiveRelations = new HashSet<>();
        pasiveRelations.add(cr2);

        c1.setPassiveRelations(pasiveRelations);

        when(companyRepository.findById(new Long(123))).thenReturn(c1);

        List<Company> activeCompanies = companyRelationsApiController.findAgentOrLogisticPartner(new Long(123));

        assertEquals(true, activeCompanies.contains(c2));

    }

    @Test
    public void shouldIncludeActiveAgentRelations() {

        CompanyRelationType crtype = new CompanyRelationType();
        crtype.setCode("ZCPG01");

        Company c1 = new Company();
        c1.setName("c1");

        Company c2 = new Company();
        c2.setName("c2");

        CompanyRelation cr2 = new CompanyRelation();
        cr2.setRelationType(crtype);
        cr2.setActiveCompany(c2);
        cr2.setPassiveCompany(c1);

        Set<CompanyRelation> pasiveRelations = new HashSet<>();
        pasiveRelations.add(cr2);

        c1.setPassiveRelations(pasiveRelations);

        when(companyRepository.findById(new Long(123))).thenReturn(c1);

        List<Company> activeCompanies = companyRelationsApiController.findAgentOrLogisticPartner(new Long(123));

        assertEquals(true, activeCompanies.contains(c2));

    }

    @Test
    public void shouldNotIncludePassive() {

        CompanyRelationType crtype = new CompanyRelationType();
        crtype.setCode("ZCPG01");

        Company c1 = new Company();
        c1.setName("c1");

        Company c2 = new Company();
        c2.setName("c2");

        CompanyRelation cr2 = new CompanyRelation();
        cr2.setRelationType(crtype);
        cr2.setActiveCompany(c1);
        cr2.setPassiveCompany(c2);

        Set<CompanyRelation> activeRelations = new HashSet<>();
        activeRelations.add(cr2);

        c1.setActiveRelations(activeRelations);

        when(companyRepository.findById(new Long(123))).thenReturn(c1);

        List<Company> activeCompanies = companyRelationsApiController.findAgentOrLogisticPartner(new Long(123));

        assertEquals(false, activeCompanies.contains(c2));

    }

    @Test
    public void shouldNotIncludeNonLogisticsOrNonAgent() {

        CompanyRelationType crtype = new CompanyRelationType();
        crtype.setCode("ZCPG01asdasd");

        Company c1 = new Company();
        c1.setName("c1");

        Company c2 = new Company();
        c2.setName("c2");

        CompanyRelation cr2 = new CompanyRelation();
        cr2.setRelationType(crtype);
        cr2.setActiveCompany(c2);
        cr2.setPassiveCompany(c1);

        Set<CompanyRelation> pasiveRelations = new HashSet<>();
        pasiveRelations.add(cr2);

        c1.setPassiveRelations(pasiveRelations);

        when(companyRepository.findById(new Long(123))).thenReturn(c1);

        List<Company> activeCompanies = companyRelationsApiController.findAgentOrLogisticPartner(new Long(123));

        assertEquals(false, activeCompanies.contains(c2));

    }

}
