package ekol.orders.transportOrder.service;

import ekol.exceptions.ResourceNotFoundException;
import ekol.orders.transportOrder.domain.TradeType;
import ekol.orders.transportOrder.dto.TradeTypeModal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by burak on 10/08/16.
 */
@Service
public class TradeTypeService {

    private static final String COUNTRY_TURKEY = "TURKEY";

    private static final String ERROR_ORIGINLOCATION_NOTFOUND = "Origin Location with given id is not found";
    private static final String ERROR_DESTINATIONLOCATION_NOTFOUND = "Destination Location with given id is not found";

    private static final String ERROR_ORIGINPOSTALADDRESS_NOTEXIST = "Requested Origin Location does not have postal address information";
    private static final String ERROR_DESTINATIONPOSTALADDRESS_NOTEXIST = "Requested Destination Location does not have postal address information";


    private static final String ERROR_ORIGINCOUNTRY_NOTEXIST = "Requested Origin Location does not have country information";
    private static final String ERROR_DESTINATIONCOUNTRY_NOTEXIST = "Requested Destination Location does not have country information";


    @Value("${oneorder.kartoteks-service}")
    private String kartoteksServiceName;

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;

    public TradeType retrieveTradeType(String originLocationId, String destinationLocationId) {

        TradeTypeModal originModal = getLocation(originLocationId);
        TradeTypeModal destinationModal = getLocation(destinationLocationId);

        checkOrigin(originModal);
        checkDestination(destinationModal);

        TradeTypeModal.TradeTypeCountry originPostalCountry = originModal.getPostaladdress().getCountry();
        TradeTypeModal.TradeTypeCountry destinationPostalCountry = destinationModal.getPostaladdress().getCountry();

        String originCountryName = originPostalCountry.getCountryName();
        boolean isOriginEUMember = originPostalCountry.isEuMember();

        String destCountryName = destinationPostalCountry.getCountryName();
        boolean isDestEUMember = destinationPostalCountry.isEuMember();

        TradeType tradeType = TradeType.OTHER;

        if (originCountryName.equalsIgnoreCase(COUNTRY_TURKEY) || destCountryName.equalsIgnoreCase(COUNTRY_TURKEY)) {
            if (originCountryName.equalsIgnoreCase(destCountryName)) {
                tradeType = TradeType.NONE;
            } else {
                if (originCountryName.equalsIgnoreCase(COUNTRY_TURKEY)) {
                    tradeType = TradeType.EXPORT;
                } else {
                    tradeType = TradeType.IMPORT;
                }
            }
        } else if (isOriginEUMember && isDestEUMember) {
            tradeType = TradeType.EU;
        }

        return tradeType;
    }

    private TradeTypeModal getLocation(String locationId) {
        return oAuth2RestTemplate.getForObject(kartoteksServiceName + "/location/" + locationId, TradeTypeModal.class);
    }

    private static void checkOrigin(TradeTypeModal originModal) {
        if (originModal == null) {
            throw new ResourceNotFoundException(ERROR_ORIGINLOCATION_NOTFOUND);
        }
        if (originModal.getPostaladdress() == null) {
            throw new ResourceNotFoundException(ERROR_ORIGINPOSTALADDRESS_NOTEXIST);
        }
        if (originModal.getPostaladdress().getCountry() == null) {
            throw new ResourceNotFoundException(ERROR_ORIGINCOUNTRY_NOTEXIST);
        }
    }

    private static void checkDestination(TradeTypeModal destinationModal) {
        if (destinationModal == null) {
            throw new ResourceNotFoundException(ERROR_DESTINATIONLOCATION_NOTFOUND);
        }
        if (destinationModal.getPostaladdress() == null) {
            throw new ResourceNotFoundException(ERROR_DESTINATIONPOSTALADDRESS_NOTEXIST);
        }
        if (destinationModal.getPostaladdress().getCountry() == null) {
            throw new ResourceNotFoundException(ERROR_DESTINATIONCOUNTRY_NOTEXIST);
        }
    }

}
