package ekol.usermgr.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.usermgr.domain.Language;

public interface LanguageRepository extends ApplicationRepository<Language> {

    Iterable<Language> findAllByOrderByNameAsc();

    Language findByIsoCodeIgnoreCase(String isoCode);

}

