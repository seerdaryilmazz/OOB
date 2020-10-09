package ekol.location.domain.location.comnon;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Embeddable
@Access(AccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailWithType implements Serializable {

    @Column
    private String email;

    @Column(length = 20)
    private String usageType;

    @Column
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean isDefault;

    @Override
    public String toString(){
        StringBuilder emailWithType = new StringBuilder(32);

        if(usageType != null){
            if(emailWithType.length() > 0){
                emailWithType.append(".");
            }
            emailWithType.append(usageType);
        }
        if(email != null){
            if(emailWithType.length() > 0){
                emailWithType.append(".");
            }
            emailWithType.append(email);
        }
        return emailWithType.toString();
    }
    @Override
    public boolean equals(Object other){
        if(other == null || !(other instanceof EmailWithType)){
            return false;
        }
        return toString().equals(other.toString());
    }
    @Override
    public int hashCode(){
        return toString().hashCode();
    }

    public void copyTypesIfEmailsEqual(EmailWithType emailData){
        if(getEmail().equals(emailData.getEmail())){
            setDefault(emailData.isDefault());
            if(emailData.getUsageType() != null){
                setUsageType(emailData.getUsageType());
            }
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsageType() {
        return usageType;
    }

    public void setUsageType(String usageType) {
        this.usageType = usageType;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
