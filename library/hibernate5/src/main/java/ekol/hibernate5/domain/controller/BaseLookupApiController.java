package ekol.hibernate5.domain.controller;

import ekol.hibernate5.domain.entity.LookupEntity;
import ekol.hibernate5.domain.repository.LookupRepository;
import ekol.model.LookupValueLabel;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public abstract class BaseLookupApiController<T extends LookupEntity> {

    private LookupRepository<T> lookupRepository;

    public void setLookupRepository(LookupRepository<T> lookupRepository) {
        this.lookupRepository = lookupRepository;
    }

    @RequestMapping(value = {"/", ""}, method = RequestMethod.POST)
    public T add(@RequestBody T lookup){
        lookup.setName(lookup.getName().toUpperCase(Locale.ENGLISH));
        lookup.setCode(lookup.getCode().toUpperCase(Locale.ENGLISH));
        return lookupRepository.save(lookup);
    }

    @RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
    public List<LookupValueLabel> findAll(){
        List<T> lookups = lookupRepository.findByOrderByName();
        return lookups.stream().map(lt ->
                new LookupValueLabel(lt.getId().toString(), lt.getName(), lt.getCode())).collect(Collectors.toList());

    }

    @RequestMapping(value = {"/{id}/", "/{id}"}, method = RequestMethod.GET)
    public LookupValueLabel get(@PathVariable Long id){
        T lookup = lookupRepository.findOne(id);
        return new LookupValueLabel(lookup.getId().toString(), lookup.getName(), lookup.getCode());
    }

    @RequestMapping(value = {"/{id}/", "/{id}"}, method = RequestMethod.PUT)
    public T update(@PathVariable Long id, @RequestBody T updatedLookup){
        T lookup = lookupRepository.findOne(id);
        lookup.setName(updatedLookup.getName().toUpperCase(Locale.ENGLISH));
        lookup.setCode(updatedLookup.getCode().toUpperCase(Locale.ENGLISH));
        return lookupRepository.save(lookup);
    }

    @RequestMapping(value = {"/{id}/", "/{id}"}, method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id){
        T lookup = lookupRepository.findOne(id);
        lookup.setDeleted(true);
        lookupRepository.save(lookup);
    }
}
