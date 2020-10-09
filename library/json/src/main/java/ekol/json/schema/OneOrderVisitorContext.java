package ekol.json.schema;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.module.jsonSchema.factories.VisitorContext;

/**
 * Created by ozer on 20/01/2017.
 */
public class OneOrderVisitorContext extends VisitorContext {

    @Override
    public String getSeenSchemaUri(JavaType aSeenSchema) {
        // To ease schema parsing, pretend you have not seen any schemas before
        // Otherwise you will have $ref's
        return null;
    }
}
