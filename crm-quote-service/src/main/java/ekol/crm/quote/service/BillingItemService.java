package ekol.crm.quote.service;

import java.util.*;
import java.util.stream.*;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ekol.crm.quote.domain.model.BillingItem;
import ekol.crm.quote.repository.BillingItemRepository;
import ekol.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BillingItemService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BillingItemService.class);
	private static final String CACHE_NAME = "billing-item-cache";
	
	private BillingItemRepository billingItemRepository;

	public Iterable<BillingItem> listBillingItems(){
		return billingItemRepository.findAll();
	}

	public BillingItem getBillingItemByName(String name){
		return StreamSupport.stream(billingItemRepository.findAll().spliterator(),false)
				.filter(i->Objects.equals(name, i.getName()))
				.findFirst().orElse(null);
	}
	
	public List<BillingItem> getBillingItemByCode(Set<String> codes){
		return StreamSupport.stream(billingItemRepository.findAll().spliterator(),false)
				.filter(i->codes.contains(i.getCode()))
				.collect(Collectors.toList());
	}
	
	public List<BillingItem> getBillingItemByName(Set<String> names){
		return StreamSupport.stream(billingItemRepository.findAll().spliterator(),false)
				.filter(i->names.contains(i.getName()))
				.collect(Collectors.toList());
	}

	public List<BillingItem> getBillingItemsByServiceArea(String serviceArea){
		return getBillingItemsByServiceAreas(Arrays.asList(serviceArea));
	}

	public List<BillingItem> getBillingItemsByServiceAreas(Collection<String> serviceArea){
		return StreamSupport.stream(billingItemRepository.findAll().spliterator(),true)
				.filter(i->serviceArea.stream().anyMatch(i.getServiceArea()::equals))
				.collect(Collectors.toList());
	}
	
	public BillingItem findById(Long id) {
		return StreamSupport.stream(billingItemRepository.findAll().spliterator(),true)
				.filter(i->Objects.equals(id, i.getId()))
				.findFirst()
				.orElseThrow(ResourceNotFoundException::new);
	}
	
	@CacheEvict(cacheNames = CACHE_NAME, allEntries = true)
	public BillingItem save(BillingItem request) {
		return billingItemRepository.save(request);
	}
	
	@CacheEvict(cacheNames = CACHE_NAME, allEntries = true)
	public BillingItem update (Long id, BillingItem request) {
		BillingItem entity = findById(id);
		Optional.of(request).map(BillingItem::getCode).ifPresent(entity::setCode);
		Optional.of(request).map(BillingItem::getDescription).ifPresent(entity::setDescription);
		Optional.of(request).map(BillingItem::getName).ifPresent(entity::setName);
		Optional.of(request).map(BillingItem::getServiceArea).ifPresent(entity::setServiceArea);
		return billingItemRepository.save(entity);
	}

	@CacheEvict(cacheNames = CACHE_NAME, allEntries = true)
	public String clearCache() {
		LOGGER.info("{} is evicted", CACHE_NAME);
		return HttpStatus.OK.name();
	}
}
