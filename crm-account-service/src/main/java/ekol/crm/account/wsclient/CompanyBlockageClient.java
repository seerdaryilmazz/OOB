package ekol.crm.account.wsclient;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import ekol.crm.account.wsclient.companyblockage.wsdl.ObjectFactory;
import ekol.crm.account.wsclient.companyblockage.wsdl.WSCGETCOMPANYBLOCKAGEInput;
import ekol.crm.account.wsclient.companyblockage.wsdl.WSCGETCOMPANYBLOCKAGEOutput;
import lombok.Setter;

@Setter
public class CompanyBlockageClient extends WebServiceGatewaySupport   {
	
	private boolean enable = true;

	public String getCompanyBlockage(Long companyId){
		
		if(!enable) {
			return StringUtils.EMPTY;
		}
		
		ObjectFactory objectFactory = new ObjectFactory();
		WSCGETCOMPANYBLOCKAGEInput request = objectFactory.createWSCGETCOMPANYBLOCKAGEInput();
		request.setICOMPANYIDVARCHAR2IN(String.valueOf(companyId));
		request.setOBLOCKAGECODEVARCHAR2OUT(new ObjectFactory().createWSCGETCOMPANYBLOCKAGEInputOBLOCKAGECODEVARCHAR2OUT());
		
		WSCGETCOMPANYBLOCKAGEOutput response = (WSCGETCOMPANYBLOCKAGEOutput)getWebServiceTemplate().marshalSendAndReceive(request, new SoapActionCallback("WSC_GET_COMPANY_BLOCKAGE"));
		
		return response.getOBLOCKAGECODE();
	}
}
