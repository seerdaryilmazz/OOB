package ekol.authorization.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import ekol.authorization.domain.auth.*;
import ekol.exceptions.BadRequestException;

import java.io.IOException;

/**
 * Created by kilimci on 03/01/2018.
 */
public class BaseEntityDeserializer  extends JsonDeserializer<BaseEntity> {

    @Override
    public BaseEntity deserialize(JsonParser jsonParser, DeserializationContext ctx) throws IOException {

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        String type = "";
        String name = "";
        Long id = null, externalId = null;
        if(node.has("type")) {
           type = node.get("type").asText();
        }
        if(node.has("name")) {
            name = node.get("name").asText();
        }
        if(node.has("id")) {
            id = node.get("id").asLong();
        }
        if(node.has("externalId")) {
            externalId = node.get("externalId").asLong();
        }
        BaseEntity entity;
        if(type.equals("Team")){
            entity = new AuthTeam();
        }else if(type.equals("Customer")){
            entity = new AuthCustomer();
        }else if(type.equals("Subsidiary")){
            entity = new AuthSubsidiary();
        }else if(type.equals("Sector")){
            entity = new AuthSector();
        }else if(type.equals("Department")){
            entity = new AuthDepartment();
        }else{
            throw new BadRequestException("Unknown node type: " + type);
        }

        entity.setName(name);
        entity.setId(id);
        entity.setExternalId(externalId);

        return entity;
    }
}
