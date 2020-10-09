package ekol.crm.activity.service;

import java.time.*;
import java.util.*;
import java.util.stream.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import ekol.crm.activity.client.UserServiceClient;
import ekol.crm.activity.common.*;
import ekol.crm.activity.domain.*;
import ekol.crm.activity.domain.Calendar;
import ekol.crm.activity.domain.dto.*;
import ekol.crm.activity.event.dto.*;
import ekol.crm.activity.repository.ActivityRepository;
import ekol.crm.activity.util.Utils;
import ekol.crm.activity.validator.ActivityValidator;
import ekol.exceptions.*;
import ekol.model.*;
import ekol.resource.oauth2.SessionOwner;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ActivityService {

    private ActivityRepository repository;
    private ActivityValidator validator;
    private ApplicationEventPublisher applicationEventPublisher;
    private StringRedisTemplate redisTemplate;
    private SessionOwner sessionOwner;
    private MongoTemplate mongoTemplate;
    private UserServiceClient userServiceClient;

    private static final String OUTLOOK_SOURCE_KEY = "increment_OutlookSourceId";
    private static final int OUTLOOK_SOURCE_KEY_INCREMENT_START = 10000;
    

    private Long produceOutlookSourceId(){
        redisTemplate.opsForValue().setIfAbsent(OUTLOOK_SOURCE_KEY, String.valueOf(OUTLOOK_SOURCE_KEY_INCREMENT_START));
        redisTemplate.opsForValue().increment(OUTLOOK_SOURCE_KEY, 1);
        return Long.valueOf(redisTemplate.opsForValue().get(OUTLOOK_SOURCE_KEY));
    }

    public Page<Activity> list(Pageable pageRequest) {
        return repository.findAllByDeletedFalse(pageRequest);
    }
	public Iterable<Activity> listAll(Set<String> ids){
		return repository.findAll(ids);
	}

    @Transactional
    public Activity save(String id, Activity activity){
    	Activity existedActivity = getByActivityId(id);

    	activity.setId(id);
    	activity.setVersion(existedActivity.getVersion());
    	activity.setCreatedAt(existedActivity.getCreatedAt());
    	activity.setCreatedBy(existedActivity.getCreatedBy());
        activity.getActivityAttributes().putAll(existedActivity.getActivityAttributes());
        Activity savedActivity = createOrUpdateActivity(activity);
    	
		applicationEventPublisher.publishEvent(ActivityEvent.with(ActivityChange.with(id, ActivityJson.fromEntity(existedActivity), ActivityJson.fromEntity(savedActivity)), Operation.UPDATE));
    	
    	return savedActivity;
    }
    
	@Transactional
	public void updateAccount(Collection<String> ids, IdNamePair account) {
		Iterable<Activity> activities = repository.findAll(ids);
		activities.forEach(activity->{
			activity.setAccount(account);
			Activity savedActivity = repository.save(activity);
			applicationEventPublisher.publishEvent(ActivityEvent.with(savedActivity, Operation.UPDATE));
		});
	}
    
    @Transactional
    public Activity save(Activity activity){
    	activity.setStatus(determineStatus(activity));
    	Activity savedActivity = createOrUpdateActivity(activity);
    	applicationEventPublisher.publishEvent(ActivityEvent.with(savedActivity, Operation.CREATE));
    	return savedActivity;
    }
    
	public Activity createOrUpdateActivity(Activity activity) {
		validator.validate(activity);
		if (activity.getCalendar() != null && activity.getCalendar().getCalendarId() == null) {
			activity.getCalendar().setCalendarId(produceOutlookSourceId());
		}
		/*
		 * It must be used to save activity. Closed temporarily for DEMO.
		 */

		Activity savedActivity = repository.save(activity);
		Optional.of(savedActivity).filter(act-> ActivityStatus.OPEN == act.getStatus() || ActivityStatus.CANCELED == act.getStatus()).map(Activity::getCalendar).ifPresent(calendar->{
			if(calendar.isOnlyWithOrganizer() || calendar.isShareWithAll()) {
				applicationEventPublisher.publishEvent(ActivityEvent.with(savedActivity, Operation.OUTLOOK_EVENT));
			}
		});
		return savedActivity;
	}

    @Transactional
    public void delete(String id) {
    	Activity activity = getByActivityId(id);
    	if (activity.isDeleted()) {
    		throw new BadRequestException("Activity is already deleted.");
    	} else {
    		User currentUser = sessionOwner.getCurrentUser();
    		if(Objects.equals(activity.getCreatedBy(), currentUser.getUsername())) {
    			activity.setDeleted(true);
    			activity = repository.save(activity);
    			applicationEventPublisher.publishEvent(ActivityEvent.with(activity, Operation.DELETE));
    		} else {
    			activity.setDeleted(false);
    			throw new BadRequestException("Activity can not be deleted.");
    		}
    	}
    }

    public Activity getByActivityId(String id) {

        return Optional.ofNullable(repository.findById(id))
                .orElseThrow(() -> new ResourceNotFoundException("Activity with id {0} not found", id));
    }


    public List<Activity> getByAccountId(Long accountId) {
        return repository.findByAccountIdAndDeletedFalseOrderByCreatedAtDesc(accountId);
    }

    /**
     * @return Bir activity kaydının, belirtilen kullanıcı ile ilgili olup olmadığını tespit etmek için kullanacağımız criteria nesnesini döner.
     */
    public Criteria getRelatedToUserCriteria(User user) {
    	Criteria usernameCrit = Criteria.where("createdBy").is(user.getUsername());
        if(StringUtils.isNotBlank(user.getEmail())) {
        	return new Criteria().orOperator(usernameCrit, Criteria.where("calendar.internalParticipants.emailAddress").in(user.getEmail()));
        }
        return usernameCrit;

    }

    public Page<Activity> search(ActivitySearchJson search) {
    	
        User user = Optional.of(search)
        		.map(ActivitySearchJson::getUsername)
        		.filter(StringUtils::isNotEmpty)
        		.map(userServiceClient::getUserDetail)
        		.orElseGet(sessionOwner::getCurrentUser);

        ZoneId zoneId = ZoneId.of(user.getTimeZoneId());
        List<Criteria> firstLevelCriteriaList = new ArrayList<>();

        firstLevelCriteriaList.add(Criteria.where("deleted").is(false));
        if(Objects.nonNull(search.getUsername())) {
        	firstLevelCriteriaList.add(getRelatedToUserCriteria(user));
        }
        
        Optional.ofNullable(search.getAccountId())
        	.ifPresent(crit->firstLevelCriteriaList.add(Criteria.where("account.id").is(crit)));
        
        Optional.ofNullable(search.getScopeCode())
        	.filter(StringUtils::isNoneEmpty)
        	.ifPresent(crit->firstLevelCriteriaList.add(Criteria.where("scope.code").is(crit)));
        
        Optional.ofNullable(search.getToolCode())
        	.filter(StringUtils::isNoneEmpty)
        	.ifPresent(crit->firstLevelCriteriaList.add(Criteria.where("tool.code").is(crit)));

        if(Optional.ofNullable(search.getStatusCode()).isPresent()){
            firstLevelCriteriaList.add(Criteria.where("status").is(ActivityStatus.valueOf(search.getStatusCode())));
        }else {
            // MongoDB'de isNot veya not equal anlamında "ne" kullanılıyor.
            firstLevelCriteriaList.add(Criteria.where("status").ne(ActivityStatus.CANCELED));
        }
        Optional.ofNullable(search.getStatusCode())
	        .filter(StringUtils::isNoneEmpty)
	        .ifPresent(crit->firstLevelCriteriaList.add(Criteria.where("status").is(ActivityStatus.valueOf(crit))));
        
        Optional.ofNullable(search.getServiceAreaCode())
        	.filter(StringUtils::isNoneEmpty)
        	.ifPresent(crit->firstLevelCriteriaList.add(Criteria.where("serviceAreas.code").is(crit)));

        Optional.ofNullable(search.getActivityAttributeKey())
                .filter(StringUtils::isNotEmpty)
                .ifPresent(key-> Optional.ofNullable(search.getActivityAttributeValue())
                        .filter(StringUtils::isNotEmpty)
                        .ifPresent(value-> firstLevelCriteriaList.add(Criteria.where(("activityAttributes."+key)).is(value))));
        
        Instant minStartDateInstant = Optional.ofNullable(search.getMinStartDate())
        	.map(FixedZoneDateTimeDeserializer::deserialize)
        	.map(FixedZoneDateTime::toInstant)
        	.orElse(null);
        
        Instant maxStartDateInstant = Optional.ofNullable(search.getMaxStartDate())
        		.map(FixedZoneDateTimeDeserializer::deserialize)
        		.map(FixedZoneDateTime::toInstant)
        		.orElse(null);
        
        if (minStartDateInstant != null && maxStartDateInstant != null) {
            firstLevelCriteriaList.add(Criteria.where("calendar.startDate.dateTimeUtc").gte(minStartDateInstant).lte(maxStartDateInstant));
        } else if (minStartDateInstant != null) {
            firstLevelCriteriaList.add(Criteria.where("calendar.startDate.dateTimeUtc").gte(minStartDateInstant));
        } else if (maxStartDateInstant != null) {
            firstLevelCriteriaList.add(Criteria.where("calendar.startDate.dateTimeUtc").lte(maxStartDateInstant));
        }
        
        Instant minCreationDateInstant = Optional.ofNullable(search.getMinCreationDate())
            	.map(Utils::deserializeLocalDateStr)
            	.map(t->ZonedDateTime.of(t, LocalTime.MIN, zoneId).toInstant())
            	.orElse(null);
            
        Instant maxCreationDateInstant = Optional.ofNullable(search.getMaxCreationDate())
        		.map(Utils::deserializeLocalDateStr)
        		.map(t->ZonedDateTime.of(t, LocalTime.MAX, zoneId).toInstant())
        		.orElse(null);

        if (minCreationDateInstant != null && maxCreationDateInstant != null) {
            firstLevelCriteriaList.add(Criteria.where("createdAt").gte(minCreationDateInstant).lte(maxCreationDateInstant));
        } else if (minCreationDateInstant != null) {
            firstLevelCriteriaList.add(Criteria.where("createdAt").gte(minCreationDateInstant));
        } else if (maxCreationDateInstant != null) {
            firstLevelCriteriaList.add(Criteria.where("createdAt").lte(maxCreationDateInstant));
        }

        Criteria mainCriteria = new Criteria();
        mainCriteria.andOperator(firstLevelCriteriaList.toArray(new Criteria[firstLevelCriteriaList.size()]));

        Pageable pageable = new PageRequest(search.getPage(), search.getPageSize(), Sort.Direction.DESC, "createdAt");
        List<Activity> requestedPageData = mongoTemplate.find(new Query(mainCriteria).with(pageable), Activity.class);
        long total = mongoTemplate.count(new Query(mainCriteria), Activity.class);

        return new PageImpl<>(requestedPageData, pageable, total);
    }

    public Page<ActivityJson> convertToActivityJsonPage(Page<Activity> activityPage) {
        List<ActivityJson> activityJsons = activityPage.getContent().stream().map(ActivityJson::fromEntity).collect(Collectors.toList());
        return new PageImpl<>(activityJsons, new PageRequest(activityPage.getNumber(), activityPage.getSize()), activityPage.getTotalElements());
    }

    public HomePageStatistics getHomePageStatistics() {

        User currentUser = sessionOwner.getCurrentUser();

        List<Criteria> firstLevelCriteriaList = new ArrayList<>();
        firstLevelCriteriaList.add(Criteria.where("deleted").is(false));
        
        firstLevelCriteriaList.add(getRelatedToUserCriteria(currentUser));
        
        firstLevelCriteriaList.add(Criteria.where("status").ne(ActivityStatus.CANCELED));

        Criteria mainCriteria = new Criteria();
        mainCriteria.andOperator(firstLevelCriteriaList.toArray(new Criteria[firstLevelCriteriaList.size()]));

        MatchOperation matchOperation = Aggregation.match(mainCriteria);
        GroupOperation groupOperation = Aggregation.group("scope.code").count().as("count");
        Aggregation aggregation = Aggregation.newAggregation(matchOperation, groupOperation);
        AggregationResults<CodeCountPair> aggregationResults = mongoTemplate.aggregate(aggregation, Activity.class, CodeCountPair.class);
        List<CodeCountPair> rows = aggregationResults.getMappedResults();
        long totalCount = 0;
        List<ActivityScopeBasedCount> activityScopeBasedCounts = new ArrayList<>();

        for (CodeCountPair row : rows) {
            ActivityScope activityScope = ActivityScope.valueOf(row.getCode());
            activityScopeBasedCounts.add(new ActivityScopeBasedCount(activityScope, row.getCount()));
            totalCount += row.getCount();
        }

        return new HomePageStatistics(totalCount, activityScopeBasedCounts);
    }

    @Transactional
    public List<Note> saveNotes(String activityId, List<NoteJson> request){

        Activity activity = getByActivityId(activityId);
        List<Note> notes = new ArrayList<>();
        if(!CollectionUtils.isEmpty(request)){
            notes.addAll(request.stream().map(NoteJson::toEntity).collect(Collectors.toList()));
        }
        List<Note> existingNotes = activity.getNotes();

        if(!CollectionUtils.isEmpty(existingNotes)){
            Set<String> noteIds = notes.stream().map(Note::getNoteId).collect(Collectors.toSet());
            existingNotes.stream()
                    .filter(c -> !noteIds.contains(c.getNoteId())).collect(Collectors.toList())
                    .forEach(note -> {
                        note.setDeleted(true);
                        notes.add(note);
                    });
        }
        activity.setNotes(notes);
        Activity savedActivity = repository.save(activity);

        return savedActivity.getNotes().stream()
                .filter(note -> !note.isDeleted())
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Document> saveDocuments(String activityId, List<DocumentJson> request){

        Activity activity = getByActivityId(activityId);
        List<Document> documents = new ArrayList<>();
        if(!CollectionUtils.isEmpty(request)){
            documents.addAll(request.stream().map(DocumentJson::toEntity).collect(Collectors.toList()));
        }
        List<Document> existingDocuments = activity.getDocuments();

        if(!CollectionUtils.isEmpty(existingDocuments)){
            Set<String> documentIds = documents.stream().map(Document::getDocumentId).collect(Collectors.toSet());
            existingDocuments.stream()
                    .filter(c -> !documentIds.contains(c.getDocumentId())).collect(Collectors.toList())
                    .forEach(document -> {
                        document.setDeleted(true);
                        documents.add(document);
                    });
        }
        activity.setDocuments(documents);
        Activity savedActivity = repository.save(activity);

        return savedActivity.getDocuments().stream()
                .filter(document -> !document.isDeleted())
                .collect(Collectors.toList());
    }

	public void validateCalendar(Activity entity) {
		Optional<Activity> activityOptional = Optional.of(entity);
		if(activityOptional.map(Activity::getCalendar).map(Calendar::getEndDate).isPresent()) {
			if(activityOptional.map(Activity::getCalendar).map(Calendar::getStatus).filter(CalendarStatus.EXPIRED::equals).isPresent()) {
				throw new ValidationException("Activity end date is a past date. Activity status will be \"{0}\". Are you sure?", ActivityStatus.COMPLETED.name());
			}
		}
	}
	
	private ActivityStatus determineStatus(Activity entity) {
		Optional<Activity> activityOptional = Optional.of(entity);
		if(activityOptional.map(Activity::getTool).map(CodeNamePair::getCode).map(ActivityTool::valueOf).filter(ActivityTool.E_MAIL::equals).isPresent()
				|| activityOptional.map(Activity::getCalendar).map(Calendar::getStatus).filter(CalendarStatus.EXPIRED::equals).isPresent()) {
			return ActivityStatus.COMPLETED;
		} else if(Objects.isNull(entity.getStatus())) {
			return ActivityStatus.OPEN;
		}
		return entity.getStatus();
	}
	
	public void updateActivityParticipantsName(Contact contact) {
		List<Activity> activities = mongoTemplate.find(new Query(Criteria.where("calendar.externalParticipants.id").in(contact.getCompanyContactId())), Activity.class);
		activities.forEach(activity->{
			Optional.of(activity)
				.map(Activity::getCalendar)
				.map(Calendar::getExternalParticipants)
				.map(Collection::stream)
				.orElseGet(Stream::empty)
				.forEach(participant->{
					if(Objects.equals(participant.getId(), contact.getCompanyContactId())) {
						participant.setName(contact.getFullname());
					}
				});
		});
		repository.save(activities);
	}

}



