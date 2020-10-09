package ekol.crm.search.controller;

import javax.validation.constraints.Max;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ekol.crm.search.domain.DocumentType;
import ekol.crm.search.service.IndexingService;
import ekol.exceptions.BadRequestException;
import lombok.AllArgsConstructor;

@Validated
@RestController
@RequestMapping("/search/index")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class IndexController {

	private IndexingService indexingService;

    @GetMapping
    public String indexAll( 
    		@RequestParam(required = false, defaultValue = "10") @Max(20) Integer pageBlock,
    		@RequestParam(required = false, defaultValue = "50") @Max(100) Integer pageSize){
    	indexingService.indexImportedData(null, pageBlock, pageSize);
    	return "Imported data has been indexed successfully.";
    }
    
    @GetMapping("/{document}")
    public String index(@PathVariable DocumentType document, 
    					@RequestParam(required = false, defaultValue = "10") @Max(20) Integer pageBlock,
    					@RequestParam(required = false, defaultValue = "50") @Max(100) Integer pageSize){
        indexingService.indexImportedData(document, pageBlock, pageSize);
        return "Imported data has been indexed successfully.";
    }

    @GetMapping("/{document}/{id}")
    public String indexDocument(@PathVariable DocumentType document, @PathVariable Long id){
    	if(DocumentType.account == document) {
    		indexingService.indexAccount(id);
    	} else if(DocumentType.quote == document) {
        	indexingService.indexQuote(id);
    	} else if(DocumentType.agreement == document) {
    		indexingService.indexAgreement(id);
    	} else if(DocumentType.opportunity == document){
    		indexingService.indexOpportunity(id);
		} else {
    		throw new BadRequestException("Invalid document");
    	}
    	return String.format("%s data has been indexed successfully.", document.name());
    }
    
    @DeleteMapping
    public String deleteIndex() {
    	indexingService.deleteIndex();
    	return "Index is deleted";
    }
    
    @DeleteMapping("/{document}/{id}")
    public String deleteDocument(@PathVariable DocumentType document, @PathVariable Long id) {
    	return indexingService.deleteDocument(id, document);
    }
}
