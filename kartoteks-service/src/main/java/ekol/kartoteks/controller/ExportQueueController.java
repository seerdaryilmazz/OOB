package ekol.kartoteks.controller;

import ekol.event.auth.Authorize;
import ekol.kartoteks.domain.CompanyExportQueue;
import ekol.kartoteks.domain.CompanyExportQueueFilter;
import ekol.kartoteks.domain.ExportQueueStatus;
import ekol.kartoteks.repository.CompanyExportQueueCustomRepository;
import ekol.kartoteks.service.CompanyDataExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by kilimci on 11/12/2017.
 */
@RestController
@RequestMapping("/export-queue")
public class ExportQueueController {

    @Autowired
    private CompanyExportQueueCustomRepository companyExportQueueRepository;

    @Autowired
    private CompanyDataExportService companyDataExportService;

    @Authorize(operations = "kartoteks.export-queue.list")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<CompanyExportQueue> list(@RequestParam(value="status",required = false) ExportQueueStatus status,
                                         @RequestParam(value="companyId" , required = false) Long companyId,
                                         @RequestParam(value="applicationCompanyId" , required = false) String applicationCompanyId,
                                         @RequestParam(value="page" , required = false) Integer page,
                                         @RequestParam(value="size" , required = false) Integer size){
        return companyExportQueueRepository.searchWithFilter(
                CompanyExportQueueFilter.with(status, companyId, applicationCompanyId, page, size)
        );
    }

    @Authorize(operations = "kartoteks.export-queue.execute")
    @RequestMapping(value = "/{exportQueueId}/execute", method = RequestMethod.POST)
    public void execute(@PathVariable Long exportQueueId){
        companyDataExportService.execute(exportQueueId);
    }
}
