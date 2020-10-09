package ekol.json.schema;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.module.jsonSchema.factories.VisitorContext;
import com.fasterxml.jackson.module.jsonSchema.factories.WrapperFactory;

/**
 * Created by ozer on 20/01/2017.
 */
public class OneOrderWrapperFactory extends WrapperFactory {

    @Override
    public OneOrderSchemaFactoryWrapper getWrapper(SerializerProvider provider) {
        return new OneOrderSchemaFactoryWrapper(provider);
    }

    @Override
    public OneOrderSchemaFactoryWrapper getWrapper(SerializerProvider provider, VisitorContext rvc) {
        OneOrderSchemaFactoryWrapper wrapper = new OneOrderSchemaFactoryWrapper(provider);
        wrapper.setVisitorContext(rvc);
        return wrapper;
    }
}
