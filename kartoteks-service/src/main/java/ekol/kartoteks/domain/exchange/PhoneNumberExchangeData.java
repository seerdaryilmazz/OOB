package ekol.kartoteks.domain.exchange;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.kartoteks.domain.PhoneNumberWithType;

/**
 * Created by kilimci on 05/05/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PhoneNumberExchangeData {

    private String usageType;
    private String numberType;
    private String countryCode;
    private String regionCode;
    private String phone;
    private String extension;
    private boolean isDefault;

    public static PhoneNumberExchangeData fromPhoneNumber(PhoneNumberWithType phoneNumber){
        PhoneNumberExchangeData exchangeData = new PhoneNumberExchangeData();
        exchangeData.setNumberType(phoneNumber.getNumberType() != null ? phoneNumber.getNumberType().getCode() : null);
        exchangeData.setUsageType(phoneNumber.getUsageType() != null ? phoneNumber.getUsageType().getCode() : null);
        exchangeData.setCountryCode(phoneNumber.getPhoneNumber().getCountryCode());
        exchangeData.setRegionCode(phoneNumber.getPhoneNumber().getRegionCode());
        exchangeData.setPhone(phoneNumber.getPhoneNumber().getPhone());
        exchangeData.setExtension(phoneNumber.getPhoneNumber().getExtension());
        exchangeData.setDefault(phoneNumber.isDefault());
        return exchangeData;
    }

    public String getUsageType() {
        return usageType;
    }

    public void setUsageType(String usageType) {
        this.usageType = usageType;
    }

    public String getNumberType() {
        return numberType;
    }

    public void setNumberType(String numberType) {
        this.numberType = numberType;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }




}
