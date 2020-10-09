package ekol.crm.quote.service;

import ekol.crm.quote.domain.model.VehicleRequirement;
import ekol.crm.quote.domain.model.quote.Quote;
import ekol.crm.quote.repository.VehicleRequirementRepository;
import ekol.crm.quote.validator.VehicleRequirementValidator;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class VehicleRequirementCrudService {

    private VehicleRequirementRepository repository;
    private VehicleRequirementValidator validator;

    @Transactional
    public Set<VehicleRequirement> save(Quote quote, Set<VehicleRequirement> existing, Set<VehicleRequirement> request){

        Set<VehicleRequirement> savedVehicleRequirements = new HashSet<>();
        if(!CollectionUtils.isEmpty(request)){
            Set<VehicleRequirement> vehicleRequirements = request.stream().map(vehicleRequirement -> {
                vehicleRequirement.setQuote(quote);
                this.validator.validate(vehicleRequirement);
                return vehicleRequirement;
            }).collect(Collectors.toSet());

            savedVehicleRequirements.addAll(IteratorUtils.toList(this.repository.save(vehicleRequirements).iterator()));
        }

        // delete discarded vehicle requirements
        deleteDiscardedVehicleRequirements(existing, savedVehicleRequirements);

        return savedVehicleRequirements;
    }

    private void deleteDiscardedVehicleRequirements(Set<VehicleRequirement> existing, Set<VehicleRequirement> savedVehicleRequirements) {

        if(!CollectionUtils.isEmpty(existing)){
            Set<Long> savedVehicleRequirementIds = savedVehicleRequirements.stream().map(VehicleRequirement::getId)
                    .collect(Collectors.toSet());

            existing.stream()
                    .filter(c -> !savedVehicleRequirementIds.contains(c.getId())).collect(Collectors.toList())
                    .forEach(this::delete);
        }
    }

    @Transactional
    public void delete(VehicleRequirement vehicleRequirement){
        vehicleRequirement.setDeleted(true);
        repository.save(vehicleRequirement);
    }

    public List<VehicleRequirement> listForQuote(Quote quote){
        return repository.findByQuote(quote);
    }

    @Transactional
    public void deleteForQuote(Quote quote){
        List<VehicleRequirement> vehicleRequirements = repository.findByQuote(quote);
        vehicleRequirements.forEach(vehicleRequirement -> {
            vehicleRequirement.setDeleted(true);
            repository.save(vehicleRequirement);
        });
    }
}
