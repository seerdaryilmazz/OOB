package ekol.crm.account.service;

import ekol.crm.account.domain.dto.CustomPageImpl;
import ekol.crm.account.domain.dto.kartoteksservice.Company;
import ekol.crm.account.domain.dto.kartoteksservice.CompanyLocation;
import ekol.crm.account.util.Utils;
import ekol.model.CodeNamePair;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CompanyService {

    @Value("${oneorder.kartoteks-service}")
    private String kartoteksServiceName;

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;

    public Company findById(Long id, boolean ignoreNotFoundException) {
        return Utils.getForObject(ignoreNotFoundException, oAuth2RestTemplate, kartoteksServiceName + "/company/" + id, Company.class);
    }

    public CustomPageImpl<Long> searchCompanyIdsByCityAndDistrict(String city, String district, boolean searchOnlyInDefaultLocations, int size, int page) {

        List<String> requestParams = new ArrayList<>();
        requestParams.add("city=" + Objects.toString(city, ""));
        requestParams.add("district=" + Objects.toString(district, ""));
        requestParams.add("searchOnlyInDefaultLocations=" + searchOnlyInDefaultLocations);
        requestParams.add("size=" + size);
        requestParams.add("page=" + page);

        String url = kartoteksServiceName + "/search/by-city-and-district?" + StringUtils.join(requestParams, "&");

        ResponseEntity<CustomPageImpl<Long>> response = oAuth2RestTemplate.exchange(
                url, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<CustomPageImpl<Long>>() {});

        return response.getBody();
    }

    public CodeNamePair findServiceAreaByCode(String code, boolean ignoreNotFoundException){
        String url = kartoteksServiceName + "/business-segment-type/code/" + code;
        return Utils.getForObject(ignoreNotFoundException, oAuth2RestTemplate, url, CodeNamePair.class);
    }

    public CompanyLocation findCompanyLocationByMappedApplication(String applicationName, String applicationId, boolean ignoreNotFoundException){
        String url = kartoteksServiceName + "/location/mapped-application?applicationName={applicationName}&applicationId={applicationId}";
        return Utils.getForObject(ignoreNotFoundException, oAuth2RestTemplate, url, CompanyLocation.class, applicationName, applicationId);
    }
}
