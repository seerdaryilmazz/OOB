package ekol.orders.lookup.controller;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ekol.event.auth.Authorize;
import ekol.exceptions.ResourceNotFoundException;
import ekol.orders.lookup.domain.Incoterm;
import ekol.orders.lookup.repository.IncotermRepository;
import lombok.AllArgsConstructor;

/**
 * Created by kilimci on 12/04/2017.
 */
@RestController
@RequestMapping("/incoterm")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class IncotermController {

    private IncotermRepository incotermRepository;

    private void check(Long id){
        if(!incotermRepository.exists(id)){
            throw new ResourceNotFoundException("Incoterm with {0} not found", id);
        }
    }

    @Authorize(operations="order.incoterms.manage")
    @PostMapping(value = {"/", ""})
    public Incoterm add(@RequestBody Incoterm lookup){
        lookup.setName(lookup.getName().toUpperCase(Locale.ENGLISH));
        lookup.setCode(lookup.getCode().toUpperCase(Locale.ENGLISH));
        return incotermRepository.save(lookup);
    }

    @GetMapping(value = {"/", ""})
    public List<Incoterm> findAll(){
        return incotermRepository.findByOrderByName();
    }

    @GetMapping(value = {"/{id}/", "/{id}"})
    public Incoterm get(@PathVariable Long id){
        check(id);
        return incotermRepository.findOne(id);
    }

    @GetMapping(value = "/byCode")
    public Incoterm findByCode(@RequestParam String code) {
        Optional<Incoterm> optional = incotermRepository.findByCodeAndActiveTrue(code);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new ResourceNotFoundException("Incoterm not found");
        }
    }

    @GetMapping(value = "/byName")
    public Incoterm findByName(@RequestParam String name) {
        Optional<Incoterm> optional = incotermRepository.findByNameIgnoreCaseAndActiveTrue(name);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new ResourceNotFoundException("Incoterm not found");
        }
    }

    @Authorize(operations="order.incoterms.manage")
    @PutMapping(value = {"/{id}/", "/{id}"})
    public Incoterm update(@PathVariable Long id, @RequestBody Incoterm updated){
        check(id);
        return incotermRepository.save(updated);
    }

    @Authorize(operations="order.incoterms.manage")
    @DeleteMapping(value = {"/{id}/", "/{id}"})
    public void delete(@PathVariable Long id){
        check(id);
        Incoterm incoterm = incotermRepository.findOne(id);
        incoterm.setDeleted(true);
        incotermRepository.save(incoterm);
    }

    private Incoterm setActive(Long id, boolean active){
        if(!incotermRepository.exists(id)){
            throw new ResourceNotFoundException("Incoterm with {0} id not found", id);
        }
        Incoterm incoterm = incotermRepository.findOne(id);
        incoterm.setActive(active);
        return incotermRepository.save(incoterm);
    }
    
    @Authorize(operations="order.incoterms.manage")
    @PutMapping(value = {"/{id}/activate"})
    public Incoterm activate(@PathVariable Long id){
        return this.setActive(id, true);
    }
    
    @Authorize(operations="order.incoterms.manage")
    @PutMapping(value = {"/{id}/inactivate"})
    public Incoterm inactivate(@PathVariable Long id){
        return this.setActive(id, false);
    }
}
