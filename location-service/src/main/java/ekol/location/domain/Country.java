package ekol.location.domain;

import java.math.BigDecimal;

import javax.persistence.*;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.hibernate5.domain.entity.BaseEntity;

/**
 * Created by ozer on 13/12/16.
 */
@Entity
@Table(name = "lcountry")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Country extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_lcountry", sequenceName = "seq_lcountry")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_lcountry")
    private Long id;

    @Column
    private String name;

    @Column
    private String iso;

    @Column
    private Long phoneCode;

    @Column
    private String phoneFormat;

    @Column
    private String language;

    @Column
    private String allowedChars;

    @Column
    private String currency;

    @Column
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean euMember;

    @Column
    private String isoAlpha3Code;

    @Column
    private String timezone;

    /**
     * Bu alan şimdilik ülkeler harita üzerinde gösterilirken haritayı ortalamak için kullanılıyor.
     */
    @Column
    private BigDecimal centerLat;

    /**
     * Bu alan şimdilik ülkeler harita üzerinde gösterilirken haritayı ortalamak için kullanılıyor.
     */
    @Column
    private BigDecimal centerLng;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public Long getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(Long phoneCode) {
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

    public String getIsoAlpha3Code() {
        return isoAlpha3Code;
    }

    public void setIsoAlpha3Code(String isoAlpha3Code) {
        this.isoAlpha3Code = isoAlpha3Code;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public BigDecimal getCenterLat() {
        return centerLat;
    }

    public void setCenterLat(BigDecimal centerLat) {
        this.centerLat = centerLat;
    }

    public BigDecimal getCenterLng() {
        return centerLng;
    }

    public void setCenterLng(BigDecimal centerLng) {
        this.centerLng = centerLng;
    }
}
