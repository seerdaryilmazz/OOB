package ekol.kartoteks.event;

import java.util.Objects;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.event.annotation.ConsumesWebEvent;
import ekol.kartoteks.domain.*;
import ekol.kartoteks.domain.dto.AccountJson;
import ekol.kartoteks.repository.*;
import ekol.kartoteks.service.*;
import lombok.AllArgsConstructor;

/**
 * Created by kilimci on 07/12/2017.
 */
@RestController
@RequestMapping("/event-consumer")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EventConsumerController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EventConsumerController.class);

    private CompanyDataExportService companyDataExportService;
    private CompanyDeleteService companyDeleteService;
    private CompanyRepository companyRepository;
    private CompanyLocationRepository companyLocationRepository;

    @PostMapping("/company-export")
    @ConsumesWebEvent(event = "company-export", name = "export company data to external systems")
    public void consumeCompanyExport(@RequestBody CompanyExportEventMessage message){
        companyDataExportService.processQueueItem(message.getId());
    }
    @PostMapping("/company-retry-export")
    @ConsumesWebEvent(event = "company-retry-export", delayed = true, name = "retry company export error")
    public void consumeCompanyExportError(@RequestBody CompanyExportEventMessage message){
        companyDataExportService.processQueueItem(message.getId());
    }

    @PostMapping("/force-company-export")
    @ConsumesWebEvent(event = "force-company-export", name = "force company export in kartoteks-service")
    public void consumeForceCompanyExportEvent(@RequestBody ForceCompanyExportEventMessage message) {
        Company company = companyRepository.findOne(message.getCompanyId());
        if (company != null) {
            companyDataExportService.export(company, true);
        }
    }

    @PostMapping("/force-location-export")
    @ConsumesWebEvent(event = "force-location-export", name = "force location export in kartoteks-service")
    public void consumeForceLocationExportEvent(@RequestBody ForceLocationExportEventMessage message) {
        CompanyLocation location = companyLocationRepository.findOne(message.getLocationId());
        if (location != null) {
            companyDataExportService.export(location.getCompany(), location, true);
        }
    }

    @PostMapping("/account-owner-update")
    @ConsumesWebEvent(event = "account-owner-update", name = "export company belongs to updated account owner")
    public void consumeUpdatedAccount(@RequestBody AccountJson message) {
        Company company = companyRepository.findOne(message.getCompany().getId());
        if (company != null) {
            companyDataExportService.export(company, false);
        }
    }
    
    @PostMapping("/account-delete")
    @ConsumesWebEvent(event = "account-delete", name = "company delete by account delete")
    public void consumeAccountDelete(@RequestBody AccountJson message) {
    	Company company = companyRepository.findOne(message.getCompany().getId());
    	if(Objects.nonNull(company) && company.isTemp()) {
    		try {
    			companyDeleteService.delete(company);
    		} catch(Exception e) {
    			LOGGER.warn("company can not be deleted", e);
    		}
    	}
    }
}
