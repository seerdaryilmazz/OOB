package ekol.crm.quote.domain.dto.validation;

import java.util.*;
import java.util.stream.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import ekol.crm.quote.client.LocationService;
import ekol.crm.quote.domain.dto.PriceJson;
import ekol.crm.quote.domain.dto.locationservice.*;
import ekol.crm.quote.domain.enumaration.*;
import ekol.crm.quote.domain.model.*;
import ekol.crm.quote.domain.model.product.SpotProduct;
import ekol.crm.quote.domain.model.quote.SpotQuote;
import ekol.crm.quote.util.ProductUtils;
import ekol.model.*;
import lombok.*;

@Component
@RequiredArgsConstructor(onConstructor=@__(@Autowired))
public class PriceValidationRequestConverter {

    @NonNull
    private LocationService locationServiceClient;
    
    private static final Map<String, String> VEHICLE_TYPE_MAP = new HashMap<>();
    private static final Map<LoadingType, String> LOADING_TYPE_MAP = new EnumMap<>(LoadingType.class);
    private static final Map<DeliveryType, String> DELIVERY_TYPE_MAP = new EnumMap<>(DeliveryType.class);

    static {
        VEHICLE_TYPE_MAP.put("CURTAIN_SIDER", "CURTAIN_SIDE");
        VEHICLE_TYPE_MAP.put("BOX_BODY", "BOX");
        VEHICLE_TYPE_MAP.put("MEGA", "MEGA");
        VEHICLE_TYPE_MAP.put("SUITABLE_FOR_TRAIN", "");
        VEHICLE_TYPE_MAP.put("XL_CERTIFICATE", "");
        VEHICLE_TYPE_MAP.put("FRIGO", "FRIGO");
        VEHICLE_TYPE_MAP.put("ISOLATED", "");
        VEHICLE_TYPE_MAP.put("DOUBLE_DECK", "DOUBLE_DECK");
        VEHICLE_TYPE_MAP.put("SUITABLE_FOR_HANGING_LOADS", "BOX");
        VEHICLE_TYPE_MAP.put("LIFTING_ROOF", "CURTAIN_SIDE");
        VEHICLE_TYPE_MAP.put("SLIDING_ROOF", "SLIDING_ROOF");
        VEHICLE_TYPE_MAP.put("SECURITY_SENSOR", "SECURITY_SENSOR");
        VEHICLE_TYPE_MAP.put("TAIL_LIFT", "LIFT");
        VEHICLE_TYPE_MAP.put("TRAILER", "");
        VEHICLE_TYPE_MAP.put("CONTAINER", "CONTAINER");
        VEHICLE_TYPE_MAP.put("SWAP_BODY", "");

        LOADING_TYPE_MAP.put(LoadingType.EKOL_CROSS_DOCK, "Ekol Depo");
        LOADING_TYPE_MAP.put(LoadingType.PARTNER_CROSS_DOCK, "Acenta Depo");
        LOADING_TYPE_MAP.put(LoadingType.CUSTOMER_ADDRESS, "Müşteri Adresinden Yükleme");

        DELIVERY_TYPE_MAP.put(DeliveryType.EKOL_CROSS_DOCK, "Müşteri Adresinden Yükleme");
    }


