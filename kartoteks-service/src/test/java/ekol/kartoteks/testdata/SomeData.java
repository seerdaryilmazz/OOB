package ekol.kartoteks.testdata;

import ekol.kartoteks.builder.*;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * Created by kilimci on 18/10/16.
 */
public class SomeData {
    public static CountryBuilder someCountry() {
        return CountryBuilder.aCountry().withCountryName("test").withCurrency("TRY").withPhoneFormat("###").withLanguage("EL")
                .withDeleted(false).withEuMember(false).withIso("TE").withPhoneCode(90);
    }

    public static CompanyTypeBuilder someCompanyType() {
        return CompanyTypeBuilder.aCompanyType().withCode(RandomStringUtils.randomAlphabetic(3)).withName("company type").withDeleted(false);
    }

    public static TaxOfficeBuilder someTaxOffice() {
        return TaxOfficeBuilder.aTaxOffice().withCode(RandomStringUtils.randomAlphabetic(5)).withName(RandomStringUtils.randomAlphabetic(10)).withCityCode("00").withCountryCode("AA").withDeleted(false);
    }
    public static CompanySegmentTypeBuilder someCompanySegmentType() {
        return CompanySegmentTypeBuilder.aCompanySegmentType().withCode(RandomStringUtils.randomAlphabetic(3)).withName("segment type").withDeleted(false);
    }
    public static CompanyRelationTypeBuilder someCompanyRelationType() {
        return CompanyRelationTypeBuilder.aCompanyRelationType().withCode(RandomStringUtils.randomAlphabetic(3)).withName("relation").withDeleted(false);
    }

    public static SalesPortfolioBuilder someSalesPortfolio() {
        return SalesPortfolioBuilder.aSalesPortfolio().withCode(RandomStringUtils.randomAlphabetic(3)).withName("sales portfolio").withDeleted(false);
    }

    public static CompanyBuilder someCompany(){
        return CompanyBuilder.aCompany().withName("name").withLocalName("local name")
                .withCountry(someCountry().build()).withWebsite("website").withEmailDomain("domain")
                .withType(someCompanyType().build())
                .withTaxoffice(someTaxOffice().build())
                .withTaxid("taxid").withTckn("tckn").withLogoUrl("logourl")
                .withSegmentType(someCompanySegmentType().build())
                .withShortNameChecked(false);
    }

    public static CompanyRelationBuilder someCompanyRelation(){
        return CompanyRelationBuilder.aCompanyRelation().withRelationType(someCompanyRelationType().build())
                .withActiveCompany(someCompany().build())
                .withPassiveCompany(someCompany().build())
                .withDeleted(false);
    }
}
