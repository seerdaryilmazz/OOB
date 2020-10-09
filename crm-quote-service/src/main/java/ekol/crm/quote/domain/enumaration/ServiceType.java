package ekol.crm.quote.domain.enumaration;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;

import ekol.crm.quote.common.Constants;
import lombok.Getter;


@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Deprecated
public enum ServiceType {
    STANDARD("Standard", Constants.SERVICE_TYPE_CATEGORY_MAIN, null, null),

    EXPRESS("Express", Constants.SERVICE_TYPE_CATEGORY_MAIN, "EXPRESS", "ROAD"),
    SUPER_EXPRESS("Super Express",Constants.SERVICE_TYPE_CATEGORY_MAIN, "SUPER_EXPRESS", "ROAD"),
    SPEEDY("Speedy", Constants.SERVICE_TYPE_CATEGORY_MAIN, "SPEEDY", "ROAD"),
    SPEEDY_VAN("Speedy Van", Constants.SERVICE_TYPE_CATEGORY_MAIN, "SPEEDY", "ROAD"),
    SPEEDYXL("Speedyxl", Constants.SERVICE_TYPE_CATEGORY_MAIN, "SPEEDY", "ROAD"),
    ROAD_PALLETIZATION("Palletization", Constants.SERVICE_TYPE_CATEGORY_EXTRA, "ROAD_PALLETIZATION", "ROAD"),
    ATA_CARNET("Ata Carnet", Constants.SERVICE_TYPE_CATEGORY_EXTRA, "ATA_CARNET", "ROAD"),
    CAD("CAD-Cash Against Documents", Constants.SERVICE_TYPE_CATEGORY_EXTRA, "CAD", "ROAD"),
    TRANSIT_TRADE("Transit Trade", Constants.SERVICE_TYPE_CATEGORY_EXTRA, "TRANSIT_SERVICE_COST", "ROAD"),
    HEALTH_CERTIFICATION("Health Certification", Constants.SERVICE_TYPE_CATEGORY_EXTRA, "HEALTH_CERTIFICATION_COST", "ROAD"),
    INSURANCE("Insurance",Constants.SERVICE_TYPE_CATEGORY_EXTRA, "TRANSPORTATION_INSURANCE_COST", "ROAD"),
    ATR("ATR",Constants.SERVICE_TYPE_CATEGORY_EXTRA,"ATR_COST","ROAD"),
    CERTIFICATE_OF_ORIGIN("Certificate of Origin",Constants.SERVICE_TYPE_CATEGORY_EXTRA,"CERTIFICATE_OF_ORIGIN_COST","ROAD"),
   

    SEA_COLLECTION("Collection", Constants.SERVICE_TYPE_CATEGORY_EXTRA, "SEA_COLLECTION", "SEA"),
    STORAGE("Storage", Constants.SERVICE_TYPE_CATEGORY_EXTRA, "STORAGE", "SEA"),
    DEPOSIT_FOR_DEMURRAGE("Deposit For Demurrage", Constants.SERVICE_TYPE_CATEGORY_EXTRA, "DEPOSIT_FOR_DEMURRAGE", "SEA"),
    ENS("ENS", Constants.SERVICE_TYPE_CATEGORY_EXTRA, "ENS", "SEA"),
    MANIFEST_CORRECTION("Manifest Correction", Constants.SERVICE_TYPE_CATEGORY_EXTRA, "MANIFEST_CORRECTION", "SEA"),
    SEA_BILL_OF_LADING("Bill of Lading", Constants.SERVICE_TYPE_CATEGORY_EXTRA, "SEA_BILL_OF_LADING", "SEA"),
    LASHING("Lashing", Constants.SERVICE_TYPE_CATEGORY_EXTRA, "LASHING", "SEA"),
    SEA_MOTO_COURRIER("Moto Courrier", Constants.SERVICE_TYPE_CATEGORY_EXTRA, "SEA_MOTO_COURRIER", "SEA"),
    SEAL("Seal", Constants.SERVICE_TYPE_CATEGORY_EXTRA, "SEAL", "SEA"),
    VGM("VGM", Constants.SERVICE_TYPE_CATEGORY_EXTRA, "VGM", "SEA"),
    SEA_INSURANCE("Insurance", Constants.SERVICE_TYPE_CATEGORY_EXTRA, "SEA_INSURANCE", "SEA"),
    SEA_AGENCY_OF_PROFIT("Agency Profit", Constants.SERVICE_TYPE_CATEGORY_EXTRA, "SEA_AGENCY_OF_PROFIT", "SEA"),
    SEA_LOCAL_COSTS("Local Costs", Constants.SERVICE_TYPE_CATEGORY_EXTRA, "SEA_LOCAL_COSTS", "SEA"),
    SEA_OTHER_SERVICES("Other Services", Constants.SERVICE_TYPE_CATEGORY_EXTRA, "SEA_OTHER_SERVICES", "SEA"),

    AIR_COLLECTION("Collection", Constants.SERVICE_TYPE_CATEGORY_EXTRA, "SEA_COLLECTION", "AIR"),
    AIR_BILL_OF_LADING("Bill of Lading", Constants.SERVICE_TYPE_CATEGORY_EXTRA, "AIR_BILL_OF_LADING", "AIR"),
    AIR_MOTO_COURRIER("Moto Courrier", Constants.SERVICE_TYPE_CATEGORY_EXTRA, "AIR_MOTO_COURRIER", "AIR"),
    AIR_PALLETIZATION("Palletization", Constants.SERVICE_TYPE_CATEGORY_EXTRA, "AIR_PALLETIZATION", "AIR"),
    AIR_INSURANCE("Insurance", Constants.SERVICE_TYPE_CATEGORY_EXTRA, "AIR_INSURANCE", "AIR"),
    AIR_AGENCY_OF_PROFIT("Agency Profit", Constants.SERVICE_TYPE_CATEGORY_EXTRA, "AIR_AGENCY_OF_PROFIT", "AIR"),
    AIR_LOCAL_COSTS("Local Costs", Constants.SERVICE_TYPE_CATEGORY_EXTRA, "AIR_LOCAL_COSTS", "AIR"),
    AIR_OTHER_SERVICES("Other Services", Constants.SERVICE_TYPE_CATEGORY_EXTRA, "AIR_OTHER_SERVICES", "AIR");


    private String id;
    private String code;
    private String name;
    private String category;
    private String billingItemName;
    private String serviceArea;

    private ServiceType(String name, String category, String billingItemName, String serviceArea){
        this.id = name();
        this.code = name();
        this.name = name;
        this.category = category;
        this.billingItemName = billingItemName;
        this.serviceArea = serviceArea;
    }

    @JsonCreator
    public static ServiceType fromNode(JsonNode node) {
        if (!node.has("id"))
            return null;

        String name = node.get("id").asText();

        return ServiceType.valueOf(name);
    }
}

