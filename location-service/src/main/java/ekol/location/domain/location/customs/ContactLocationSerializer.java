package ekol.location.domain.location.customs;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ContactLocationSerializer extends JsonSerializer<CustomsOfficeLocation> {

    @Override
    public Class<CustomsOfficeLocation> handledType() {
        return CustomsOfficeLocation.class;
    }
    @Override
    public void serialize(CustomsOfficeLocation location, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        if(location == null){
            return;
        }
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("id");
        if(location.getId() == null){
            jsonGenerator.writeNull();
        }else{
            jsonGenerator.writeNumber(location.getId());
        }
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeString(location.getName());
        jsonGenerator.writeEndObject();
    }
}
