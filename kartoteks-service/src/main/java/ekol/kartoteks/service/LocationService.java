package ekol.kartoteks.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class LocationService {

    @Value("${oneorder.location-service}")
    private String locationServiceName;

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;

    public boolean warehouseExistsByLocationId(Long locationId) {
        return warehouseExistsByLocationIds(Arrays.asList(locationId));
    }

    public boolean warehouseExistsByLocationIds(Iterable<Long> locationIds) {
        String url = locationServiceName + "/location/warehouse/existsbycompanylocation/" + StringUtils.join(locationIds, ",");
        return oAuth2RestTemplate.getForObject(url, Boolean.class);
    }

    public boolean customerWarehouseExistsByLocationId(Long locationId) {
        return customerWarehouseExistsByLocationIds(Arrays.asList(locationId));
    }

    public boolean customerWarehouseExistsByLocationIds(Iterable<Long> locationIds) {
        String url = locationServiceName + "/location/customerwarehouse/existsbycompanylocation/" + StringUtils.join(locationIds, ",");
        return oAuth2RestTemplate.getForObject(url, Boolean.class);
    }
}