    public PriceValidationRequest convert(SpotQuote quote, List<Price> prices) {

        SpotProduct product = (SpotProduct)quote.getProducts().iterator().next();

        String arrivalCountry = product.getToCountry().getIso();
        String arrivalPostalCode = product.getToPoint().getName();
        if(ProductUtils.isImport(product)) {
        	CustomsPoint customsArrival = quote.getCustoms().getArrival();
        	if ("CUSTOMER_ADDRESS".equals(product.getDeliveryType().getCode())
        			&& !Arrays.asList("CUSTOMS_CLEARANCE_PARK", "FREE_ZONE", "CUSTOMER_CUSTOMS_WAREHOUSE").contains(customsArrival.getClearanceType().getCode())
        			&& Objects.nonNull(customsArrival.getLocation())) {
        		arrivalCountry = Objects.nonNull(customsArrival.getCustomsLocationCountry()) ? customsArrival.getCustomsLocationCountry().getIso() : arrivalCountry;
        		arrivalPostalCode = Objects.nonNull(customsArrival.getCustomsLocationPostal()) ? customsArrival.getCustomsLocationPostal().getName() : arrivalPostalCode;
        	}
        }

        PriceValidationRequest request = new PriceValidationRequest();
        request.setFromCountryCode(product.getFromCountry().getIso());
        request.setFromPostalCode(product.getFromPoint().getName());
        request.setToCountryCode(arrivalCountry);
        request.setToPostalCode(arrivalPostalCode);
        request.setTruckLoadType(product.getShipmentLoadingType());
        request.setLoadingDate(product.getEarliestReadyDate());
        request.setLoadingType(LOADING_TYPE_MAP.get(product.getLoadingType()));
        request.setDeliveryType(convertDeliveryType(quote.getCustoms(), product));
        request.setVehicleType(convertVehicleType(quote.getVehicleRequirements()));
        request.setWeight(Optional.ofNullable(quote.getMeasurement()).map(Measurement::getWeight).orElse(null));
        request.setLdm(Optional.ofNullable(quote.getMeasurement()).map(Measurement::getLdm).orElse(null));
        request.setVolume(Optional.ofNullable(quote.getMeasurement()).map(Measurement::getVolume).orElse(null));
        request.setPayWeight(quote.getPayWeight());
        request.setPrices(Optional.ofNullable(prices).orElseGet(Collections::emptyList).stream().map(PriceJson::fromEntity).collect(Collectors.toList()));

        return request;
    }
    

    private String convertDeliveryType(Customs customs, SpotProduct product) {
        Optional<CustomsClearanceType> arrivalClearanceType = Optional.ofNullable(customs).map(Customs::getArrival).map(CustomsPoint::getClearanceType);
        if(arrivalClearanceType.isPresent() && arrivalClearanceType.get() == CustomsClearanceType.EKOL_BOUNDED_WAREHOUSE){
            return "Ekol Antrepo";
        }
        return Optional.ofNullable(product.getDeliveryLocation())
        		.map(IdNamePair::getId)
        		.map(locationServiceClient::getWarehouseByCompanyLocationId)
        		.map(WarehouseJson::getCustomsDetails)
        		.map(CustomsDetailsJson::getCustomsType)
        		.map(CodeNamePair::getCode)
        		.filter(t->Stream.of(DeliveryType.values()).map(Enum::name).anyMatch(t::equals))
        		.map(DeliveryType::valueOf)
        		.map(DELIVERY_TYPE_MAP::get)
        		.orElse(null);
    }

    private String convertVehicleType(Set<VehicleRequirement> vehicleRequirements) {
        List<String> vehicleTypes = Optional.ofNullable(vehicleRequirements)
                .orElseGet(Collections::emptySet).stream()
                .filter(vehicleRequirement -> vehicleRequirement.getOperationType().getCode().equals("COLLECTION"))
                .map(VehicleRequirement::getRequirement).map(CodeNamePair::getCode).collect(Collectors.toList());

        if(!CollectionUtils.isEmpty(vehicleTypes)){
            if(vehicleTypes.size() == 1){
                return VEHICLE_TYPE_MAP.get(vehicleTypes.get(0));
            }else{
                Set<String> mappedVehicleTypes = new HashSet<>();
                int i = 0;
                for (String vehicleType : vehicleTypes) {
                    if(VEHICLE_TYPE_MAP.containsKey(vehicleType)){
                        mappedVehicleTypes.add(VEHICLE_TYPE_MAP.get(vehicleType));
                    }else{
                        mappedVehicleTypes.add(String.valueOf(i++));
                    }
                }
                if(mappedVehicleTypes.size() == 2){
                    if(mappedVehicleTypes.contains("MEGA") && mappedVehicleTypes.contains("BOX")){
                        return "MEGA_BOX";
                    }
                    if(mappedVehicleTypes.contains("MEGA") && mappedVehicleTypes.contains("CURTAIN_SIDE")){
                        return "MEGA_CURTAIN_SIDE";
                    }
                    if(mappedVehicleTypes.contains("FRIGO") && mappedVehicleTypes.contains("CONTAINER")){
                        return "FRIGO_CONTAINER";
                    }
                    if(mappedVehicleTypes.contains("FRIGO") && mappedVehicleTypes.contains("DOUBLE_DECK")){
                        return "FRIGO_DOUBLE_DECK";
                    }
                }
            }
        }
        return null;
    }
}
