package ekol.usermgr.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * Created by ozer on 17/02/2017.
 */
public class PasswordSerializer extends JsonSerializer<String> {

    public static final String DEFAULT_PASSWORD_DISPLAY = "*****";

    @Override
    public void serialize(String password, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (StringUtils.isNotBlank(password)) {
            jsonGenerator.writeString(DEFAULT_PASSWORD_DISPLAY);
        } else {
            jsonGenerator.writeString("");
        }
    }
}