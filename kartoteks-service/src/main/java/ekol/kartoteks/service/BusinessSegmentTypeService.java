package ekol.kartoteks.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ekol.kartoteks.domain.BusinessSegmentType;
import ekol.kartoteks.repository.BusinessSegmentTypeRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BusinessSegmentTypeService {
	
	private BusinessSegmentTypeRepository businessSegmentTypeRepository;
	
	@Cacheable(cacheNames = "one-day-cache")
	public List<BusinessSegmentType> findAll() {
		return (List<BusinessSegmentType>) businessSegmentTypeRepository.findAll(new Sort(Sort.Direction.ASC, "rank"));
	}
	
	@Cacheable(cacheNames = "one-day-cache")
	public BusinessSegmentType findByCode(String code) {
		return businessSegmentTypeRepository.findByCode(code);
	}
}
