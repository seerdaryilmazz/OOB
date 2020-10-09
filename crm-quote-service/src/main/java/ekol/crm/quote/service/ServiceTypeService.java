package ekol.crm.quote.service;

import java.util.*;
import java.util.stream.*;

import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ekol.crm.quote.domain.enumaration.ServiceTypeCategory;
import ekol.crm.quote.domain.model.ServiceType;
import ekol.crm.quote.repository.ServiceTypeRepository;
import ekol.exceptions.*;
import ekol.model.CodeNamePair;
import ekol.oneorder.configuration.ConfigurationApi;
import ekol.oneorder.configuration.dto.ListOption;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ServiceTypeService {

	private ConfigurationApi configurationApi;
	private ServiceTypeRepository serviceTypeRepository;
	
	public List<ServiceType> list(){
		return serviceTypeRepository.findAll();
	}
	
	public List<ServiceType> listExtraServices(String serviceArea, Long subsidiaryId) {
		String key = String.format("%s_EXTRA_SERVICE_TYPE", serviceArea);
		ListOption option = configurationApi.getList(key, subsidiaryId);
		Set<String> serviceTypeCodes = Stream.of(option.getValue()).parallel().map(CodeNamePair::getCode).collect(Collectors.toSet());
		return listBy(null, ServiceTypeCategory.EXTRA, new String[] {serviceArea} ).stream().filter(t->serviceTypeCodes.contains(t.getCode())).collect(Collectors.toList());
	}
	
	public List<ServiceType> listBy(String code, ServiceTypeCategory category, String[] serviceArea){
		return list().parallelStream()
				.filter(t->Objects.isNull(code) || Objects.equals(t.getCode(), code))
				.filter(t->Objects.isNull(category) || Objects.equals(t.getCategory(), category))
				.filter(t->ArrayUtils.isEmpty(serviceArea) || StringUtils.isBlank(t.getServiceArea()) || Stream.of(serviceArea).anyMatch(t.getServiceArea()::equals))
				.collect(Collectors.toList());
	}
	
	public ServiceType findById(String id) {
		return Optional.of(serviceTypeRepository.findOne(id)).orElseThrow(ResourceNotFoundException::new);
	}
	
	public ServiceType save(ServiceType request) {
		if(list().stream().filter(t->Objects.equals(t.getServiceArea(), request.getServiceArea())).anyMatch(t->t.getCode().equalsIgnoreCase(request.getCode()))){
			throw new ValidationException("This service code already exists for {0}", request.getServiceArea());
		}
		if(list().stream().filter(t->Objects.equals(t.getServiceArea(), request.getServiceArea())).anyMatch(t->t.getName().equalsIgnoreCase(request.getName()))){
			throw new ValidationException("This service name already exists for {0}", request.getServiceArea());
		}
		return serviceTypeRepository.save(request);
	}
	
	public ServiceType update(String id, ServiceType request) {
		if(list().stream().filter(t->!Objects.equals(t.getId(), id)).filter(t->Objects.equals(t.getServiceArea(), request.getServiceArea())).anyMatch(t->t.getCode().equalsIgnoreCase(request.getCode()))){
			throw new ValidationException("This service code already exists for {0}", request.getServiceArea());
		}
		if(list().stream().filter(t->!Objects.equals(t.getId(), id)).filter(t->Objects.equals(t.getServiceArea(), request.getServiceArea())).anyMatch(t->t.getName().equalsIgnoreCase(request.getName()))){
			throw new ValidationException("This service name already exists for {0}", request.getServiceArea());
		}
		
		ServiceType entity = findById(id);
		Optional.of(request).map(ServiceType::getBillingItem).ifPresent(entity::setBillingItem);
		Optional.of(request).map(ServiceType::getCategory).ifPresent(entity::setCategory);
		Optional.of(request).map(ServiceType::getCode).ifPresent(entity::setCode);
		Optional.of(request).map(ServiceType::getName).ifPresent(entity::setName);
		Optional.of(request).map(ServiceType::getServiceArea).ifPresent(entity::setServiceArea);
		return serviceTypeRepository.save(entity);
	}
}
