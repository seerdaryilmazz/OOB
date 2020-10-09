package ekol.orders.lookup.domain;


import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum DocumentTypeGroup {
    ORDER, TRANSPORTATION, DANGEROUS_GOODS, HEALTH_CERTIFICATE, AGENT_DOCUMENTS, COMMUNICATION, OTHER
}
