/**
 * MNBArfolyamServiceSoap_GetCurrentExchangeRates_StringFault_FaultMessage.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.6  Built on : Jul 30, 2017 (09:08:31 BST)
 */
package ekol.currency.webserviceclient.exchangerate.hungary;

public class MNBArfolyamServiceSoap_GetCurrentExchangeRates_StringFault_FaultMessage
    extends Exception {
    private static final long serialVersionUID = 1510434709104L;
    private ekol.currency.webserviceclient.exchangerate.hungary.MNBArfolyamServiceSoapImplStub.String faultMessage;

    public MNBArfolyamServiceSoap_GetCurrentExchangeRates_StringFault_FaultMessage() {
        super(
            "MNBArfolyamServiceSoap_GetCurrentExchangeRates_StringFault_FaultMessage");
    }

    public MNBArfolyamServiceSoap_GetCurrentExchangeRates_StringFault_FaultMessage(
        String s) {
        super(s);
    }

    public MNBArfolyamServiceSoap_GetCurrentExchangeRates_StringFault_FaultMessage(
        String s, Throwable ex) {
        super(s, ex);
    }

    public MNBArfolyamServiceSoap_GetCurrentExchangeRates_StringFault_FaultMessage(
        Throwable cause) {
        super(cause);
    }

    public void setFaultMessage(
        ekol.currency.webserviceclient.exchangerate.hungary.MNBArfolyamServiceSoapImplStub.String msg) {
        faultMessage = msg;
    }

    public ekol.currency.webserviceclient.exchangerate.hungary.MNBArfolyamServiceSoapImplStub.String getFaultMessage() {
        return faultMessage;
    }
}
