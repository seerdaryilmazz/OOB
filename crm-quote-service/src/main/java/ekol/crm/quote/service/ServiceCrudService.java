package ekol.crm.quote.service;

import ekol.crm.quote.domain.model.Service;
import ekol.crm.quote.domain.model.quote.Quote;
import ekol.crm.quote.repository.ServiceRepository;
import ekol.crm.quote.validator.ServiceValidator;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ServiceCrudService {

    private ServiceRepository repository;
    private ServiceValidator validator;

    @Transactional
    public Set<Service> save(Quote quote, Set<Service> existing, Set<Service> request){

        Set<Service> savedServices = new HashSet<>();
        if(!CollectionUtils.isEmpty(request)){
            Set<Service> services = request.stream().map(service -> {
                service.setQuote(quote);
                this.validator.validate(service);
                return service;
            }).collect(Collectors.toSet());

            savedServices.addAll(IteratorUtils.toList(this.repository.save(services).iterator()));
        }
        // delete discarded services
        deleteDiscardedServices(existing, savedServices);

        return savedServices;
    }

    private void deleteDiscardedServices(Set<Service> existing, Set<Service> savedServices) {
        if(!CollectionUtils.isEmpty(existing)){
            Set<Long> savedServiceIds = savedServices.stream().map(Service::getId)
                    .collect(Collectors.toSet());
            existing.stream()
                    .filter(c -> !savedServiceIds.contains(c.getId())).collect(Collectors.toList())
                    .forEach(this::delete);
        }
    }

    @Transactional
    public void delete(Service service){
        service.setDeleted(true);
        repository.save(service);
    }

    @Transactional
    public void deleteForQuote(Quote quote){
        List<Service> services = repository.findByQuote(quote);
        services.forEach(service -> {
            service.setDeleted(true);
            repository.save(service);
        });
    }
}
