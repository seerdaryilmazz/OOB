package ekol.crm.account.controller;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ekol.crm.account.domain.dto.ValidationJson;
import ekol.crm.account.domain.dto.potential.PotentialJson;
import ekol.crm.account.domain.dto.potential.PotentialSearchJson;
import ekol.crm.account.domain.dto.potential.PotentialValidationRequest;
import ekol.crm.account.domain.model.Account;
import ekol.crm.account.domain.model.potential.Potential;
import ekol.crm.account.service.AccountCrudService;
import ekol.crm.account.service.PotentialCrudService;
import ekol.exceptions.ValidationException;
import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/potential")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PotentialController {

    private PotentialCrudService potentialCrudService;
    private AccountCrudService accountCrudService;

    @Timed(value = "crm-account.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @PostMapping("/{accountId}")
    public PotentialJson createPotential(@PathVariable Long accountId, @RequestBody PotentialJson request) {
        request.setValidityStartDate(LocalDate.now());
        request.setValidityEndDate(LocalDate.of(9999, Month.DECEMBER, 31));
        request.validatePotential();
        return potentialCrudService.save(accountId, request.toEntity()).toJson();
    }

    @Timed(value = "crm-account.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @PutMapping("/{accountId}")
    public PotentialJson updatePotential(@PathVariable Long accountId, @RequestBody PotentialJson request) {
        request.validatePotential();
        return potentialCrudService.save(accountId, request.toEntity()).toJson();
    }

    @PutMapping("/{potentialId}/shiftValidityStartDate")
    public PotentialJson shiftValidityStartDate(@PathVariable Long potentialId, @RequestParam int numberOfDays) {
        return potentialCrudService.shiftValidityStartDate(potentialId, numberOfDays).toJson();
    }

    @DeleteMapping("/{potentialId}")
    public void deletePotential(@PathVariable Long potentialId){
        Potential potential = potentialCrudService.getByIdOrThrowException(potentialId);
        potentialCrudService.delete(potential);
    }

    @Timed(value = "crm-account.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @PostMapping("/search")
    public Page<PotentialJson> search(@Valid @RequestBody PotentialSearchJson searchConfig) {
        return potentialCrudService.search(searchConfig);
    }
    
   
    @GetMapping("/search-potentials/{accountId}")
    public List<PotentialJson> searchPotentialsByAccountId(@PathVariable Long accountId) {
    	List<Potential> potentials = potentialCrudService.getByAccountId(accountId);
        return potentials.stream()
        		.map(Potential::toJson)
        		.collect(Collectors.toList());
    }

    @Timed(value = "crm-account.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @GetMapping("/{potentialId}")
    public PotentialJson retrievePotential(@PathVariable Long potentialId) {
        return potentialCrudService.getByIdOrThrowException(potentialId).toJson();
    }
        

    @PostMapping("/validate")
    public ValidationJson validate(@RequestBody PotentialValidationRequest request) {
        try{
            potentialCrudService.validate(request);
        }catch(ValidationException ve){
            return ValidationJson.anInvalidResult(ve);
        }
        return ValidationJson.aValidResult();
    }
    
    
    @GetMapping("/duplicate-control")   
    public void duplicateControl(@RequestParam Long potentialId, @RequestParam Long accountId ) {
    	Account account = accountCrudService.getByIdOrThrowException(accountId);
    	Potential potential = potentialCrudService.getByIdOrThrowException(potentialId);
    	potential.setAccount(account);
        potentialCrudService.checkForDuplicatedRecord(potential);
    }
}
