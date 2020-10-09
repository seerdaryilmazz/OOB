package ekol.agreement.queue.wscbfunitprice.client;

import ekol.agreement.queue.domain.dto.AgreementJson;
import ekol.agreement.queue.wscbfunitprice.wsdl.*;
import ekol.agreement.queue.wscbfunitprice.wsdl.ObjectFactory;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

@Setter
public class RainbowClient extends WebServiceGatewaySupport   {

	private String gatewayUrl;

	private boolean enable = true;

	private static final Logger LOGGER = LoggerFactory.getLogger(RainbowClient.class);

	public WSCBFUNITPRICEInput setRainbowRequest(AgreementJson agreement){

		ObjectFactory objectFactory = new ObjectFactory();
		WSCBFUNITPRICEInput request = objectFactory.createWSCBFUNITPRICEInput();

		WSCBFUNITPRICEHTYPEType inputType = objectFactory.createWSCBFUNITPRICEHTYPEType();

		WSCBFUNITPRICEHTYPEType.WSCBFUNITPRICEHTYPE contract = objectFactory.createWSCBFUNITPRICEHTYPETypeWSCBFUNITPRICEHTYPE();
		contract.setACCOUNTNAME(agreement.getAccount().getName());
		contract.setCONTRACTID(agreement.getId());
		contract.setCONTRACTNAME(agreement.getName());
		contract.setCONTRACTNUMBER(agreement.getNumber());
		contract.setCONTRACTURL(gatewayUrl + "/ui/crm/agreement/view/" + String.valueOf(agreement.getId()));
		contract.setSTATUS(agreement.getStatus().getCode());

		WSCBFUNITPRICEHTYPEType.WSCBFUNITPRICEHTYPE.LINE unitPriceLine = objectFactory.createWSCBFUNITPRICEHTYPETypeWSCBFUNITPRICEHTYPELINE();
		Optional.of(agreement).map(AgreementJson::getUnitPrices).map(Collection::stream).orElseGet(Stream::empty).forEach(unitPrice->{
			WSCBFUNITPRICELTYPEIntType unitPriceType = objectFactory.createWSCBFUNITPRICELTYPEIntType();
			unitPriceType.setBILLINGITEMCODE(Double.valueOf(unitPrice.getBillingItem().getCode()));
			unitPriceType.setCURRENCY(unitPrice.getCurrency());
			unitPriceType.setID(unitPrice.getId());
			unitPriceType.setPRICE(unitPrice.getPrice().doubleValue());
			unitPriceType.setSERVICENAME(unitPrice.getServiceName());
			unitPriceType.setBASEDON(unitPrice.getBasedOn().getCode());
			unitPriceType.setSTARTDATE(dateConverter(unitPrice.getValidityStartDate()));
			unitPriceType.setENDDATE(dateConverter(unitPrice.getValidityEndDate()));
			unitPriceLine.getWSCBFUNITPRICELTYPE().add(unitPriceType);
		});
		contract.setLINE(unitPriceLine);
		inputType.setWSCBFUNITPRICEHTYPE(contract);
		request.setIHEADERWSCBFUNITPRICEHTYPECIN(inputType);
		request.setORESULTVARCHAR2OUT(objectFactory.createWSCBFUNITPRICEInputORESULTVARCHAR2OUT());
		return request;
	}

	private String dateConverter(LocalDate date){
		return Optional.ofNullable(date).map(DateTimeFormatter.ofPattern("ddMMyyyy")::format).orElse(null);
	}

	public WSCBFUNITPRICEOutput sendToRainbow(WSCBFUNITPRICEInput input){
		if(input != null){
			return (WSCBFUNITPRICEOutput)getWebServiceTemplate().marshalSendAndReceive(input, new SoapActionCallback("WSC_BF_UNIT_PRICE"));
		}
		return null;
	}
}
