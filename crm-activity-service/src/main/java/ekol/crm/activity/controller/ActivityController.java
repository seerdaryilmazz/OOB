package ekol.crm.activity.controller;

import java.util.*;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import ekol.crm.activity.domain.*;
import ekol.crm.activity.domain.dto.*;
import ekol.crm.activity.service.*;
import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/activity")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ActivityController {

    private ActivityService activityService;
    private ActivityServiceBulkJobs activityServiceBulkJobs;

    @PostMapping
    @Timed(value = "crm-activity.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    public ActivityJson createActivity(@RequestBody ActivityJson request, @RequestParam(required = false) Map<String, String> parameters) {
        request.validate();
        request.setActivityAttributes(parameters);
        return ActivityJson.fromEntity(activityService.save(request.toEntity()));
    }

    @PutMapping("/{id}")
    @Timed(value = "crm-activity.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    public ActivityJson updateActivity(@PathVariable String id, @RequestBody ActivityJson request) {
        request.validate();
        return ActivityJson.fromEntity(activityService.save(id, request.toEntity()));
    }
    
    @PatchMapping("/{id}/status/{status}")
    public ActivityJson updateStatus(@PathVariable String id, @PathVariable ActivityStatus status) {
    	Activity entity = activityService.getByActivityId(id);
		entity.setStatus(status);
    	return ActivityJson.fromEntity(activityService.save(id, entity));
    }

    @PutMapping("/{activiyId}/notes")
    public List<NoteJson> updateNotes(@PathVariable String activiyId, @RequestBody List<NoteJson> request) {
        return activityService.saveNotes(activiyId, request).stream().map(NoteJson::fromEntity).collect(Collectors.toList());
    }

    @PutMapping("/{activiyId}/documents")
    public List<DocumentJson> updateDocuments(@PathVariable String activiyId, @RequestBody List<DocumentJson> request) {
        return activityService.saveDocuments(activiyId, request).stream().map(DocumentJson::fromEntity).collect(Collectors.toList());
    }

    @GetMapping
    @Timed(value = "crm-activity.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    public Page<ActivityJson> listActivities(@RequestParam Integer size, @RequestParam Integer page) {

        Pageable pageRequest = new PageRequest(page, size);

        Page<Activity> entityPage = activityService.list(pageRequest);

        List<ActivityJson> accounts = entityPage.getContent().stream().map(ActivityJson::fromEntity).collect(Collectors.toList());
        return new PageImpl<>(accounts, pageRequest, entityPage.getTotalElements());
    }

    @GetMapping("/byAccount/{accountId}")
    @Timed(value = "crm-activity.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    public List<ActivityJson> retrieveActivitiesByAccount(@PathVariable Long accountId, @RequestParam String serviceArea) {
        List<Activity> activities = activityService.getByAccountId(accountId);
        return activities.stream()
                .filter(p -> p.getServiceAreas().stream().anyMatch(area -> area.getCode().equalsIgnoreCase(serviceArea)))
                .map(ActivityJson::fromEntity)
                .collect(Collectors.toList());
    }
    @GetMapping("/actives/byAccount/{accountId}")
    @Timed(value = "crm-activity.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    public List<ActivityJson> retrieveActiveActivitiesByAccount(@PathVariable Long accountId) {
        List<Activity> activities = activityService.getByAccountId(accountId);
        return activities.stream()
                .filter(p -> ActivityStatus.CANCELED != p.getStatus())
                .map(ActivityJson::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Timed(value = "crm-activity.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    public ActivityJson retrieveActivityById(@PathVariable String id) {
        return ActivityJson.fromEntity(activityService.getByActivityId(id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        activityService.delete(id);
    }
    
    @PostMapping("/validate-calendar")
    public void validateCalendar(@RequestBody ActivityJson request) {
    	activityService.validateCalendar(request.toEntity());
    }

    @GetMapping("/search")
    @Timed(value = "crm-activity.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    public Page<ActivityJson> search(@Valid ActivitySearchJson search) {
        Page<Activity> activityPage = activityService.search(search);
        return activityService.convertToActivityJsonPage(activityPage);
    }

    @GetMapping("/homePageStatistics")
    @Timed(value = "crm-activity.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    public HomePageStatistics getHomePageStatistics() {
        return activityService.getHomePageStatistics();
    }

    /**
     * Normalde GET yerine POST kullanmak daha doğru ancak tarayıcıdan çağırılacağı için GET olması kolaylık sağlıyor.
     */
    @GetMapping("/snapshot")
    public String createActivitySnapshot() {
    	activityServiceBulkJobs.createActivitySnapshot();
        return "activity snapshot triggered";
    }
    
    @GetMapping("/fill-activity-internal-participants-usernames")
    public String fillActivityInternalParticipantsUsernames() {
    	activityServiceBulkJobs.updateCalenderIndividualParticipantUsername();
    	return "fill-activity-internal-participants-usernames triggered";
    }
}
