package ekol.location.domain.location.comnon;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import ekol.location.domain.location.customs.CustomsOffice;

import java.io.IOException;

public class CustomsOfficeSerializer extends JsonSerializer<CustomsOffice> {

    @Override
    public Class<CustomsOffice> handledType() {
        return CustomsOffice.class;
    }

    @Override
    public void serialize(CustomsOffice customsOffice, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        if(customsOffice == null){
            return;
        }
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("id");
        if(customsOffice.getId() == null){
            jsonGenerator.writeNull();
        }else{
            jsonGenerator.writeNumber(customsOffice.getId());
        }
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeString(customsOffice.getName());
        jsonGenerator.writeFieldName("customsCode");
        jsonGenerator.writeString(customsOffice.getCustomsCode());
        jsonGenerator.writeEndObject();
    }
}