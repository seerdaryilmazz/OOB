package ekol.crm.account.mockdata;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

import ekol.crm.account.builder.AccountBuilder;
import ekol.crm.account.builder.PotentialBuilder;
import ekol.crm.account.domain.enumaration.*;
import ekol.crm.account.domain.model.*;

public class MockData {

    public static final AccountBuilder ekolSpainAccount = AccountBuilder.anAccount()
            .withName("EKOL SPAIN")
            .withCompany(new IdNamePair(1L, "EKOL SPAIN"))
            .withCountry(new IsoNamePair("ES", "SPAIN"))
            .withAccountOwner("sales")
            .withAccountType(AccountType.PROSPECT)
            .withSegmentType(SegmentType.CRM)
            .withParentSector(new CodeNamePair("SERVICE", "Service"))
            .withSubSector(new CodeNamePair("SERVICE", "Service"));

    private static final Country countrySpain = Country.builder().id(1L).iso("ES").name("SPAIN").status(CountryStatus.ACTIVE).build();

    private static final Country countryPoland = Country.builder().id(2L).iso("PL").name("POLAND").status(CountryStatus.ACTIVE).build();

    private static final CountryPoint fromCountryPoint = CountryPoint.builder().id(1L).name("12").code("12").build();

    private static final CountryPoint toCountryPoint = CountryPoint.builder().id(2L).name("20").code("20").build();

    public static final PotentialBuilder ekolSpainPotential = PotentialBuilder.aPotential()
            .withId(1L)
            .withAccount(ekolSpainAccount.build())
            .withFromCountry(countrySpain)
            .withFromCountryPoint(Arrays.asList(fromCountryPoint).stream().collect(Collectors.toSet()))
            .withToCountry(countryPoland)
            .withToCountryPoint(Arrays.asList(toCountryPoint).stream().collect(Collectors.toSet()))
            .withShipmentLoadingTypes(new HashSet<>(Arrays.asList(ShipmentLoadingType.FTL)))
            .withFrequencyType(FrequencyType.ANNUAL)
            .withFrequency(2)
            .withServiceArea("ROAD")
            .withValidityStartDate(LocalDate.now())
            .withValidityEndDate(LocalDate.of(9999, Month.DECEMBER, 31))
            .withLoadWeightType(LoadWeightType.HEAVY_LOAD);
}
