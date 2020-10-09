package ekol.orders.transportOrder.elastic.shipment.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ozer on 10/10/16.
 */
@JsonSerialize(using = SortType.CustomSerializer.class)
public enum SortType {
    CUSTOMER_NAME("Customer Name", "customerName.tr", null),
    READY_DATE("Ready Date", "readyAtDate", null),
    PICKUP_APPOINTMENT("Pickup Appointment", "pickupAppointment.start", "pickupAppointment"),
    DELIVERY_APPOINTMENT("Delivery Appointment", "deliveryAppointment.start", "deliveryAppointment"),
    SENDER_COMPANY("Sender Company", "sender.companyName.tr", "sender"),
    SENDER_LOCATION_OWNER("Sender Location Owner", "sender.locationOwnerCompanyName.tr", "sender"),
    RECEIVER_COMPANY("Consignee Company", "receiver.companyName.tr", "receiver"),
    RECEIVER_LOCATION_OWNER("Consignee Location Owner", "receiver.locationOwnerCompanyName.tr", "receiver"),
    SHIPMENT_NO("Shipment No", "shipmentId", null),
    ORDER_NO("Order No", "transportOrderId", null),
    GROSS_WEIGHT("Gross Weight", "grossWeight", null),
    VOLUME("Volume", "volume", null),
    LDM("LDM", "ldm", null),
    CURRENT_FROM("From", "currentFrom.location.name.tr", "currentFrom.location"),
    CURRENT_TO("To", "currentTo.location.name.tr", "currentTo.location"),
    RECEIVER_LOCATION_NAME("Consignee Location Name", "receiver.location.name.tr", "receiver.location");

    private final String name;
    private final String field;
    private final String nestedPath;

    public String getName() {
        return name;
    }

    public String getField() {
        return field;
    }

    public String getNestedPath() {
        return nestedPath;
    }

    SortType(String name, String field, String nestedPath) {
        this.name = name;
        this.field = field;
        this.nestedPath = nestedPath;
    }

    public SortBuilder buildSort(SortDirection sortDirection) {
        FieldSortBuilder fieldSortBuilder = SortBuilders.fieldSort(field).order(sortDirection == SortDirection.DESC ? SortOrder.DESC : SortOrder.ASC);
        if (nestedPath != null) {
            fieldSortBuilder.setNestedPath(nestedPath);
        }
        return fieldSortBuilder;
    }

    private static Map<String, SortType> MAP = new HashMap<>();

    static {
        Arrays.asList(SortType.values()).forEach(sortType -> MAP.put(sortType.getName(), sortType));
    }

    public static SortType fromName(String name) {
        return MAP.get(name);
    }

    public static enum SortDirection {
        ASC, DESC
    }

    public static class CustomSerializer extends JsonSerializer<SortType> {

        @Override
        public void serialize(
                SortType obj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                throws IOException {

            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("name");
            jsonGenerator.writeString(obj.getName());
            jsonGenerator.writeFieldName("field");
            jsonGenerator.writeString(obj.getField());
            jsonGenerator.writeFieldName("nestedPath");
            jsonGenerator.writeString(obj.getNestedPath());
            jsonGenerator.writeEndObject();
        }
    }
}
