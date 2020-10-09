package ekol.crm.account.domain.dto;

import java.util.Set;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.account.domain.enumaration.*;
import ekol.crm.account.domain.model.Account;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountChangeJson {
	private Set<Long> accountIds;

	// changeable fields
    private AccountType accountType;
    private String accountOwner;
    private SegmentType segmentType;


    public boolean fieldToChangeExists(){
        return !StringUtils.isEmpty(this.accountOwner) ||
                !StringUtils.isEmpty(this.segmentType) ||
                !StringUtils.isEmpty(this.accountType);
    }

    public void apply(Account account){
        if(!StringUtils.isEmpty(this.accountOwner)){
            account.setAccountOwner(this.accountOwner);
        }
        if(!StringUtils.isEmpty(this.segmentType)){
            account.setSegment(this.segmentType);
        }
        if(!StringUtils.isEmpty(this.accountType)){
            account.setAccountType(this.accountType);
        }
    }
}
