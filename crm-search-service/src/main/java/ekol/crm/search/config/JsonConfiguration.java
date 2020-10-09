package ekol.crm.search.config;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

@Configuration
public class JsonConfiguration {
	
	@Autowired
    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;
	
	@PostConstruct
	private void init() {
		ParsedStringTermsBucketSerializer serializer = new ParsedStringTermsBucketSerializer(StringTerms.Bucket.class);
        SimpleModule module = new SimpleModule();
        module.addSerializer(serializer);

        ObjectMapper mapper = mappingJackson2HttpMessageConverter.getObjectMapper();
		mapper.registerModule(module);
	}
	
	public static class ParsedStringTermsBucketSerializer extends StdSerializer<StringTerms.Bucket> {

	    public ParsedStringTermsBucketSerializer(Class<StringTerms.Bucket> t) {
	        super(t);
	    }

	    @Override
	    public void serialize(StringTerms.Bucket value, JsonGenerator gen, SerializerProvider provider) throws IOException {
	        gen.writeStartObject();
	        gen.writeObjectField("aggregations", value.getAggregations());
	        gen.writeObjectField("key", value.getKey());
	        gen.writeStringField("keyAsString", value.getKeyAsString());
	        gen.writeNumberField("docCount", value.getDocCount());
	        gen.writeEndObject();
	    }
	}

}
