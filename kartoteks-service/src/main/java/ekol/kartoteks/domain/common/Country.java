package ekol.kartoteks.domain.common;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Locale;

/**
 * Created by fatmaozyildirim on 3/17/16.
 */

@Entity
@Table(name="Country",uniqueConstraints ={@UniqueConstraint(columnNames = {"name"})})
@SequenceGenerator(name="SEQ_COUNTRY", sequenceName = "SEQ_COUNTRY")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "deletedAt", "lastUpdated", "lastUpdatedBy"})
@Where(clause = "deleted = 0")
public class Country extends BaseEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_COUNTRY")
    private Long id;
    @Column(name="name",nullable=false,length = 100)
    private String countryName;
    @Column(nullable=false,length = 2)
    private String iso;
    @Column(nullable = false)
    private Integer phoneCode;
    @Column(nullable = false, length = 100)
    private String phoneFormat;
    @Column(length = 2, nullable = false)
    private String language;
    @Column(length = 100)
    private String allowedChars;
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(nullable = false)
    private boolean workingWith;
    @Column(nullable = false, length = 3)
    private String currency;
    @Column(nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean euMember;

    @JsonIgnore
    public Locale getLocale(){
        return new Locale(language, iso);
    }

    @Override
    public boolean equals(Object other){
        if(other == null || !(other instanceof Country)){
            return false;
        }
        return getIso().equals(((Country) other).getIso());
    }

    @Override
    public int hashCode(){
        return getIso().hashCode();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public Integer getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(Integer phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getPhoneFormat() {
        return phoneFormat;
    }

    public void setPhoneFormat(String phoneFormat) {
        this.phoneFormat = phoneFormat;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAllowedChars() {
        return allowedChars;
    }

    public void setAllowedChars(String allowedChars) {
        this.allowedChars = allowedChars;
    }

    public boolean isWorkingWith() {
        return workingWith;
    }

    public void setWorkingWith(boolean workingWith) {
        this.workingWith = workingWith;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isEuMember() {
        return euMember;
    }

    public void setEuMember(boolean euMember) {
        this.euMember = euMember;
    }
}
