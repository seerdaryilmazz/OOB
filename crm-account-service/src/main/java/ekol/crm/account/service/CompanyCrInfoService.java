package ekol.crm.account.service;

import java.util.*;
import java.util.stream.*;

import ekol.crm.account.domain.dto.kartoteksservice.CompanyLocation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ekol.crm.account.domain.dto.CompanyCrInfo;
import ekol.crm.account.wsclient.CompanyCrInfoClient;
import ekol.crm.account.wsclient.companycrinfo.wsdl.*;
import ekol.crm.account.wsclient.companycrinfo.wsdl.WSCCOMPANYCRRESULTWType.WSCCOMPANYCRRESULTW;
import ekol.crm.account.wsclient.companycrinfo.wsdl.WSCCOMPANYCRRESULTWType.WSCCOMPANYCRRESULTW.RECORDS;
import lombok.AllArgsConstructor;

/**
 * Created by Dogukan Sahinturk on 26.12.2019
 */
@Service
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class CompanyCrInfoService {
	
    private CompanyCrInfoClient companyCrInfoClient;
    private CompanyService companyService;

    public List<CompanyCrInfo> getCompanyCrInfo(Long companyId) {
        return Optional.of(companyId)
            .map(companyCrInfoClient::getCompanyCrInfo)
            .map(WSCGETCOMPANYCRINFOPOutput::getORECORDS)
            .map(WSCCOMPANYCRRESULTWType::getWSCCOMPANYCRRESULTW)
            .map(WSCCOMPANYCRRESULTW::getRECORDS)
            .map(RECORDS::getCOMPANYCRINFORS)
            .map(Collection::stream)
            .orElseGet(Stream::empty)
            .map(this::mapCompanyCrInfo)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
    
    private CompanyCrInfo mapCompanyCrInfo(COMPANYCRINFORSIntType cr) {
        String location = this.getLocation(cr.getQUADROCOMPANYCODE());
        if(StringUtils.isNotBlank(location)){
            CompanyCrInfo crInfo = new CompanyCrInfo();
            crInfo.setDefaultAccountable(cr.getDEFAULTACCOUNTABLE());
            crInfo.setDisplayName(cr.getUSERNAME());
            crInfo.setUsername(StringUtils.lowerCase(cr.getUSERCODE()));
            crInfo.setEmail(cr.getEMAIL());
            crInfo.setPhone(cr.getPHONE01());
            crInfo.setWorkArea(Optional.ofNullable(cr.getWORKAREA()).map(t->StringUtils.split(t, ";")).map(Stream::of).orElseGet(Stream::empty).map(StringUtils::trim).collect(Collectors.toSet()));
            crInfo.setServiceGroupCode(Optional.ofNullable(cr.getSERVICEGROUPCODE()).map(t->StringUtils.split(t, ";")).map(Stream::of).orElseGet(Stream::empty).map(StringUtils::trim).collect(Collectors.toSet()));
            crInfo.setStatus(cr.getSTATUS());
            crInfo.setQuadroCompanyCode(cr.getQUADROCOMPANYCODE());
            crInfo.setLocation(location);
            return crInfo;
        }else {
            return null;
        }
    }

    private String getLocation(String quadroCompanyCode){
        CompanyLocation companyLocation = companyService.findCompanyLocationByMappedApplication("QUADRO", quadroCompanyCode, false);
        if(Objects.nonNull(companyLocation)){
            List<String> location = new ArrayList<>();
            if(StringUtils.isNotBlank(companyLocation.getPostaladdress().getDistrict())){
                location.add(companyLocation.getPostaladdress().getDistrict());
            }
            if(StringUtils.isNotBlank(companyLocation.getPostaladdress().getCity())){
                location.add(companyLocation.getPostaladdress().getCity());
            }
            location.add(companyLocation.getPostaladdress().getCountry().getCountryName());
            return companyLocation.getName() + " ("+ companyLocation.getPostaladdress().getPostalCode()+" - "+ StringUtils.join(location, "/") + ")";
        }else {
            return null;
        }
    }
}
