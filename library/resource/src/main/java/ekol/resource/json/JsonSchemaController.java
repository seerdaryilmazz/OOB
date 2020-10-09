package ekol.resource.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import ekol.json.schema.JsonSchemaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ozer on 23/01/2017.
 */
@RestController
public class JsonSchemaController {

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @RequestMapping(value = "/schema", method = RequestMethod.GET)
    public JsonSchema getSchema(@RequestParam(name = "className") String className) {
        return JsonSchemaUtils.createSchemaFor(className, jacksonObjectMapper);
    }
}

