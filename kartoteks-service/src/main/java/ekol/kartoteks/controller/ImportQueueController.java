package ekol.kartoteks.controller;


import ekol.event.auth.Authorize;
import ekol.exceptions.BadRequestException;
import ekol.kartoteks.domain.*;
import ekol.kartoteks.repository.CompanyImportQueueCustomRepository;
import ekol.kartoteks.repository.CompanyImportQueueRepository;
import ekol.kartoteks.service.CompanyExchangeDataConverter;
import ekol.kartoteks.service.ImportQueueProgressService;
import ekol.kartoteks.service.ImportQueueSaveService;
import ekol.resource.oauth2.SessionOwner;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Created by fatmaozyildirim on 4/7/16.
 */
@RestController
@RequestMapping("/import-queue")
public class ImportQueueController {

    @Autowired
    private CompanyImportQueueRepository importQueueRepository;

    @Autowired
    private CompanyImportQueueCustomRepository importQueueCustomRepository;

    @Autowired
    private ImportQueueSaveService importQueueSaveService;

    @Autowired
    private ImportQueueProgressService importQueueProgressService;

    @Autowired
    private CompanyExchangeDataConverter dataConverter;

    @Autowired
    private SessionOwner sessionOwner;

    @Timed(value = "kartoteks.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @Authorize(operations = "kartoteks.import-queue.insert")
    @RequestMapping(value="", method= RequestMethod.POST)
    public CompanyImportQueue add(@RequestBody CompanyImportQueue companyImportQueue){
        if(companyImportQueue == null) {
            throw new BadRequestException("queue data is null");
        }
        return importQueueSaveService.save(companyImportQueue);
    }

    @Authorize(operations = "kartoteks.import-queue.list")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<CompanyImportQueue> list(@RequestParam(value="status",required = false) ImportQueueStatus status,
                                         @RequestParam(value="startDate",required = false) String startDate,
                                         @RequestParam(value="endDate",required = false) String endDate,
                                         @RequestParam(value="companyName" , required = false) String companyName,
                                         @RequestParam(value="customerCompanyCode" , required = false) String customerCompanyCode,
                                         @RequestParam(value="orderCode" , required = false) String orderCode,
                                         @RequestParam(value="type" , required = false) String type,
                                         @RequestParam(value="page" , required = false) Integer page,
                                         @RequestParam(value="size" , required = false) Integer size){

        return importQueueCustomRepository.searchWithFilter(
                CompanyImportQueueFilter.withStatus(status)
                        .companyName(companyName)
                        .type(type)
                        .startDate(startDate)
                        .endDate(endDate)
                        .customerCompanyCode(customerCompanyCode)
                        .orderCode(orderCode)
                        .page(page != null ? page : 1)
                        .size(size != null ? size : 20));
    }
    private void checkQueueItem(Long queueId){
        if(!importQueueRepository.exists(queueId)){
            throw new BadRequestException("Queue item with id {0}, does not exist", queueId);
        }
    }
    @Authorize(operations = "kartoteks.import-queue.import-company")
    @RequestMapping(value = "/next-item",method = RequestMethod.GET)
    public CompanyImportQueue getNextItem(){
        return importQueueRepository.findFirstByStatus(ImportQueueStatus.PENDING);
    }
    @Authorize(operations = "kartoteks.import-queue.import-company")
    @RequestMapping(value = "/{queueId}",method = RequestMethod.GET)
    public CompanyImportQueue get(@PathVariable Long queueId){
        checkQueueItem(queueId);
        return importQueueRepository.findOne(queueId);
    }
    @Authorize(operations = "kartoteks.import-queue.import-company")
    @RequestMapping(value = "/{queueId}/company",method = RequestMethod.GET)
    public Company getCompanyFromQueue(@PathVariable Long queueId){
        checkQueueItem(queueId);
        CompanyImportQueue importQueue = importQueueRepository.findOne(queueId);
        return dataConverter.convertCompany(importQueue.getApplication(), importQueue.parseCompanyJson());
    }

    @Timed(value = "kartoteks.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @Authorize(operations = "kartoteks.company.merge-with-queue")
    @RequestMapping(value = "/{queueId}/complete",method = RequestMethod.PUT)
    public Company completeQueueItem(@PathVariable long queueId, @RequestBody Company company ){
        checkQueueItem(queueId);
        CompanyImportQueue queue = importQueueRepository.findOne(queueId);
        return importQueueSaveService.completeQueueAndSaveCompany(queue, company);
    }
    @Authorize(operations = "kartoteks.import-queue.list")
    @RequestMapping(value = "/today",method = RequestMethod.GET)
    public List<CompanyImportQueue> listToday(){
        return importQueueCustomRepository.searchWithFilter(
                CompanyImportQueueFilter.withStatus(ImportQueueStatus.PENDING).startDate(LocalDate.now()));

    }

    @Timed(value = "kartoteks.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @Authorize(operations = "kartoteks.import-queue.get-statistics")
    @RequestMapping(value="/burn-down", method = RequestMethod.GET)
    public Map<String, Long[]> burnDown(@RequestParam(required = false) Integer period){
        int minusDays = 10;
        if(period != null){
            minusDays = period;
        }
        LocalDateTime aMonthEarlier = LocalDate.now().minusDays(minusDays).atStartOfDay();
        List<CompanyImportQueue> pendingQueueItems = importQueueRepository.getPendingItemsFromCreateDate(
                aMonthEarlier);
        List<CompanyImportQueue> successQueueItems = importQueueRepository.getCompletedItemsFromDate(
                aMonthEarlier);

        Map<String, Long[]> result = new LinkedHashMap<>();
        Map<String, Long> pendingResult = pendingQueueItems.stream()
                .collect(Collectors.groupingBy(item -> item.getCreateDate().getDateTime().format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                        LinkedHashMap::new, Collectors.counting()));
        pendingResult.forEach((key, value) -> {
            if(result.containsKey(key)){
                result.get(key)[0] = value;
            } else {
                result.put(key, new Long[]{value, 0L});
            }
        });
        Map<String, Long> successResult = successQueueItems.stream()
                .collect(Collectors.groupingBy(item -> item.getCompletedDate().getDateTime().format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                        Collectors.counting()));
        successResult.forEach((key, value) -> {
            if(result.containsKey(key)){
                result.get(key)[1] = value;
            } else {
                result.put(key, new Long[]{0L, value});
            }
        });
        Map<String, Long[]> sorted = new LinkedHashMap<>();
        result.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(entry -> {
                    TemporalAccessor temp = DateTimeFormatter.ofPattern("yyyyMMdd").parse(entry.getKey());
                    String key = DateTimeFormatter.ofPattern("d MMM").format(temp);
                    sorted.put(key, entry.getValue());
                });

        return sorted;
    }
    @Timed(value = "kartoteks.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @Authorize(operations = "kartoteks.import-queue.list")
    @RequestMapping(value = "/top-priority",method = RequestMethod.GET)
    public List<CompanyImportQueue> listTopPriority(){
        return importQueueCustomRepository.searchWithFilter(
                new CompanyImportQueueFilter().status(ImportQueueStatus.PENDING).page(1).size(20));

    }

    @Timed(value = "kartoteks.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @Authorize(operations = "kartoteks.import-queue.get-statistics")
    @RequestMapping(value = "/stats",method = RequestMethod.GET)
    public Map<String, List<Object[]>> stats(){
        ZonedDateTime today = LocalDate.now().atStartOfDay().atZone(ZoneId.of("Europe/Istanbul"));
        Map<String, List<Object[]>> result = new LinkedHashMap<>();
        List<Object[]> todayCounts = importQueueRepository.getStatsByGreaterThanCreateDate(today.toLocalDateTime());
        result.put("TODAY", todayCounts);
        List<Object[]> allCounts = importQueueRepository.getAllStats();
        result.put("ALL", allCounts);

        return result;
    }

    @RequestMapping(value = "/{queueId}/start",method = RequestMethod.GET)
    public Map<String, Object> startQueueItem(@PathVariable long queueId){
        checkQueueItem(queueId);
        Map<String, Object> result = new HashMap<>();
        boolean success = importQueueProgressService.startImport(queueId);
        result.put("alreadyStarted", !success);
        if(!success){
            result.put("startedBy", importQueueProgressService.getStartedBy(queueId));
        }
        return result;
    }

    @RequestMapping(value = "/{queueId}/cancel",method = RequestMethod.GET)
    public void cancelQueueItem(@PathVariable long queueId){
        checkQueueItem(queueId);
        importQueueProgressService.finishImport(queueId);
    }

    @RequestMapping(value = "/last-import-queue-item-for-location", method = RequestMethod.GET)
    public CompanyImportQueue findLastImportQueueItemForLocation(@RequestParam String application, @RequestParam Long companyId, @RequestParam Long locationId) {
        return importQueueSaveService.findLastImportQueueItemForLocation(RemoteApplication.valueOf(application), companyId, locationId);
    }
}
