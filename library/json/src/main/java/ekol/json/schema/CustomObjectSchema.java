package ekol.json.schema;

import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema;
import ekol.json.annotation.CustomSchemaType;

/**
 * Created by ozer on 20/01/2017.
 */
public class CustomObjectSchema extends ObjectSchema {

    private CustomSchemaType customSchemaType;

    public CustomObjectSchema(CustomSchemaType customSchemaType) {
        super();
        this.customSchemaType = customSchemaType;
    }

    public CustomSchemaType getCustomSchemaType() {
        return customSchemaType;
    }

    public void setCustomSchemaType(CustomSchemaType customSchemaType) {
        this.customSchemaType = customSchemaType;
    }
}
