package ekol.kartoteks.repository.common;


import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.kartoteks.domain.TaxOffice;

import java.util.List;

/**
 * Created by kilimci on 19/03/16.
 */
public interface TaxOfficeRepository extends ApplicationRepository<TaxOffice> {
    List<TaxOffice> findByCountryCodeAndCityCodeOrderByNameAsc(String countryCode, String cityCode);
    List<TaxOffice> findByCountryCodeOrderByNameAsc(String countryCode);
    List<TaxOffice> findAllByOrderByNameAsc();
    TaxOffice findByCode(String code);
}
