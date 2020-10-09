package ekol.location.domain.location.comnon;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by fatmaozyildirim on 3/21/16.
 */
@Embeddable
@Access(AccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PhoneNumberWithType implements Serializable{

    private static final long serialVersionUID = 1L;

    @Embedded
    private PhoneNumber phoneNumber;

    @Column(length = 20)
    private String phoneType;

    @Column
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean isDefault;

    public PhoneNumberWithType() {
        //Default Constructor
    }

    @Override
    public String toString(){
        StringBuilder phoneNumberWithType = new StringBuilder(32);
        if(phoneType != null){
            if(phoneNumberWithType.length() > 0){
                phoneNumberWithType.append(".");
            }
            phoneNumberWithType.append(phoneType);
        }
        if(phoneNumber != null){
            if(phoneNumberWithType.length() > 0){
                phoneNumberWithType.append(".");
            }
            phoneNumberWithType.append(phoneNumber.toString());
        }
        return phoneNumberWithType.toString();
    }
    @Override
    public boolean equals(Object other){
        if(other == null || !(other instanceof PhoneNumberWithType)){
            return false;
        }
        return toString().equals(other.toString());
    }
    @Override
    public int hashCode(){
        return toString().hashCode();
    }



    public void copyTypesIfNumbersEqual(PhoneNumberWithType phoneNumberData){
        if(getPhoneNumber().equals(phoneNumberData.getPhoneNumber())){
            setDefault(phoneNumberData.isDefault());
            if(phoneNumberData.getPhoneType() != null){
                setPhoneType(phoneNumberData.getPhoneType());
            }
        }

    }
    @JsonIgnore
    public String getPhoneNumberWithoutCodes() {
        return phoneNumber != null ? phoneNumber.getPhone() : "";
    }
    @JsonIgnore
    public String getPhoneRegionCode() {
        return phoneNumber != null ? phoneNumber.getRegionCode() : "";
    }
    @JsonIgnore
    public String getPhoneCountryCode() {
        return phoneNumber != null ? phoneNumber.getCountryCode() : "";
    }

    public void setPhoneNumberWithoutCodes(String number) {
        if(phoneNumber == null){
            phoneNumber = new PhoneNumber();
        }
        phoneNumber.setPhone(number);
    }
    public void setPhoneNumberRegionCode(String regionCode) {
        if(phoneNumber == null){
            phoneNumber = new PhoneNumber();
        }
        phoneNumber.setRegionCode(regionCode);
    }
    public void setPhoneNumberCountryCode(String countryCode) {
        if(phoneNumber == null){
            phoneNumber = new PhoneNumber();
        }
        phoneNumber.setCountryCode(countryCode);
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }
}