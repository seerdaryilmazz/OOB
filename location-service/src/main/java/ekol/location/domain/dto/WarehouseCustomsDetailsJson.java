package ekol.location.domain.dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.location.domain.LocationType;
import ekol.location.domain.WarehouseCompanyType;
import ekol.location.domain.WarehouseCustomsType;
import ekol.location.domain.location.comnon.CustomsDetails;
import ekol.location.domain.location.comnon.ExternalSystemId;
import ekol.location.domain.location.comnon.IdNameEmbeddable;
import ekol.location.domain.location.customerwarehouse.CustomerWarehouse;
import ekol.location.domain.location.warehouse.Warehouse;
import ekol.model.IdNamePair;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WarehouseCustomsDetailsJson {

    private Long id;
    private String name;
    private LocationType locationType;
    private WarehouseCustomsType customsType;
    private IdNamePair customsOffice;
    private String europeanCustomsCode;
    private String warehouseCode;
    private boolean usedForDangerousGoods;
    private boolean usedForTempControlledGoods;
    private IdNamePair companyLocation;
    private IdNamePair company;
    private Collection<ExternalSystemId> externalIds;
    private WarehouseCompanyType companyType;

    public static WarehouseCustomsDetailsJson with(Long id, String name, LocationType type, CustomsDetails customsDetails, IdNameEmbeddable company, IdNameEmbeddable companyLocation, Collection<ExternalSystemId> externalSystemIds, WarehouseCompanyType companyType ){
        WarehouseCustomsDetailsJson json = new WarehouseCustomsDetailsJson();
        json.setId(id);
        json.setName(name);
        json.setLocationType(type);
        json.setCompanyType(companyType);
        Optional.ofNullable(company).ifPresent(t->json.setCompany(new IdNamePair(t.getId(),t.getName())));
        Optional.ofNullable(companyLocation).ifPresent(t->json.setCompanyLocation(new IdNamePair(t.getId(),t.getName())));
        Optional.ofNullable(externalSystemIds).ifPresent(t->json.setExternalIds(new HashSet<>(t)));
        if(customsDetails != null){
            json.setEuropeanCustomsCode(customsDetails.getEuropeanCustomsCode());
            json.setCustomsType(customsDetails.getCustomsType());
            if(customsDetails.getCustomsOffice() != null){
                json.setCustomsOffice(new IdNamePair(customsDetails.getCustomsOffice().getId(), customsDetails.getCustomsOffice().getName()));
            }
            json.setWarehouseCode(customsDetails.getWarehouseCode());
            json.setUsedForDangerousGoods(customsDetails.isUsedForDangerousGoods());
            json.setUsedForTempControlledGoods(customsDetails.isUsedForTempControlledGoods());
        }
        return json;
    }

    public static WarehouseCustomsDetailsJson with(Warehouse warehouse){
    	return WarehouseCustomsDetailsJson.with(warehouse.getId(), warehouse.getName(),
    			warehouse.getType(), warehouse.getCustomsDetails(), warehouse.getCompany(), warehouse.getCompanyLocation(), warehouse.getExternalIds(), null);
    }
    
    public static WarehouseCustomsDetailsJson with(CustomerWarehouse customerWarehouse){
        return WarehouseCustomsDetailsJson.with(customerWarehouse.getId(), customerWarehouse.getName(),
                customerWarehouse.getType(), customerWarehouse.getCustomsDetails(), customerWarehouse.getCompany(), customerWarehouse.getCompanyLocation(), customerWarehouse.getExternalIds(), customerWarehouse.getCompanyType());
    }
}
