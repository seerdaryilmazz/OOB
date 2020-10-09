package ekol.kartoteks.builder;


import ekol.kartoteks.domain.common.Country;

/**
 * Created by kilimci on 14/10/16.
 */
public final class CountryBuilder {
    private Long id;
    private String countryName;
    private String iso;
    private Integer phoneCode;
    private String phoneFormat;
    private String language;
    private String allowedChars;
    private boolean workingWith;
    private String currency;
    private boolean euMember;
    private boolean deleted;

    private CountryBuilder() {
    }

    public static CountryBuilder aCountry() {
        return new CountryBuilder();
    }

    public CountryBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public CountryBuilder withCountryName(String countryName) {
        this.countryName = countryName;
        return this;
    }

    public CountryBuilder withIso(String iso) {
        this.iso = iso;
        return this;
    }

    public CountryBuilder withPhoneCode(Integer phoneCode) {
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

    public CountryBuilder withWorkingWith(boolean workingWith) {
        this.workingWith = workingWith;
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

    public CountryBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public CountryBuilder but() {
        return aCountry().withId(id).withCountryName(countryName).withIso(iso).withPhoneCode(phoneCode).withPhoneFormat(phoneFormat).withLanguage(language).withAllowedChars(allowedChars).withWorkingWith(workingWith).withCurrency(currency).withEuMember(euMember).withDeleted(deleted);
    }

    public Country build() {
        Country country = new Country();
        country.setId(id);
        country.setCountryName(countryName);
        country.setIso(iso);
        country.setPhoneCode(phoneCode);
        country.setPhoneFormat(phoneFormat);
        country.setLanguage(language);
        country.setAllowedChars(allowedChars);
        country.setWorkingWith(workingWith);
        country.setCurrency(currency);
        country.setEuMember(euMember);
        country.setDeleted(deleted);
        return country;
    }
}
