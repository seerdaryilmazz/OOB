package ekol.crm.account.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import ekol.crm.account.domain.model.Account;
import ekol.crm.account.domain.model.potential.Potential;
import ekol.crm.account.service.AccountCrudService;
import ekol.crm.account.service.PotentialCrudService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
public class AccountControllerIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mvc;

    @MockBean
    private AccountCrudService accountCrudService;

    @MockBean
    private PotentialCrudService potentialCrudService;

    @Captor
    private ArgumentCaptor<Account> accountCaptor;

    @Captor
    private ArgumentCaptor<ArrayList<Potential>> potentialListCaptor;

    @Autowired
    ObjectMapper objectMapper;

    @Before
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilter(springSecurityFilterChain).build();
    }

    @Test
    public void shouldCreateAccount() throws Exception{


    }

    public byte[] convertObjectToJsonBytes(Object object) throws IOException {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper.writeValueAsBytes(object);
    }
}


