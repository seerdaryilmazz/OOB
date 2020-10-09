package ekol.crm.account.wsclient;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import ekol.crm.account.wsclient.companycrinfo.wsdl.*;
import lombok.Setter;


/**
 * Created by Dogukan Sahinturk on 25.12.2019
 */
@Setter
public class CompanyCrInfoClient extends WebServiceGatewaySupport {
    private boolean enable = true;

    public WSCGETCOMPANYCRINFOPOutput getCompanyCrInfo(Long companyId){

        ObjectFactory objectFactory = new ObjectFactory();
        WSCGETCOMPANYCRINFOPInput request = objectFactory.createWSCGETCOMPANYCRINFOPInput();
        request.setIOOCMPIDNUMBERIN(companyId);
        request.setORECORDSWSCCOMPANYCRRESULTWCOUT(objectFactory.createWSCGETCOMPANYCRINFOPInputORECORDSWSCCOMPANYCRRESULTWCOUT());
        request.setORESULTVARCHAR2OUT(objectFactory.createWSCGETCOMPANYCRINFOPInputORESULTVARCHAR2OUT());

        return (WSCGETCOMPANYCRINFOPOutput) getWebServiceTemplate().marshalSendAndReceive(request, new SoapActionCallback("WSC_GET_COMPANY_CR_INFO_P"));

    }
}
