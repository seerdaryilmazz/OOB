package ekol.kartoteks.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ekol.kartoteks.builder.CompanyBuilder;
import ekol.kartoteks.domain.Company;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * Created by kilimci on 22/11/16.
 */
@RunWith(SpringRunner.class)
public class CompanyIdNameSerializerUnitTest {

    @Test
    public void shouldSerializeCompany() throws JsonProcessingException{
        Company company = CompanyBuilder.aCompany().withName("name").withId(1L).build();
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule testModule = new SimpleModule("MyModule", Version.unknownVersion());
        testModule.addSerializer(new IdNameSerializer());
        mapper.registerModule(testModule);
        String json = mapper.writeValueAsString(company);
        assertEquals("{\"id\":1,\"name\":\"name\"}", json);
    }

    @Test
    public void shouldSerializeNullObjectWhenCompanyIsNull() throws JsonProcessingException{

        ObjectMapper mapper = new ObjectMapper();
        SimpleModule testModule = new SimpleModule("MyModule", Version.unknownVersion());
        testModule.addSerializer(new IdNameSerializer());
        mapper.registerModule(testModule);
        String json = mapper.writeValueAsString(null);
        assertEquals("null", json);
    }

    @Test
    public void shouldSerializeCompanyIdAsNull() throws JsonProcessingException{
        Company company = CompanyBuilder.aCompany().withName("name").build();
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule testModule = new SimpleModule("MyModule", Version.unknownVersion());
        testModule.addSerializer(new IdNameSerializer());
        mapper.registerModule(testModule);
        String json = mapper.writeValueAsString(company);
        assertEquals("{\"id\":null,\"name\":\"name\"}", json);
    }
}
