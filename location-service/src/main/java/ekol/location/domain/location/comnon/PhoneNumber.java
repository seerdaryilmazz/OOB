package ekol.location.domain.location.comnon;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by fatmaozyildirim on 3/14/16.
 */
@Embeddable
@Access(AccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PhoneNumber implements Serializable{

    private static final long serialVersionUID = 1905122041950251205L;

    @Column(length = 5)
    private String countryCode;

    @Column(length = 5)
    private String regionCode;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(length = 6)
    private String extension;

    public PhoneNumber() {
        //default constructor
    }

    public PhoneNumber(String countryCode, String regionCode, String phone, String extension) {
        this.countryCode = countryCode;
        this.regionCode = regionCode;
        this.phone = phone;
        this.extension = extension;
    }
    @Override
    public boolean equals(Object other){
        if(other == null || !(other instanceof PhoneNumber)){
            return false;
        }
        return toString().equals(other.toString());
    }

    @Override
    public int hashCode(){
        return toString().hashCode();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder(16);
        if(getCountryCode() != null){
            sb.append("+").append(getCountryCode());
        }
        if(getRegionCode() != null){
            sb.append("(").append(getRegionCode()).append(")");
        }
        if(getPhone() != null){
            if(getPhone().length() > 5){
                sb.append(getPhone().substring(0,3)).append(" ").append(getPhone().substring(3));
            }else{
                sb.append(getPhone());
            }
        }
        if(getExtension() != null){
            sb.append("-").append(getExtension());
        }
        return sb.toString();
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
