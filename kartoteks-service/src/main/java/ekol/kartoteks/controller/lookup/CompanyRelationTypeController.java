package ekol.kartoteks.controller.lookup;

import ekol.kartoteks.domain.CompanyRelationType;
import ekol.kartoteks.repository.CompanyRelationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

/**
 * Created by kilimci on 06/05/16.
 */
@RestController
@RequestMapping("/company-relation-type")
public class CompanyRelationTypeController {

    @Autowired
    private CompanyRelationTypeRepository repository;

    @RequestMapping(value="", method = RequestMethod.POST)
    public CompanyRelationType add(@RequestBody CompanyRelationType lookup){
        return repository.save(lookup);
    }

    @RequestMapping(value="", method = RequestMethod.GET)
    public List<CompanyRelationType> findAll(){
        return repository.findByOrderByName();
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public CompanyRelationType get(@PathVariable Long id){
        return repository.findOne(id);
    }

    @RequestMapping(value="/{id}", method = RequestMethod.PUT)
    public CompanyRelationType update(@PathVariable Long id, @RequestBody CompanyRelationType updatedLookup){
        CompanyRelationType lookup = repository.findOne(id);
        lookup.setName(updatedLookup.getName().toUpperCase(Locale.ENGLISH));
        lookup.setCode(updatedLookup.getCode().toUpperCase(Locale.ENGLISH));
        lookup.setAltName(updatedLookup.getAltName().toUpperCase(Locale.ENGLISH));
        return repository.save(lookup);
    }

    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id){
        CompanyRelationType lookup = repository.findOne(id);
        lookup.setDeleted(true);
        repository.save(lookup);
    }
}
