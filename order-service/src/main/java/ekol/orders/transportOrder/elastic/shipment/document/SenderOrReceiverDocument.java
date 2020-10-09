package ekol.orders.transportOrder.elastic.shipment.document;


import ekol.orders.transportOrder.domain.SenderOrReceiver;
import ekol.orders.transportOrder.elastic.shipment.model.LocationType;
import org.springframework.data.elasticsearch.annotations.*;

/**
 * Created by ozer on 03/10/16.
 */
public class SenderOrReceiverDocument {

    @MultiField(
            mainField = @Field(type = FieldType.String, index = FieldIndex.not_analyzed),
            otherFields = {
                    @InnerField(type = FieldType.String, indexAnalyzer = "turkish", searchAnalyzer = "turkish", suffix = "tr")
            }
    )
    private String companyName;

    @MultiField(
            mainField = @Field(type = FieldType.String, index = FieldIndex.not_analyzed),
            otherFields = {
                    @InnerField(type = FieldType.String, indexAnalyzer = "turkish", searchAnalyzer = "turkish", suffix = "tr")
            }
    )
    private String locationOwnerCompanyName;

    @Field(type = FieldType.Nested)
    private LocationDocument location;

    @Field(type = FieldType.Long)
    private Long regionId;

    @Field(type = FieldType.String)
    private String regionCategoryId;

    @Field(type = FieldType.Long)
    private Long operationRegionId;

    public static SenderOrReceiverDocument fromSenderOrReceiver(SenderOrReceiver senderOrReceiver){
        SenderOrReceiverDocument document = new SenderOrReceiverDocument();
        if (senderOrReceiver != null) {
            document.setCompanyName(senderOrReceiver.getCompany().getName());
            document.setLocationOwnerCompanyName(senderOrReceiver.getLocationOwnerCompany().getName());
            document.setLocation(LocationDocument.fromLocation(senderOrReceiver.getLocation(), LocationType.CUSTOMER));
            document.setRegionId(senderOrReceiver.getLocationRegionId());
            document.setRegionCategoryId(senderOrReceiver.getLocationRegionCategoryId());
            document.setOperationRegionId(senderOrReceiver.getLocationOperationRegionId());
        }
        return document;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocationOwnerCompanyName() {
        return locationOwnerCompanyName;
    }

    public void setLocationOwnerCompanyName(String locationOwnerCompanyName) {
        this.locationOwnerCompanyName = locationOwnerCompanyName;
    }

    public LocationDocument getLocation() {
        return location;
    }

    public void setLocation(LocationDocument location) {
        this.location = location;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public String getRegionCategoryId() {
        return regionCategoryId;
    }

    public void setRegionCategoryId(String regionCategoryId) {
        this.regionCategoryId = regionCategoryId;
    }

    public Long getOperationRegionId() {
        return operationRegionId;
    }

    public void setOperationRegionId(Long operationRegionId) {
        this.operationRegionId = operationRegionId;
    }
}