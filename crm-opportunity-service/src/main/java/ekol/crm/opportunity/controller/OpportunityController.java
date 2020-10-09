package ekol.crm.opportunity.controller;

import java.util.*;
import java.util.stream.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ekol.crm.opportunity.domain.dto.*;
import ekol.crm.opportunity.domain.enumaration.OpportunityStatus;
import ekol.crm.opportunity.domain.model.Opportunity;
import ekol.crm.opportunity.repository.OpportunityRepository;
import ekol.crm.opportunity.service.OpportunityService;
import ekol.crm.opportunity.validation.*;
import ekol.resource.validation.OneOrderValidation;
import lombok.AllArgsConstructor;

/**
 * Created by Dogukan Sahinturk on 18.11.2019
 */
@RestController
@RequestMapping("/opportunity")
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Validated
public class OpportunityController {

    private OpportunityService opportunityService;
    private OpportunityRepository opportunityRepository;

    @PostMapping
    public OpportunityJson createOpportunity(@Valid @RequestBody OpportunityJson request, @RequestParam(required = false) Map<String, String> parameters){
        request.setOpportunityAttribute(parameters);
        return OpportunityJson.fromEntity(opportunityService.save(request));
    }

    @PutMapping("/{id}")
	@OneOrderValidation(CloseOpportunityValidation.class)
	@OneOrderValidation(CancelOpportunityValidation.class)
    public OpportunityJson updateOpportunity(@Valid @PathVariable Long id, @RequestBody OpportunityJson request, @RequestParam(required = false) Map<String, String> parameters){
        request.setId(id);
        request.setOpportunityAttribute(parameters);
        return OpportunityJson.fromEntity(opportunityService.save(request));
    }

    @GetMapping("/{id}")
    public OpportunityJson retrieveOpportunity(@PathVariable Long id) {
        return opportunityService.getOpportunityById(id);
    }

    @PutMapping("/{opportunityId}/notes")
    public List<NoteJson> updateNotes(@PathVariable Long opportunityId, @RequestBody List<NoteJson> request) {
        return opportunityService.updateNotes(opportunityId, request).stream().map(NoteJson::fromEntity).collect(Collectors.toList());
    }

    @PutMapping("/{opportunityId}/documents")
    public List<DocumentJson> updateDocuments(@PathVariable Long opportunityId, @RequestBody List<DocumentJson> request) {
        return opportunityService.updateDocuments(opportunityId, request).stream().map(DocumentJson::fromEntity).collect(Collectors.toList());
    }

    @GetMapping
    public Page<OpportunityJson> retrieveOpportunities(@RequestParam(required = false, defaultValue = "0") Integer page,
                                                       @RequestParam(required = false, defaultValue = "10") @Max(100) Integer size,
                                                       @RequestParam(required = false) Long accountId,
                                                       @RequestParam(required = false, defaultValue = "false") boolean ignoreCanceled) {

        Pageable pageRequest = new PageRequest(page, size, new Sort(Sort.Direction.DESC, "lastUpdated"));
        Page<Opportunity> entityPage = opportunityService.retrieveOpportunities(pageRequest, accountId, ignoreCanceled);
        List<OpportunityJson> opportunities = entityPage.getContent().stream().map(OpportunityJson::fromEntity).collect(Collectors.toList());
        return new PageImpl<>(opportunities, pageRequest, entityPage.getTotalElements());
    }
    
    @GetMapping("/list-by-accountId/{accountId}")
    public List<OpportunityJson> listOpportunities(@PathVariable Long accountId){
    	return opportunityService.getByAccountId(accountId).stream()
                .filter(p->OpportunityStatus.CANCELED != p.getStatus())
                .map(OpportunityJson :: fromEntity)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public void deleteOpportunityById(@PathVariable Long id){
        Opportunity opportunity = opportunityService.getByIdOrThrowException(id);
        opportunityService.deleteOpportunity(opportunity);
    }

    @DeleteMapping("/list")
    public void deleteOpportunities(@RequestBody List<Long> opportunityIds){
        Iterable<Opportunity> opportunities = opportunityRepository.findAll(opportunityIds);
        opportunityService.deleteOpportunity(opportunities);
    }
    
	@PutMapping("/{id}/open")
	public OpportunityJson reopen(@PathVariable Long id) {
		return OpportunityJson.fromEntity(opportunityService.reopen(id));
	}

}
