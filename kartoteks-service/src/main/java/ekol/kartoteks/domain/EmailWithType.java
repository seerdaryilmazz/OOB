package ekol.kartoteks.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.Type;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by kilimci on 06/04/16.
 */
@Embeddable
@Access(AccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailWithType implements Serializable {
    private static final long serialVersionUID = 3L;

    @Embedded
    private Email email;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "usage_type_id")
    @NotAudited
    private UsageType usageType;

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
            emailWithType.append(usageType.getCode());
        }
        if(email != null){
            if(emailWithType.length() > 0){
                emailWithType.append(".");
            }
            emailWithType.append(email.toString());
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

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public UsageType getUsageType() {
        return usageType;
    }

    public void setUsageType(UsageType usageType) {
        this.usageType = usageType;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
