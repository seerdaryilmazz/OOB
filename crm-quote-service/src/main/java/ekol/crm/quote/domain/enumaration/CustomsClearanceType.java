package ekol.crm.quote.domain.enumaration;

import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.*;

import ekol.crm.quote.common.Constants;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum CustomsClearanceType {
    EKOL_BOUNDED_WAREHOUSE(
    		"Ekol Bonded Warehouse",
    		"EKOL_WAREHOUSE", 
    		Constants.BONDED_WAREHOUSE, 
    		null, 
    		ImmutableMap.of(Constants.IMPORT, Sets.newHashSet(Constants.ARRIVAL)),
    		Constants.WAREHOUSE, 
    		false, 
    		null),
    EXTERNAL_BOUNDED_WAREHOUSE(
    		"External Bonded Warehouse",
    		"PARTNER_WAREHOUSE", 
    		Constants.BONDED_WAREHOUSE, 
    		"COMPANY", 
    		ImmutableMap.of(Constants.IMPORT, Sets.newHashSet(Constants.ARRIVAL)), 
    		Constants.WAREHOUSE, 
    		false, 
    		Constants.BILLING_ITEM_DELIVERY_COST_DOT),
    CUSTOMER_CUSTOMS_WAREHOUSE(
    		"Customer Customs Warehouse",
    		null, 
    		"CUSTOMER_CUSTOMS_WAREHOUSE", 
    		"COMPANY", 
    		ImmutableMap.of(Constants.IMPORT, Sets.newHashSet(Constants.ARRIVAL)), 
    		Constants.WAREHOUSE, 
    		false, 
    		Constants.BILLING_ITEM_DELIVERY_COST_DOT),
    CUSTOMS_WAREHOUSE(
    		"Customs Warehouse",
    		null, 
    		"CUSTOMS_WAREHOUSE", 
    		null, 
    		ImmutableMap.of(Constants.IMPORT, Sets.newHashSet(Constants.ARRIVAL)), 
    		Constants.WAREHOUSE, 
    		false, 
    		Constants.BILLING_ITEM_DELIVERY_COST_DOT),
    CUSTOMS_CLEARANCE_PARK(
    		"Customs Clearance Park",
    		null, 
    		"CUSTOMS_CLEARANCE_PARK", 
    		null, 
    		ImmutableMap.of(Constants.IMPORT, Sets.newHashSet(Constants.ARRIVAL)),
    		Constants.WAREHOUSE, 
    		false, 
    		Constants.BILLING_ITEM_DELIVERY_COST_DOT),

    AT_COUNTRY_BORDER_CUSTOMS(
    		"At Country Border Customs",
    		null, 
    		Constants.EUROPE_CUSTOMS_LOCATION, 
    		null, 
    		ImmutableMap.of(
    				Constants.IMPORT, 		Sets.newHashSet(Constants.DEPARTURE),
    				Constants.EXPORT, 		Sets.newHashSet(Constants.ARRIVAL),
    				Constants.NON_TURKEY, 	Sets.newHashSet(Constants.DEPARTURE,Constants.ARRIVAL)),
    		Constants.WAREHOUSE, 
    		true, 
    		null),
    WHERE_CUSTOMER_PREFERRED(
    		"Where Customer Preferred",
    		null, 
    		Constants.EUROPE_CUSTOMS_LOCATION, 
    		null, 
    		ImmutableMap.of(
    				Constants.IMPORT, 		Sets.newHashSet(Constants.DEPARTURE),
    				Constants.EXPORT, 		Sets.newHashSet(Constants.ARRIVAL),
    				Constants.NON_TURKEY, 	Sets.newHashSet(Constants.DEPARTURE,Constants.ARRIVAL)),
    		Constants.WAREHOUSE, 
    		true, 
    		null),
    AT_DELIVERY_LOCATION(
    		"At Delivery Location",
    		null, 
    		Constants.EUROPE_CUSTOMS_LOCATION, 
    		null, 
    		ImmutableMap.of(
    				Constants.EXPORT, 		Sets.newHashSet(Constants.ARRIVAL),
    				Constants.NON_TURKEY, 	Sets.newHashSet(Constants.ARRIVAL)),
    		null, 
    		true, 
    		null),
    AT_LOADING_LOCATION(
    		"At Loading Location",
    		null, 
    		Constants.EUROPE_CUSTOMS_LOCATION, 
    		null, 
    		ImmutableMap.of(
    				Constants.IMPORT, 		Sets.newHashSet(Constants.DEPARTURE),
    				Constants.NON_TURKEY, 	Sets.newHashSet(Constants.DEPARTURE)),
    		null, 
    		true, 
    		null),
    WHERE_EKOL_PREFERRED(
    		"Where Ekol Preferred",
    		null, 
    		Constants.EUROPE_CUSTOMS_LOCATION, 
    		null, 
    		ImmutableMap.of(
    				Constants.IMPORT, 		Sets.newHashSet(Constants.DEPARTURE),
    				Constants.EXPORT, 		Sets.newHashSet(Constants.ARRIVAL),
    				Constants.NON_TURKEY, 	Sets.newHashSet(Constants.DEPARTURE,Constants.ARRIVAL)),
    		Constants.WAREHOUSE, 
    		true, 
    		null),

    STANDARD(
    		"Standard",
    		null, 
    		null, 
    		null, 
    		ImmutableMap.of(Constants.EXPORT, Sets.newHashSet(Constants.DEPARTURE)), 
    		"NON-FREEZONE", 
    		false, 
    		null),
    RETURN_TO_COUNTRY_Of_ORIGINS(
    		"Return To Country of Origins",
    		null, 
    		null, 
    		null, 
    		ImmutableMap.of(
    				Constants.IMPORT, Sets.newHashSet(Constants.DEPARTURE),
    				Constants.EXPORT, Sets.newHashSet(Constants.DEPARTURE)),
    		"FREEZONE,NON_FREEZONE", 
    		false, 
    		"RCO"),
    FREE_ZONE(
    		"Free Zone",
    		null, 
    		null, 
    		null, 
    		ImmutableMap.of(
    				Constants.IMPORT, Sets.newHashSet(Constants.ARRIVAL),
    				Constants.EXPORT, Sets.newHashSet(Constants.DEPARTURE)),
    		"FREEZONE", 
    		false, 
    		"FREE_ZONE");

    private String id;
    private String code;
    private String name;
    private String ownerType;
    private String customsType;
    private String companyType;
    private Map<String, Set<String>> scope;
    private String source;
    private boolean onlyLocation;
    private String billingItemName;

    CustomsClearanceType(String name,
    					 String ownerType,
                         String customsType,
                         String companyType,
                         Map<String, Set<String>> scope,
                         String source,
                         boolean onlyLocation,
                         String billingItemName){
        this.id = this.code = name();
        this.name = name;
        this.ownerType = ownerType;
        this.customsType = customsType;
        this.companyType = companyType;
        this.scope = scope;
        this.source = source;
        this.onlyLocation = onlyLocation;
        this.billingItemName = billingItemName;
    }

    @JsonCreator
    public static CustomsClearanceType fromNode(JsonNode node) {
        if (!node.has("id"))
            return null;

        String name = node.get("id").asText();

        return CustomsClearanceType.valueOf(name);
    }
}
