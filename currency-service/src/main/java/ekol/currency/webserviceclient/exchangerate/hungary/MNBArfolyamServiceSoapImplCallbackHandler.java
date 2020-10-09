/**
 * MNBArfolyamServiceSoapImplCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.6  Built on : Jul 30, 2017 (09:08:31 BST)
 */
package ekol.currency.webserviceclient.exchangerate.hungary;


/**
 *  MNBArfolyamServiceSoapImplCallbackHandler Callback class, Users can extend this class and implement
 *  their own receiveResult and receiveError methods.
 */
public abstract class MNBArfolyamServiceSoapImplCallbackHandler {
    protected Object clientData;

    /**
     * User can pass in any object that needs to be accessed once the NonBlocking
     * Web service call is finished and appropriate method of this CallBack is called.
     * @param clientData Object mechanism by which the user can pass in user data
     * that will be avilable at the time this callback is called.
     */
    public MNBArfolyamServiceSoapImplCallbackHandler(Object clientData) {
        this.clientData = clientData;
    }

    /**
     * Please use this constructor if you don't want to set any clientData
     */
    public MNBArfolyamServiceSoapImplCallbackHandler() {
        this.clientData = null;
    }

    /**
     * Get the client data
     */
    public Object getClientData() {
        return clientData;
    }

    /**
     * auto generated Axis2 call back method for getExchangeRates method
     * override this method for handling normal response from getExchangeRates operation
     */
    public void receiveResultgetExchangeRates(
        ekol.currency.webserviceclient.exchangerate.hungary.MNBArfolyamServiceSoapImplStub.GetExchangeRatesResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getExchangeRates operation
     */
    public void receiveErrorgetExchangeRates(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getDateInterval method
     * override this method for handling normal response from getDateInterval operation
     */
    public void receiveResultgetDateInterval(
        ekol.currency.webserviceclient.exchangerate.hungary.MNBArfolyamServiceSoapImplStub.GetDateIntervalResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getDateInterval operation
     */
    public void receiveErrorgetDateInterval(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getCurrencies method
     * override this method for handling normal response from getCurrencies operation
     */
    public void receiveResultgetCurrencies(
        ekol.currency.webserviceclient.exchangerate.hungary.MNBArfolyamServiceSoapImplStub.GetCurrenciesResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getCurrencies operation
     */
    public void receiveErrorgetCurrencies(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getCurrencyUnits method
     * override this method for handling normal response from getCurrencyUnits operation
     */
    public void receiveResultgetCurrencyUnits(
        ekol.currency.webserviceclient.exchangerate.hungary.MNBArfolyamServiceSoapImplStub.GetCurrencyUnitsResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getCurrencyUnits operation
     */
    public void receiveErrorgetCurrencyUnits(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getCurrentExchangeRates method
     * override this method for handling normal response from getCurrentExchangeRates operation
     */
    public void receiveResultgetCurrentExchangeRates(
        ekol.currency.webserviceclient.exchangerate.hungary.MNBArfolyamServiceSoapImplStub.GetCurrentExchangeRatesResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getCurrentExchangeRates operation
     */
    public void receiveErrorgetCurrentExchangeRates(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getInfo method
     * override this method for handling normal response from getInfo operation
     */
    public void receiveResultgetInfo(
        ekol.currency.webserviceclient.exchangerate.hungary.MNBArfolyamServiceSoapImplStub.GetInfoResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getInfo operation
     */
    public void receiveErrorgetInfo(Exception e) {
    }
}
