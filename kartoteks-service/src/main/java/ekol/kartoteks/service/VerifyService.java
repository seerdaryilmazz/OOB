package ekol.kartoteks.service;

import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import ekol.kartoteks.domain.Company;
import ekol.kartoteks.domain.dto.EoriVerifyServiceResponse;
import ekol.kartoteks.domain.dto.TaxofficeVerifyServiceResponse;
import ekol.kartoteks.repository.CompanyRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class VerifyService {

    private Logger logger = LoggerFactory.getLogger(VerifyService.class);

    @Value("${oneorder.taxoffice-verify-service}")
    private String taxofficeVerifyServiceName;

    @Value("${oneorder.eori-verify-service}")
    private String eoriVerifyServiceName;

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;

    @Autowired
    private CompanyRepository companyRepository;

    /**
     * @param bestEffortVerification True olduğunda, sorgulama sonucu başarısız ise ve hatanın sebebi yanlış/eksik bilgi değilse
     *                               (yani diğer hatalar ise; mesela timeout hatası) vergi numarasının doğru olduğunu kabul ediyoruz.
     *                               False olduğunda, sorgulama sonucu başarısız ise hatanın sebebine bakmadan vergi numarasının yanlış olduğunu kabul ediyoruz.
     */
    public void verifyTaxId(Long companyId, boolean bestEffortVerification) {

        Company company = companyRepository.findById(companyId);
        String countryIso = company.getCountry().getIso();

        if (company == null) {
            throw new ResourceNotFoundException("Company not found.");
        }
        if (countryIso.equals("TR") && company.getTaxOffice() == null) {
            throw new BadRequestException("Tax office is empty.");
        }
        if (StringUtils.isBlank(company.getTaxId())) {
            throw new BadRequestException("Tax id is empty.");
        }

        List<String> params = new ArrayList<>();
        params.add("country=" + countryIso);
        if (countryIso.equals("TR")) {
            params.add("taxoffice=" + company.getTaxOffice().getCode());
        }
        params.add("taxid=" + company.getTaxId());

        String url = taxofficeVerifyServiceName + "/verify?" + StringUtils.join(params, "&");

        TaxofficeVerifyServiceResponse response = oAuth2RestTemplate.getForObject(url, TaxofficeVerifyServiceResponse.class);

        if (response.getStatus().equals("error")) {
            boolean throwException = true;
            if (!response.getErrorCausedByInvalidInput() && !countryIso.equals("TR") && bestEffortVerification) {
                logger.info("Tax id verification failed but tax id is accepted as verified because of bestEffortVerification setting. companyId: {}", companyId);
                throwException = false;
            }
            if (throwException) {
                throw new BadRequestException("Tax id verification failed. Message: {0}", Objects.toString(response.getErrorMessage(), ""));
            }
        }
    }

    public void verifyEoriNumber(Long companyId) {

        Company company = companyRepository.findById(companyId);

        if (company == null) {
            throw new ResourceNotFoundException("Company not found.");
        }
        if (StringUtils.isBlank(company.getEoriNumber())) {
            throw new BadRequestException("EORI number is empty.");
        }

        String url = eoriVerifyServiceName + "/verify?eori=" + company.getEoriNumber();

        EoriVerifyServiceResponse response = oAuth2RestTemplate.getForObject(url, EoriVerifyServiceResponse.class);

        if (response == null || !response.getStatus().equals("success")) {
            throw new BadRequestException("EORI number verification failed. Message: {0}", Objects.toString(response.getMessage(), ""));
        }
    }

}
