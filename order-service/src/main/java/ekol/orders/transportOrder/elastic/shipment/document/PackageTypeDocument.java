package ekol.orders.transportOrder.elastic.shipment.document;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Created by ozer on 03/10/16.
 */
public class PackageTypeDocument {

    private String name;

    @Field(type = FieldType.Integer)
    private int count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public PackageTypeDocument(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public PackageTypeDocument() {
    }
}