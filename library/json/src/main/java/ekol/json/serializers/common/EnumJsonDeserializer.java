package ekol.json.serializers.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class EnumJsonDeserializer<T extends Enum<T>> extends JsonDeserializer<T> {

    private static final String ID = "id";
    private static final String CODE = "code";
    private static final String ERROR_MSG = "Could not find id or code property in JSON string";

    // Deserialize ederken, deserialize edilecek tipi (yani Class'ı) bilmemiz gerekli çünkü
    // bu tipten bir nesne yaratıp döndürmemiz lazım.

    private Class<T> type;

    public EnumJsonDeserializer(Class<T> type) {
        this.type = type;
    }

    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String name;

        if (node.has(ID)) {
            name = node.get(ID).asText();
        } else if (node.has(CODE)) {
            name = node.get(CODE).asText();
        } else {
            throw new RuntimeException(ERROR_MSG);
        }

        return Enum.valueOf(type, name);
    }
}
