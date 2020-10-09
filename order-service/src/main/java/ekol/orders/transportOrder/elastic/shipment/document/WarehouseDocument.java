package ekol.orders.transportOrder.elastic.shipment.document;

import ekol.orders.transportOrder.common.domain.IdNamePair;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Created by kilimci on 22/08/2017.
 */
public class WarehouseDocument {

    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String name;

    @Field(type = FieldType.Nested)
    private IdNamePair location;


    public static WarehouseDocument createWith(IdNamePair warehouse, IdNamePair location){
        WarehouseDocument warehouseDocument = new WarehouseDocument();
        warehouseDocument.setName(warehouse.getName());
        warehouseDocument.setId(warehouse.getId());
        warehouseDocument.setLocation(location);
        return warehouseDocument;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IdNamePair getLocation() {
        return location;
    }

    public void setLocation(IdNamePair location) {
        this.location = location;
    }
}
