package ekol.kartoteks.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.StringUtils;

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
public class Email implements Serializable {

    private static final long serialVersionUID = 1905122041950251206L;

    
    @Column(nullable = false, length=200)
    private String emailAddress;

    public Email() {
        // default constructor
    }

    public Email(String emailAddress) {
        this.emailAddress = emailAddress;
    }


	@Override
    public boolean equals(Object other){
        if(other == null || !(other instanceof Email)){
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
        return getEmailAddress();
    }

    public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

}
