package ekol.orders.search.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;
import org.springframework.data.elasticsearch.annotations.Setting;

import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import lombok.Data;

@Data
@Document(indexName = ShipmentSearchDocument.INDEX_NAME)
@Setting(settingPath = "/elastic/ShipmentSettings.json")
public class ShipmentSearchDocument {

    public static final String INDEX_NAME = "shipments";

    private String id;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String shipmentCode;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String orderCode;

    @Field(type = FieldType.Long, index = FieldIndex.not_analyzed)
    private Long customerId;

    @MultiField(mainField = @Field(type = FieldType.String, index = FieldIndex.not_analyzed),
            otherFields = {
                    @InnerField(type = FieldType.String, indexAnalyzer = "turkish", searchAnalyzer = "turkish", suffix = "tr")})
    private String customerName;

    @Field(type = FieldType.Long, index = FieldIndex.not_analyzed)
    private Long originalCustomerId;

    @MultiField(mainField = @Field(type = FieldType.String, index = FieldIndex.not_analyzed),
            otherFields = {
                    @InnerField(type = FieldType.String, indexAnalyzer = "turkish", searchAnalyzer = "turkish", suffix = "tr")})
    private String originalCustomerName;

    @Field(type = FieldType.Long)
    private Long orderId;

    @Field(type = FieldType.Nested, index = FieldIndex.not_analyzed)
    private IdNamePair subsidiary;

    @Field(type = FieldType.Nested)
    private DateTimeDocument readyDateOrLoadingAppointment;
    
    @Field(type = FieldType.Nested)
    private DateTimeDocument deliveryDateOrUnloadingAppointment;
    
    @Field(type = FieldType.Nested)
    private DateTimeDocument readyDate;

    @Field(type = FieldType.Nested)
    private DateTimeDocument deliveryDate;
    
    @Field(type = FieldType.Nested)
    private DateTimeWindowDocument loadingAppointment;
    
    @Field(type = FieldType.Nested)
    private DateTimeWindowDocument unloadingAppointment;

    @Field(type = FieldType.Nested)
    private HandlingPartySearchDocument sender;

    @Field(type = FieldType.Nested)
    private HandlingPartySearchDocument consignee;
    
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String status;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String truckLoadType;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String serviceType;
    
    @Field(type = FieldType.Double, index = FieldIndex.not_analyzed)
    private Double grossWeight;

    @Field(type = FieldType.Double, index = FieldIndex.not_analyzed)
    private Double volume;

    @Field(type = FieldType.Double, index = FieldIndex.not_analyzed)
    private Double ldm;

    @Field(type = FieldType.Double, index = FieldIndex.not_analyzed)
    private Double payWeight;

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed)
    private Integer packageCount;

    @Field(type=FieldType.String, index = FieldIndex.not_analyzed)
    private String packageType;

    @Field(type=FieldType.String, index = FieldIndex.not_analyzed)
    private List<String> special = new ArrayList<>();

    @Field(type=FieldType.String, index = FieldIndex.not_analyzed)
    private String templateId;
    
    @Field(type=FieldType.Nested, index = FieldIndex.not_analyzed)
    private List<CodeNamePair> applicationIds;

}
