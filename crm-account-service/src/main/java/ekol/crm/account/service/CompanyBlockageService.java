package ekol.crm.account.service;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import ekol.crm.account.domain.dto.CompanyBlockageDto;
import ekol.crm.account.domain.enumaration.BlockageType;
import ekol.crm.account.wsclient.CompanyBlockageClient;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class CompanyBlockageService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyBlockageService.class);
	private static final String CACHE_NAME = "company-blockage";
	
	private CompanyBlockageClient companyBlockageClient;
	
	@PostConstruct
	@CacheEvict(cacheNames = CACHE_NAME, allEntries = true)
	public void clearCache() {
		if(LOGGER.isWarnEnabled()) {
			LOGGER.warn("{} cache is cleared", CACHE_NAME);
		}
	}
	
//	@Cacheable(CACHE_NAME)
	public CompanyBlockageDto getCompanyBlockage(Long companyId) {
		String response = companyBlockageClient.getCompanyBlockage(companyId);
		if(StringUtils.isEmpty(response)) {
			return CompanyBlockageDto.with(companyId, BlockageType.NO_BLOCKAGE);
		} else {
			return CompanyBlockageDto.with(companyId, BlockageType.get(Integer.valueOf(response)));
		}
	}
}
