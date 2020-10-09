package ekol.crm.quote.service;

import ekol.crm.quote.domain.model.ContainerRequirement;
import ekol.crm.quote.domain.model.quote.Quote;
import ekol.crm.quote.repository.ContainerRequirementRepository;
import ekol.crm.quote.validator.ContainerRequirementValidator;
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
public class ContainerRequirementCrudService {

    private ContainerRequirementRepository repository;
    private ContainerRequirementValidator validator;

    @Transactional
    public Set<ContainerRequirement> save(Quote quote, Set<ContainerRequirement> existing, Set<ContainerRequirement> request){

        Set<ContainerRequirement> savedContainerRequirements = new HashSet<>();
        if(!CollectionUtils.isEmpty(request)){
            Set<ContainerRequirement> containerRequirements = request.stream().map(containerRequirement -> {
                containerRequirement.setQuote(quote);
                this.validator.validate(containerRequirement);
                return containerRequirement;
            }).collect(Collectors.toSet());

            savedContainerRequirements.addAll(IteratorUtils.toList(this.repository.save(containerRequirements).iterator()));
        }

        // delete discarded container requirements
        deleteDiscardedContainerRequirement(existing, savedContainerRequirements);

        return savedContainerRequirements;
    }

    private void deleteDiscardedContainerRequirement(Set<ContainerRequirement> existing, Set<ContainerRequirement> savedContainerRequirements) {

        if(!CollectionUtils.isEmpty(existing)){
            Set<Long> savedContainerRequirementsIds = savedContainerRequirements.stream().map(ContainerRequirement::getId)
                    .collect(Collectors.toSet());

            existing.stream()
                    .filter(c -> !savedContainerRequirementsIds.contains(c.getId())).collect(Collectors.toList())
                    .forEach(this::delete);
        }
    }

    @Transactional
    public void delete(ContainerRequirement containerRequirement){
        containerRequirement.setDeleted(true);
        repository.save(containerRequirement);
    }

    public List<ContainerRequirement> listForQuote(Quote quote){
        return repository.findByQuote(quote);
    }

    @Transactional
    public void deleteForQuote(Quote quote){
        List<ContainerRequirement> containerRequirements = repository.findByQuote(quote);
        containerRequirements.forEach(containerRequirement -> {
            containerRequirement.setDeleted(true);
            repository.save(containerRequirement);
        });
    }
}
