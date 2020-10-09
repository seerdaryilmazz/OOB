package ekol.location.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.location.domain.Country;

import java.math.BigDecimal;

/**
 * Created by ozer on 02/02/2017.
 */
public final class CountryBuilder {
    private Long id;
    private String name;
    private String iso;
    private Long phoneCode;
    private String phoneFormat;
    private String language;
    private String allowedChars;
    private String currency;
    private boolean euMember;
    private String isoAlpha3Code;
    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;
    private String timezone;
    private BigDecimal centerLat;
    private BigDecimal centerLng;

    private CountryBuilder() {
    }

    public static CountryBuilder acountry() {
        return new CountryBuilder();
    }

    public CountryBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public CountryBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CountryBuilder withIso(String iso) {
        this.iso = iso;
        return this;
    }

    public CountryBuilder withPhoneCode(Long phoneCode) {
        this.phoneCode = phoneCode;
        return this;
    }

    public CountryBuilder withPhoneFormat(String phoneFormat) {
        this.phoneFormat = phoneFormat;
        return this;
    }

    public CountryBuilder withLanguage(String language) {
        this.language = language;
        return this;
    }

    public CountryBuilder withAllowedChars(String allowedChars) {
        this.allowedChars = allowedChars;
        return this;
    }

    public CountryBuilder withCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public CountryBuilder withEuMember(boolean euMember) {
        this.euMember = euMember;
        return this;
    }

    public CountryBuilder withIsoAlpha3Code(String isoAlpha3Code) {
        this.isoAlpha3Code = isoAlpha3Code;
        return this;
    }

    public CountryBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public CountryBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public CountryBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public CountryBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public CountryBuilder withTimezone(String timezone) {
        this.timezone = timezone;
        return this;
    }

    public CountryBuilder withCenterLat(BigDecimal centerLat) {
        this.centerLat = centerLat;
        return this;
    }

    public CountryBuilder withCenterLng(BigDecimal centerLng) {
        this.centerLng = centerLng;
        return this;
    }

    public Country build() {
        Country country = new Country();
        country.setId(id);
        country.setName(name);
        country.setIso(iso);
        country.setPhoneCode(phoneCode);
        country.setPhoneFormat(phoneFormat);
        country.setLanguage(language);
        country.setAllowedChars(allowedChars);
        country.setCurrency(currency);
        country.setEuMember(euMember);
        country.setIsoAlpha3Code(isoAlpha3Code);
        country.setDeleted(deleted);
        country.setDeletedAt(deletedAt);
        country.setLastUpdated(lastUpdated);
        country.setLastUpdatedBy(lastUpdatedBy);
        country.setTimezone(timezone);
        country.setCenterLat(centerLat);
        country.setCenterLng(centerLng);
        return country;
    }
}
