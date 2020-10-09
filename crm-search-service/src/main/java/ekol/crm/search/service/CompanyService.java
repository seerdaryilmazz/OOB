package ekol.crm.search.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import ekol.crm.search.domain.dto.CustomPageImpl;

@Service
public class CompanyService {

    @Value("${oneorder.kartoteks-service}")
    private String kartoteksServiceName;

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;

    public CustomPageImpl<Long> searchCompanyIdsByCityAndDistrict(String city, String district, boolean searchOnlyInDefaultLocations, int size, int page) {

        List<String> requestParams = new ArrayList<>();
        requestParams.add("city={city}");
        requestParams.add("district={district}");
        requestParams.add("searchOnlyInDefaultLocations={searchOnlyInDefaultLocations}");
        requestParams.add("size={size}");
        requestParams.add("page={page}");

        String url = kartoteksServiceName + "/search/by-city-and-district?" + StringUtils.join(requestParams, "&");

        ResponseEntity<CustomPageImpl<Long>> response = oAuth2RestTemplate.exchange(
                url, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<CustomPageImpl<Long>>() {}, 
                city, district, searchOnlyInDefaultLocations, size, page);

        return response.getBody();
    }
}
