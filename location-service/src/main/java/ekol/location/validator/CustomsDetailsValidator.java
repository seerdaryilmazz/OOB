package ekol.location.validator;

import ekol.exceptions.ValidationException;
import ekol.location.domain.WarehouseCustomsType;
import ekol.location.domain.location.comnon.CustomsDetails;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class CustomsDetailsValidator {

    private static void validateCustomsOffice(CustomsDetails customsDetails){
        if(customsDetails.getCustomsOffice() == null){
            throw new ValidationException("Customs Details should have a customs office");
        }
    }
    public static void validate(CustomsDetails customsDetails, String countryCode) {
        if(customsDetails == null){
            return;
        }
        if(customsDetails.getCustomsType() == null){
            throw new ValidationException("Customs Details should have a type");
        }

        if(customsDetails.getCustomsType().equals(WarehouseCustomsType.BONDED_WAREHOUSE)){
            validateCustomsOffice(customsDetails);
            Pattern warehouseCodePattern = Pattern.compile("^A[0-9]{8}$");
            if(StringUtils.isNotBlank(customsDetails.getWarehouseCode()) &&
                    !warehouseCodePattern.matcher(customsDetails.getWarehouseCode()).matches()){
                throw new ValidationException("Customs warehouse code is not valid");
            }
            if(StringUtils.isNotBlank(customsDetails.getDangerousGoodsCode()) &&
                    !warehouseCodePattern.matcher(customsDetails.getDangerousGoodsCode()).matches()){
                throw new ValidationException("Customs dangerous goods warehouse code is not valid");
            }
            if(StringUtils.isNotBlank(customsDetails.getTempControlledGoodsCode()) &&
                    !warehouseCodePattern.matcher(customsDetails.getTempControlledGoodsCode()).matches()){
                throw new ValidationException("Customs temperature controlled goods warehouse code is not valid");
            }
        }
        if(customsDetails.getCustomsType().equals(WarehouseCustomsType.CUSTOMS_WAREHOUSE)){
            validateCustomsOffice(customsDetails);
            Pattern warehouseCodePattern = Pattern.compile("^G[0-9]{8}$");
            if(StringUtils.isNotBlank(customsDetails.getWarehouseCode()) &&
                    !warehouseCodePattern.matcher(customsDetails.getWarehouseCode()).matches()){
                throw new ValidationException("Customs warehouse code is not valid");
            }
        }
        if(customsDetails.getCustomsType().equals(WarehouseCustomsType.CUSTOMER_CUSTOMS_WAREHOUSE)){
            validateCustomsOffice(customsDetails);
            Pattern warehouseCodePattern = Pattern.compile("^C[0-9]{8}$");
            if(StringUtils.isNotBlank(customsDetails.getWarehouseCode()) &&
                    !warehouseCodePattern.matcher(customsDetails.getWarehouseCode()).matches()){
                throw new ValidationException("Customs warehouse code is not valid");
            }
        }
        if(customsDetails.getCustomsType().equals(WarehouseCustomsType.EUROPE_CUSTOMS_LOCATION)){
            if(StringUtils.isBlank(customsDetails.getEuropeanCustomsCode())){
                throw new ValidationException("Customs Details should have a customs code");
            }
            if(!customsDetails.getEuropeanCustomsCode().startsWith(countryCode)){
                throw new ValidationException("Customs code should start with country code {0}", countryCode);
            }
        }
    }
}
