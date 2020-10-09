package ekol.crm.account.service;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.criteria.JoinType;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ekol.crm.account.domain.dto.potential.*;
import ekol.crm.account.domain.enumaration.LoadWeightType;
import ekol.crm.account.domain.enumaration.ShipmentLoadingType;
import ekol.crm.account.domain.model.Account;
import ekol.crm.account.domain.model.potential.*;
import ekol.crm.account.repository.PotentialRepository;
import ekol.crm.account.repository.specs.PotentialSpecification;
import ekol.crm.account.repository.specs.SpecificationUtils;
import ekol.crm.account.util.Utils;
import ekol.crm.account.validator.PotentialValidator;
import ekol.exceptions.ResourceNotFoundException;
import ekol.exceptions.ValidationException;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.model.CodeNamePair;
import ekol.resource.oauth2.SessionOwner;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PotentialCrudService {

    private PotentialRepository repository;
    private PotentialValidator validator;
    private AccountCrudService accountService;
    private ApplicationEventPublisher applicationEventPublisher;
    private SessionOwner sessionOwner;

    @Transactional
    public Potential save(Long accountId, Potential potential){

        if(accountId != null){
            Account account = accountService.getByIdOrThrowException(accountId);
            potential.setAccount(account);
        }
        this.validator.validate(potential);
        this.checkForDuplicatedRecord(potential);
        if(potential.getId() == null){
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
            potential.setCreatedAt(new UtcDateTime(now.toLocalDateTime()));
        }
        Potential savedPotential = repository.save(potential);
        applicationEventPublisher.publishEvent(savedPotential);
        return savedPotential;
    }

    @Transactional
    public Potential shiftValidityStartDate(Long potentialId, int numberOfDays) {
        Potential potential = getByIdOrThrowException(potentialId);
        potential.setValidityStartDate(potential.getValidityStartDate().plusDays(numberOfDays));
        return this.repository.save(potential);
    }

    public List<Potential> getByAccountId(Long accountId){
        return repository.findByAccountId(accountId);
    }

    public void checkForDuplicatedRecord(Potential potential){
    	if(PotentialStatus.INACTIVE == potential.getStatus()) {
    		return;
    	}
    	
        List<Potential> existingRecords;
        if(potential instanceof CustomsPotential){
        	existingRecords = repository.findByAccountIdAndCustomsType(
                    potential.getAccount().getId(),
                    potential.getCustomsType());
        }else{
        	existingRecords = findDuplicated(potential);
        }

        if (potential instanceof SeaPotential) {
            for (Iterator<Potential> iterator = existingRecords.iterator(); iterator.hasNext();) {
                Potential p = iterator.next();
                if (CollectionUtils.isEmpty(CollectionUtils.intersection(potential.getShipmentLoadingTypes(), p.getShipmentLoadingTypes()))) {
                    iterator.remove();
                }
            }
        }

        if(existingRecords.stream().filter(t->Objects.equals(t.getStatus(), PotentialStatus.ACTIVE)).filter(existing -> !Objects.equals(existing.getId(), potential.getId())).count()>0){
        	String message;
        	Object[] args = {};
            if (potential instanceof CustomsPotential) {
                message = "Potential with the same Import/Export information already exists!";
            } else if (potential instanceof SeaPotential) {
                message = "Potential with the same From Country, From Port, To Country, To Port and FCL/LCL information already exists!";
            } else {
            	args = existingRecords.stream()
            			.filter(t->Objects.equals(t.getStatus(), PotentialStatus.ACTIVE))
            			.filter(pt->!Objects.equals(pt.getId(), potential.getId()))
            			.limit(3).map(Potential::toString).toArray();
            	StringBuilder builder = new StringBuilder("The potential you entered has duplicate records:");
            	IntStream.range(0, args.length).forEach(i->builder.append("<br />{"+ i +"}"));
            	message = builder.toString();
            }
            throw new ValidationException(message, args);
        }
    }
    
    private List<Potential> findDuplicated(Potential potential) {
    	Specifications<Potential> specs = Specifications.where(PotentialSpecification.serviceAreaEqual(potential.getServiceArea()))
    			.and(PotentialSpecification.accountIdEqual(potential.getAccount().getId()))
    			.and(PotentialSpecification.fromCountryIdEqual(potential.getFromCountry().getId()))
    			.and(PotentialSpecification.toCountryIdEqual(potential.getToCountry().getId()));
    	if(CollectionUtils.isNotEmpty(potential.getFromCountryPoint())) {
    		specs = specs.and((root, criteriaQuery, cb)-> cb.or(cb.isEmpty(root.get(Potential_.fromCountryPoint)), root.join(Potential_.fromCountryPoint, JoinType.LEFT).in(potential.getFromCountryPoint())));
    	}
    	if(CollectionUtils.isNotEmpty(potential.getToCountryPoint())) {
    		specs = specs.and((root, criteriaQuery, cb)-> cb.or(cb.isEmpty(root.get(Potential_.toCountryPoint)), root.join(Potential_.toCountryPoint, JoinType.LEFT).in(potential.getToCountryPoint())));
    	}
    	return repository.findAll(specs);
}

    public Page<PotentialJson> search(PotentialSearchJson searchConfig) {

        Specifications<Potential> specs = null;

        if (searchConfig.getAccountId() != null) {
            specs = SpecificationUtils.append(specs, PotentialSpecification.accountIdEqual(searchConfig.getAccountId()));
        }
        if (searchConfig.getServiceArea() != null) {
            specs = SpecificationUtils.append(specs, PotentialSpecification.serviceAreaEqual(searchConfig.getServiceArea()));
        }
        if (searchConfig.getFromCountryId() != null) {
            specs = SpecificationUtils.append(specs, PotentialSpecification.fromCountryIdEqual(searchConfig.getFromCountryId()));
        }
        if (searchConfig.getFromCountryPointId() != null) {
            specs = SpecificationUtils.append(specs, PotentialSpecification.fromCountryPointIdIn(searchConfig.getFromCountryPointId()));
        }
        if (searchConfig.getToCountryId() != null) {
            specs = SpecificationUtils.append(specs, PotentialSpecification.toCountryIdEqual(searchConfig.getToCountryId()));
        }
        if (searchConfig.getToCountryPointId() != null) {
            specs = SpecificationUtils.append(specs, PotentialSpecification.toCountryPointIdIn(searchConfig.getToCountryPointId()));
        }
        if (StringUtils.isNotBlank(searchConfig.getLoadWeightTypeCode())) {
            LoadWeightType loadWeightType = LoadWeightType.valueOf(searchConfig.getLoadWeightTypeCode());
            specs = SpecificationUtils.append(specs, PotentialSpecification.loadWeightTypeIn(Arrays.asList(loadWeightType)));
        }
        if (StringUtils.isNotBlank(searchConfig.getShipmentLoadingTypeCode())) {
            ShipmentLoadingType shipmentLoadingType = ShipmentLoadingType.valueOf(searchConfig.getShipmentLoadingTypeCode());
            specs = SpecificationUtils.append(specs, PotentialSpecification.shipmentLoadingTypesIn(Arrays.asList(shipmentLoadingType)));
        }
        if (searchConfig.getActive() != null) {
            LocalDate localDate = Utils.getLocalDateAtTimeZone(sessionOwner.getCurrentUser().getTimeZoneId());
            if (searchConfig.getActive()) {
                specs = SpecificationUtils.append(specs, PotentialSpecification.validityPeriodContains(localDate));
            } else {
                specs = SpecificationUtils.append(specs, PotentialSpecification.validityPeriodDoesNotContain(localDate));
            }
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(searchConfig.getCreatedBy())) {
            specs = SpecificationUtils.append(specs, PotentialSpecification.createdByEqual(searchConfig.getCreatedBy()));
        }
        if (searchConfig.getMinCreationDate() != null) {
            specs = SpecificationUtils.append(specs, PotentialSpecification.createdAtGreaterThanOrEqual(LocalDateTime.of(searchConfig.getMinCreationDate(), LocalTime.MIN)));
        }
        if (searchConfig.getMaxCreationDate() != null) {
            specs = SpecificationUtils.append(specs, PotentialSpecification.createdAtLessThanOrEqual(LocalDateTime.of(searchConfig.getMaxCreationDate(), LocalTime.MAX)));
        }

        PageRequest pageRequest = new PageRequest(searchConfig.getPage(), searchConfig.getPageSize(), Sort.Direction.DESC, "createdAt");

        Page<Potential> entityPage = repository.findAll(specs, pageRequest);

        List<PotentialJson> potentials = entityPage.getContent().stream().map(Potential::toJson)
                .collect(Collectors.toList());

        return new PageImpl<>(potentials, pageRequest, entityPage.getTotalElements());
    }

    public Potential getByIdOrThrowException(Long id) {
        return this.repository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Potential with id {0} not found", id)
        );
    }

    public void delete(Potential potential){
        potential.setDeleted(true);
        this.repository.save(potential);
    }

    public void validate(PotentialValidationRequest request){
        Potential potential = getByIdOrThrowException(request.getPotentialId());
        if(potential == null){
            throw new ValidationException("Potential with id {0} not found", request.getPotentialId());
        }
        if(StringUtils.isEmpty(request.getServiceArea())){
            throw new ValidationException("Service area should not be empty.", request.getPotentialId());
        }
        if("ROAD".equalsIgnoreCase(request.getServiceArea())){
            if(StringUtils.isEmpty(request.getShipmentLoadingType())) {
                throw new ValidationException("Shipment loading type should not be empty");
            }
            if(request.getPayWeight() == null || !(request.getPayWeight().compareTo(BigDecimal.ZERO) > 0)) {
                throw new ValidationException("Pay weight should not be empty");
            }
            List<ShipmentLoadingType> shipmentLoadingTypes = potential.getShipmentLoadingTypes().stream()
                    .filter(shipmentLoadingType -> shipmentLoadingType.name().startsWith(request.getShipmentLoadingType())).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(shipmentLoadingTypes)){
                throw new ValidationException("Potential should have a matching shipment loading type");
            }
            if("LTL".equalsIgnoreCase(request.getShipmentLoadingType())){
            	if(request.getServices().stream().map(CodeNamePair::getCode).anyMatch("SPEEDY"::equals) && BigDecimal.valueOf(4200).compareTo(request.getPayWeight()) <= 0) {
            		throw new ValidationException("The Pay Weight value for the quote should be less than 4,200!");
            		
            	} else if(request.getServices().stream().map(CodeNamePair :: getCode).anyMatch("SPEEDY_VAN"::equals) && BigDecimal.valueOf(2800).compareTo(request.getPayWeight()) <= 0 ) {
            		throw new ValidationException("The Pay Weight value for the quote should be less than 2,800!");
            		
            	} else if(shipmentLoadingTypes.stream().noneMatch(shipmentLoadingType ->
                        shipmentLoadingType.getMaxPayWeight() == null || shipmentLoadingType.getMaxPayWeight().compareTo(request.getPayWeight()) > 0)){
                    throw new ValidationException("The Pay Weight value for the quote should be less than {0}!",
                            shipmentLoadingTypes.stream().max(Comparator.comparing(ShipmentLoadingType::getMaxPayWeight)).orElseThrow(NoSuchElementException::new).getMaxPayWeight());
                }
            }
        }
    }
}
