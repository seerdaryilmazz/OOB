package ekol.json.serializers.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Map;

public class EnumJsonSerializer<T extends Enum<T>> extends JsonSerializer<T> {

    private static final String ID = "id";
    private static final String CODE = "code";
    private static final String NAME = "name";

    private ConverterType defaultConverterType;

    private Map<String, ConverterType> fieldConverterTypeMap;

    public EnumJsonSerializer(ConverterType defaultConverterType, Map<String, ConverterType> fieldConverterTypeMap) {

        this.defaultConverterType = defaultConverterType;

        this.fieldConverterTypeMap = fieldConverterTypeMap;
    }

    @Override
    public void serialize(T obj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName(ID);
        jsonGenerator.writeString(obj.name());
        jsonGenerator.writeFieldName(CODE);
        jsonGenerator.writeString(obj.name());
        jsonGenerator.writeFieldName(NAME);

        ConverterType converterType = fieldConverterTypeMap != null ? fieldConverterTypeMap.get(obj.name()) : null;
        if(converterType == null) {
            converterType = this.defaultConverterType;
        }

        jsonGenerator.writeString(converterType.convert(obj));
        jsonGenerator.writeEndObject();
    }
}
