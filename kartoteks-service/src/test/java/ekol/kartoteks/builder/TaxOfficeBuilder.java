package ekol.kartoteks.builder;

import ekol.kartoteks.domain.TaxOffice;

/**
 * Created by kilimci on 17/10/16.
 */
public final class TaxOfficeBuilder {
    private Long id;
    private String name;
    private String code;
    private String cityCode;
    private String countryCode;
    private boolean deleted;

    private TaxOfficeBuilder() {
    }

    public static TaxOfficeBuilder aTaxOffice() {
        return new TaxOfficeBuilder();
    }

    public TaxOfficeBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public TaxOfficeBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TaxOfficeBuilder withCode(String code) {
        this.code = code;
        return this;
    }

    public TaxOfficeBuilder withCityCode(String cityCode) {
        this.cityCode = cityCode;
        return this;
    }

    public TaxOfficeBuilder withCountryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public TaxOfficeBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public TaxOfficeBuilder but() {
        return aTaxOffice().withId(id).withName(name).withCode(code).withCityCode(cityCode).withCountryCode(countryCode).withDeleted(deleted);
    }

    public TaxOffice build() {
        TaxOffice taxOffice = new TaxOffice();
        taxOffice.setId(id);
        taxOffice.setName(name);
        taxOffice.setCode(code);
        taxOffice.setCityCode(cityCode);
        taxOffice.setCountryCode(countryCode);
        taxOffice.setDeleted(deleted);
        return taxOffice;
    }
}
