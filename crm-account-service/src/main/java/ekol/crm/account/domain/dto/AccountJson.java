package ekol.crm.account.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.account.domain.dto.potential.PotentialJson;
import ekol.crm.account.domain.enumaration.AccountType;
import ekol.crm.account.domain.enumaration.SegmentType;
import ekol.crm.account.domain.model.Account;
import ekol.crm.account.domain.model.CodeNamePair;
import ekol.crm.account.domain.model.IdNamePair;
import ekol.crm.account.domain.model.IsoNamePair;
import ekol.exceptions.ValidationException;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@JsonIgnoreProperties(value = {"newAccount"}, ignoreUnknown = true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor


public class AccountJson {

    private Long id;
    private String name;
    private IdNamePair company;
    private IsoNamePair country;
    private String accountOwner;
    private AccountType accountType;
    private SegmentType segment;
    private CodeNamePair parentSector;
    private CodeNamePair subSector;
    private AccountDetailJson details;
    private UtcDateTime createdAt;
    private String createdBy;
    private String lastUpdatedBy;
    private UtcDateTime lastUpdated;
    private List<PotentialJson> potentials = new ArrayList<>();
    private List<ContactJson> contacts = new ArrayList<>();

    public Account toEntity(){
        return Account.builder()
                .id(getId())
                .name(getName())
                .company(getCompany())
                .country(getCountry())
                .accountOwner(getAccountOwner())
                .accountType(getAccountType())
                .segment(getSegment())
                .parentSector(getParentSector())
                .subSector(getSubSector())
                .createdBy(getCreatedBy())
                .createdAt(getId() == null ? new UtcDateTime(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime()) : getCreatedAt())
                .details(Optional.ofNullable(getDetails()).map(AccountDetailJson::toEntity).orElse(null))
                .build();
    }

    public static AccountJson fromEntity(Account account){
        return new AccountJsonBuilder()
                .id(account.getId())
                .name(account.getName())
                .company(account.getCompany())
                .country(account.getCountry())
                .accountOwner(account.getAccountOwner())
                .accountType(account.getAccountType())
                .segment(account.getSegment())
                .parentSector(account.getParentSector())
                .subSector(account.getSubSector())
                .details(Optional.ofNullable(account.getDetails()).map(AccountDetailJson::fromEntity).orElse(null))
                .createdBy(account.getCreatedBy())
                .createdAt(account.getCreatedAt())
                .lastUpdatedBy(account.getLastUpdatedBy())
                .lastUpdated(account.getLastUpdated()).build();
    }

    public void validate(){
        if(StringUtils.isBlank(getName())){
            throw new ValidationException("Account should have a name");
        }
        if(getCompany() == null || getCompany().getId() == null){
            throw new ValidationException("Company should be assigned to an account");
        }
        if(StringUtils.isBlank(getCompany().getName())){
            throw new ValidationException("Account should have a company name");
        }
        if(StringUtils.isBlank(getAccountOwner())){
            throw new ValidationException("Account should have a account owner");
        }
        if(getAccountType() == null){
            throw new ValidationException("Account should have a type");
        }
        if(getSegment() == null){
            throw new ValidationException("Account should have a segment");
        }
        if(getParentSector() == null || StringUtils.isEmpty(getParentSector().getCode())){
            throw new ValidationException("Account should have a parent sector");
        }
        if(getSubSector() == null || StringUtils.isEmpty(getSubSector().getCode())){
            throw new ValidationException("Account should have a sub sector");
        }
        if(getCountry() == null || StringUtils.isBlank(getCountry().getIso())){
            throw new ValidationException("Account should have a company's country");
        }
        if(getDetails()!=null){
            if(getDetails().getGlobal()!=null && getDetails().getGlobal() && getDetails().getGlobalAccountId()!=null){
                throw new ValidationException("Account can not be global and have a global account at the same time");
            }
            if(getDetails().getGlobal()!=null && !getDetails().getGlobal() && getDetails().getGlobalAccountOwner()!=null){
                throw new ValidationException("Account can not have global account owner when it is not global");
            }
        }

    }
}
