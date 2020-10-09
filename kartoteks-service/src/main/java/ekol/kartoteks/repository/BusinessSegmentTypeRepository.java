package ekol.kartoteks.repository;


import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.hibernate5.domain.repository.LookupRepository;
import ekol.kartoteks.domain.BusinessSegmentType;

/**
 * Created by fatmaozyildirim on 5/5/16.
 */
public interface BusinessSegmentTypeRepository extends ApplicationRepository<BusinessSegmentType> {

    BusinessSegmentType findByCode(String code);

}
