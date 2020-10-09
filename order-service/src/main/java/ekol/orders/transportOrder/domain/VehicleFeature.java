package ekol.orders.transportOrder.domain;


import ekol.exceptions.ValidationException;
import ekol.json.serializers.common.ConverterType;
import ekol.orders.transportOrder.dto.VehicleFeatureFilter;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;
import org.apache.commons.lang.StringUtils;

import javax.persistence.AttributeConverter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum VehicleFeature {

    /**
     * TODO: Bu değerler, Crm'deki tekliflerde de kullanılıyor. Özellikle isim değişikliği yapıldığında Crm tarafında etkilenen yerler de güncellenmeli.
     * TODO: Bu değerler, Crm'deki değerler ve sales-price-service'teki değerler (CrmTrailerType) ortaklaştırılsa iyi olur.
     */

    CURTAIN_SIDER,
    BOX_BODY,
    MEGA,
    SUITABLE_FOR_TRAIN,
    XL_CERTIFICATE,
    FRIGO,
    ISOLATED,
    DOUBLE_DECK,
    SUITABLE_FOR_HANGING_LOADS,
    LIFTING_ROOF,
    SLIDING_ROOF,
    SECURITY_SENSOR,
    TAIL_LIFT,
    TRAILER,
    CONTAINER,
    SWAP_BODY;

    private static List<VehicleFeature> boxFeatures = Arrays.asList(BOX_BODY, FRIGO, ISOLATED, SUITABLE_FOR_HANGING_LOADS);
    private static List<VehicleFeature> curtainSideFeatures = Arrays.asList(CURTAIN_SIDER, LIFTING_ROOF, SLIDING_ROOF);
    private static List<VehicleFeature> trailerTypes = Arrays.asList(TRAILER);
    private static List<VehicleFeature> containerTypes = Arrays.asList(CONTAINER, SWAP_BODY);

    public static void validateCooccurence(List<VehicleFeature> featureList) {

        List<VehicleFeature> requiredBoxFeatures = featureList.stream()
                .filter(vehicleFeature -> boxFeatures.contains(vehicleFeature)).collect(toList());
        List<VehicleFeature> requiredCurtainSideFeatures = featureList.stream()
                .filter(vehicleFeature -> curtainSideFeatures.contains(vehicleFeature)).collect(toList());

        if(!requiredBoxFeatures.isEmpty() && !requiredCurtainSideFeatures.isEmpty()){
            throw new ValidationException("Following features can not exist together: " + requiredCurtainSideFeatures.get(0).name() + " - " + requiredBoxFeatures.get(0).name());
        }

        List<VehicleFeature> requiredTrailerTypes = featureList.stream()
                .filter(vehicleFeature -> trailerTypes.contains(vehicleFeature)).collect(toList());
        List<VehicleFeature> requiredContainerTypes = featureList.stream()
                .filter(vehicleFeature -> containerTypes.contains(vehicleFeature)).collect(toList());
        if(!requiredTrailerTypes.isEmpty() && !requiredContainerTypes.isEmpty()){
            throw new ValidationException("Following features can not exist together: " + requiredTrailerTypes.get(0).name() + " - " + requiredContainerTypes.get(0).name());
        }
    }

    public static void validateCooccurence(List<VehicleFeature> required, List<VehicleFeature> notAllowed) {

        //error: when any feature exist in both required and not allowed
        required.stream().filter(feature -> notAllowed.contains(feature)).findFirst().ifPresent(vehicleFeature -> {
            throw new ValidationException("Following feature exist in both required and not allowed filters: " + vehicleFeature.name());
        });

        validateCooccurence(required);
        validateCooccurence(notAllowed);

        List<VehicleFeature> requiredBoxFeatures = required.stream()
                .filter(vehicleFeature -> boxFeatures.contains(vehicleFeature)).collect(toList());
        List<VehicleFeature> requiredCurtainSideFeatures = required.stream()
                .filter(vehicleFeature -> curtainSideFeatures.contains(vehicleFeature)).collect(toList());

        if(notAllowed.contains(BOX_BODY) && !requiredBoxFeatures.isEmpty()){
            throw new ValidationException("There shouldn't be a required box feature when 'BOX' is not allowed");
        }
        if(notAllowed.contains(CURTAIN_SIDER) && !requiredCurtainSideFeatures.isEmpty()){
            throw new ValidationException("There shouldn't be a required curtain side feature when 'CURTAIN SIDE' is not allowed");
        }

    }

    public static class DBConverter implements AttributeConverter<Set<VehicleFeature>, String> {
        @Override
        public String convertToDatabaseColumn(Set<VehicleFeature> list) {
            return String.join(",", list.stream().map(elem -> elem.name()).collect(toList()));
        }

        @Override
        public Set<VehicleFeature> convertToEntityAttribute(String enumString) {
            if(StringUtils.isEmpty(enumString)) {
                return new HashSet<>();
            }
            return Arrays.stream(enumString.split(",")).map(elem -> VehicleFeature.valueOf(elem)).collect(Collectors.toSet());
        }

    }

    public static List<VehicleFeature> fromVehicleFeatureFilter(VehicleFeatureFilter vehicleFeatureFilter) {
        List<VehicleFeature> features = new ArrayList<>();

        if(vehicleFeatureFilter.isCurtainSider()) { features.add(VehicleFeature.CURTAIN_SIDER);}
        if(vehicleFeatureFilter.isBox()) { features.add(VehicleFeature.BOX_BODY);}
        if(vehicleFeatureFilter.isMega()) { features.add(VehicleFeature.MEGA);}
        if(vehicleFeatureFilter.isDoubleDeck()) { features.add(VehicleFeature.DOUBLE_DECK);}
        if(vehicleFeatureFilter.isFrigoTrailer()) { features.add(VehicleFeature.FRIGO);}
        if(vehicleFeatureFilter.isIsolated()) { features.add(VehicleFeature.ISOLATED);}
        if(vehicleFeatureFilter.isLiftingRoof()) { features.add(VehicleFeature.LIFTING_ROOF);}
        if(vehicleFeatureFilter.isSecuritySensor()) { features.add(VehicleFeature.SECURITY_SENSOR);}
        if(vehicleFeatureFilter.isSlidingRoof()) { features.add(VehicleFeature.SLIDING_ROOF);}
        if(vehicleFeatureFilter.isSuitableForHangingLoads()) { features.add(VehicleFeature.SUITABLE_FOR_HANGING_LOADS);}
        if(vehicleFeatureFilter.isSuitableForTrain()) { features.add(VehicleFeature.SUITABLE_FOR_TRAIN);}
        if(vehicleFeatureFilter.isTailLift()) { features.add(VehicleFeature.TAIL_LIFT);}
        if(vehicleFeatureFilter.isXlCertificate()) { features.add(VehicleFeature.XL_CERTIFICATE);}


        return features;
    }

}

