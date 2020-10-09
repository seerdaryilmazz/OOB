package ekol.kartoteks.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import ekol.kartoteks.domain.IdNameSerializable;

import java.io.IOException;

/**
 * Created by kilimci on 25/06/16.
 */
public class IdNameSerializer extends JsonSerializer<IdNameSerializable> {

    @Override
    public Class<IdNameSerializable> handledType() {
        return IdNameSerializable.class;
    }
    @Override
    public void serialize(IdNameSerializable idName, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        if(idName == null){
            return;
        }
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("id");
        if(idName.getId() == null){
            jsonGenerator.writeNull();
        }else{
            jsonGenerator.writeNumber(idName.getId());
        }
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeString(idName.getName());
        jsonGenerator.writeEndObject();
    }
}
