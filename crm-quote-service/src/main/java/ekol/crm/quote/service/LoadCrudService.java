package ekol.crm.quote.service;

import ekol.crm.quote.domain.model.Load;
import ekol.crm.quote.domain.model.quote.Quote;
import ekol.crm.quote.repository.LoadRepository;
import ekol.crm.quote.validator.LoadValidator;
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
public class LoadCrudService {

    private LoadRepository repository;
    private LoadValidator validator;

    @Transactional
    public Set<Load> save(Quote quote, Set<Load> existing, Set<Load> request){
        Set<Load> savedLoads = new HashSet<>();
        if(!CollectionUtils.isEmpty(request)){
            Set<Load> loads = request.stream().map(load -> {
                load.setQuote(quote);
                this.validator.validate(load);
                return load;
            }).collect(Collectors.toSet());

            savedLoads.addAll(IteratorUtils.toList(this.repository.save(loads).iterator()));
        }
        // delete discarded loads
        deleteDiscardedLoads(existing, savedLoads);

        return savedLoads;
    }

    private void deleteDiscardedLoads(Set<Load> existing, Set<Load> savedLoads) {
        if(!CollectionUtils.isEmpty(existing)){
            Set<Long> savedLoadIds = savedLoads.stream().map(Load::getId)
                    .collect(Collectors.toSet());

            existing.stream()
                    .filter(c -> !savedLoadIds.contains(c.getId())).collect(Collectors.toList())
                    .forEach(this::delete);
        }
    }

    @Transactional
    public void delete(Load load){
        load.setDeleted(true);
        repository.save(load);
    }

    @Transactional
    public void deleteForQuote(Quote quote){
        List<Load> loads = repository.findByQuote(quote);
        loads.forEach(load -> {
            load.setDeleted(true);
            repository.save(load);
        });
    }
}
