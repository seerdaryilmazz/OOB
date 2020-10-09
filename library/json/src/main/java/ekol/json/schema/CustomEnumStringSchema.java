package ekol.json.schema;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
import com.fasterxml.jackson.module.jsonSchema.types.StringSchema;
import ekol.json.annotation.CustomSchemaType;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by ozer on 20/01/2017.
 */
public class CustomEnumStringSchema extends StringSchema {

    private CustomSchemaType customSchemaType;
    private static final Map<String, Map<String, String>> PROPERTIES;

    static {
        Map<String, String> stringType = new LinkedHashMap<>();
        stringType.put("type", "string");

        PROPERTIES = new LinkedHashMap<>();
        PROPERTIES.put("id", stringType);
        PROPERTIES.put("code", stringType);
        PROPERTIES.put("name", stringType);
    }

    public CustomEnumStringSchema(CustomSchemaType customSchemaType) {
        super();
        this.customSchemaType = customSchemaType;
    }

    public CustomSchemaType getCustomSchemaType() {
        return customSchemaType;
    }

    public void setCustomSchemaType(CustomSchemaType customSchemaType) {
        this.customSchemaType = customSchemaType;
    }

    public Map<String, Map<String, String>> getProperties() {
        return PROPERTIES;
    }

    @Override
    public JsonFormatTypes getType() {
        return JsonFormatTypes.OBJECT;
    }
}
