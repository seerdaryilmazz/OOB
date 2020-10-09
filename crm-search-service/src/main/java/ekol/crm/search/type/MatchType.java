package ekol.crm.search.type;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonSerialize(using = MatchType.CustomSerializer.class)
public enum MatchType {
	ACCOUNT_OWNER("Account Owner", null, "accountOwner", MatchFilterType.TEXT),
	ACCOUNT("Account", "account", "account.id", MatchFilterType.NUMERIC),
    ACCOUNT_LOCATION("Account Location", "accountLocation", "accountLocation.id", MatchFilterType.NUMERIC),
    POTENTIAL("Potential", null, "potentialId", MatchFilterType.NUMERIC),
    NUMBER("Number", null, "number", MatchFilterType.NUMERIC),
    SERVICE_AREA("Service Area Code", "serviceArea", "serviceArea.code", MatchFilterType.TEXT),
    ACCOUNT_ID("Account Id", null, "id", MatchFilterType.NUMERIC),
    COUNTRY_ISO("Country Iso", "country", "country.iso", MatchFilterType.TEXT),
	ACCOUNT_TYPE("Account Type", "accountType", "accountType.code", MatchFilterType.TEXT),
    SEGMENT("Segment Code", "segment", "segment.code", MatchFilterType.TEXT),
    STATUS("Status Code", "status", "status.code", MatchFilterType.TEXT),
    CITY("City", null, "city", MatchFilterType.TEXT),
    DISTRICT("District", null, "district", MatchFilterType.TEXT),
    PARENT_SECTOR("Parent Sector", "parentSector","parentSector.code", MatchFilterType.TEXT),
    SUB_SECTOR("Sub Sector", "subSector", "subSector.code", MatchFilterType.TEXT),
    MIN_CREATION_DATE("Min Creation Date", null, "createdAt", MatchFilterType.DATE ),
    MAX_CREATION_DATE("Max Creation Date", null, "createdAt", MatchFilterType.DATE ),
    CREATED_BY("Created By", null, "createdBy", MatchFilterType.TEXT)
    ;

    private final String name;
    private final String path;
    private final String field;
    private final MatchFilterType filterType;

    public boolean isNested() {
        return !Objects.isNull(getPath());
    }

    public QueryBuilder buildQuery(String val) {
    	return buildQuery(val, "eq");
    }
    
    public QueryBuilder buildQuery(String val, String operator) {
        if (StringUtils.isBlank(val)) {
            return null;
        }
        
        QueryBuilder queryBuilder = null;
        if("eq".equals(operator)) {
        	queryBuilder = QueryBuilders.matchQuery(getField(), val);
        } else {
        	queryBuilder = QueryBuilders.rangeQuery(getField());
        	if ("gte".equals(operator)) {
        		RangeQueryBuilder.class.cast(queryBuilder).gte(val);
        	} else if ("lte".equals(operator)) {
        		RangeQueryBuilder.class.cast(queryBuilder).lte(val);
        	}
        }

        if (isNested()) {
            return QueryBuilders.nestedQuery(getPath(), queryBuilder);
        } else {
            return queryBuilder;
        }
    }

    private static final Map<String, MatchType> MAP = Stream.of(MatchType.values()).collect(Collectors.toMap(MatchType::getName, m->m));

    public static MatchType fromName(String field) {
        return MAP.get(field);
    }

    public static class CustomSerializer extends JsonSerializer<MatchType> {

        @Override
        public void serialize(
                MatchType obj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                throws IOException {

            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("name");
            jsonGenerator.writeString(obj.getName());
            jsonGenerator.writeFieldName("path");
            jsonGenerator.writeString(obj.getPath());
            jsonGenerator.writeFieldName("field");
            jsonGenerator.writeString(obj.getField());
            jsonGenerator.writeFieldName("filterType");
            jsonGenerator.writeString(obj.getFilterType().name());
            jsonGenerator.writeEndObject();
        }
    }
}
