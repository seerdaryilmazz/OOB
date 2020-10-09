package ekol.crm.activity.service;

import java.sql.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import ekol.crm.activity.client.UserServiceClient;
import ekol.crm.activity.common.FixedZoneDateTime;
import ekol.crm.activity.domain.*;
import ekol.crm.activity.domain.Calendar;
import ekol.crm.activity.repository.ActivityRepository;
import ekol.crm.activity.util.Utils;
import ekol.exceptions.ApplicationException;
import ekol.model.*;

@Service
public class ActivityServiceBulkJobs {
	
	private static final Logger logger = LoggerFactory.getLogger(ActivityServiceBulkJobs.class);
	
	@Autowired
	private ActivityRepository repository;
	
	@Autowired
	private UserServiceClient userServiceClient;
	
	@Value("${oneorder.datasource.driverClassName}") 
	String oneorderDatasourceDriverClassName;
    
	@Value("${oneorder.datasource.jdbcUrl}") 
    String oneorderDatasourceJdbcUrl;
    
    @Value("${oneorder.datasource.username}") 
    String oneorderDatasourceUsername;
    
    @Value("${oneorder.datasource.password}") 
    String oneorderDatasourcePassword;

    @Async
    public void createActivitySnapshot() {

        logger.info("activity snapshot started");

        Instant snapshotInstant = Instant.now();
        java.util.Calendar calendarForTargetTimezoneConfig = java.util.Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Connection connection = null;
        PreparedStatement ps = null;
        int pageIndex = 0;
        int pageSize = 500;
        
        try {

            Class.forName(oneorderDatasourceDriverClassName);
            connection = DriverManager.getConnection(oneorderDatasourceJdbcUrl, oneorderDatasourceUsername, oneorderDatasourcePassword);
            connection.setAutoCommit(false);
            
            if (Utils.doesTableExist(connection, "CRM_ACTIVITY_SNAPSHOT")) {
                ps = connection.prepareStatement("truncate table crm_activity_snapshot");
                ps.executeUpdate();
                ps.close();
            } else {
	            String createTableSql =
	                    "create table crm_activity_snapshot ( " +
	                            "id varchar2(24) not null, " +
	                            "account_id number(19,0), " +
	                            "account_name varchar2(500), " +
	                            "service_areas varchar2(500), " +
	                            "scope_code varchar2(100), " +
	                            "scope_name varchar2(100), " +
	                            "tool_code varchar2(100), " +
	                            "tool_name varchar2(100), " +
	                            "type_code varchar2(100), " +
	                            "type_name varchar2(100), " +
	                            "calendar_start_date timestamp(6), " +
	                            "calendar_end_date timestamp(6), " +
	                            "calendar_subject varchar2(1000), " +
	                            "created_at timestamp(6), " +
	                            "created_by varchar2(100), " +
	                            "last_updated timestamp(6), " +
	                            "last_updated_by varchar2(100), " +
	                            "snapshot_time timestamp(6), " +
	                            "constraint pk_crm_activity_snapshot primary key (id))";
	
	            ps = connection.prepareStatement(createTableSql);
	            ps.executeUpdate();
	            ps.close();
            }
            
			List<CodeNamePair> newColumns = Arrays.asList(
					CodeNamePair.with("internal_participants", "varchar2(1000)"),
					CodeNamePair.with("status", "varchar2(32)")
				);
            
			for (Iterator<CodeNamePair> iterator = newColumns.iterator(); iterator.hasNext();) {
				CodeNamePair column = iterator.next();
				if(!Utils.doesColumnExist(connection, "CRM_ACTIVITY_SNAPSHOT", column.getCode().toUpperCase())) {
					ps = connection.prepareStatement(Utils.addColumnStatement("crm_activity_snapshot", column.getCode(), column.getName()));
					ps.executeUpdate();
					ps.close();
				}
				
			}

            ps = connection.prepareStatement("insert into crm_activity_snapshot " +
                    "(id, account_id, account_name, service_areas, scope_code, scope_name, tool_code, tool_name, type_code, type_name, " +
                    "calendar_start_date, calendar_end_date, calendar_subject, created_at, created_by, last_updated, last_updated_by, snapshot_time, internal_participants, status) " +
                    "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            while (true) {
                
                Pageable pageable = new PageRequest(pageIndex, pageSize);
                Page<Activity> page = repository.findAllByDeletedFalse(pageable);
                
                if (!CollectionUtils.isEmpty(page.getContent())) {
                    
                    for (Activity activity : page.getContent()) {

                    	Timestamp calendarStartDate = Optional.of(activity)
                    			.map(Activity::getCalendar)
                    			.map(Calendar::getStartDate)
                    			.map(FixedZoneDateTime::getDateTimeUtc)
                    			.map(Utils::convertInstantToTimestamp)
                    			.orElse(null);
                    	
                    	Timestamp calendarEndDate = Optional.of(activity)
                    			.map(Activity::getCalendar)
                    			.map(Calendar::getEndDate)
                    			.map(FixedZoneDateTime::getDateTimeUtc)
                    			.map(Utils::convertInstantToTimestamp)
                    			.orElse(null);
                    	
                    	String internalParticipants = Optional.of(activity).map(Activity::getCalendar)
                        		.map(Calendar::getInternalParticipants)
                        		.orElseGet(ArrayList::new).stream()
                        		.map(Individual::getUsername)
                        		.filter(StringUtils::isNotEmpty)
                        		.collect(Collectors.joining(","));
                        
                        Utils.setParameter(ps,1, activity.getId());
                        Utils.setParameter(ps,2, Optional.of(activity).map(Activity::getAccount).map(IdNamePair::getId).orElse(null));
                        Utils.setParameter(ps,3, Optional.of(activity).map(Activity::getAccount).map(IdNamePair::getName).orElse(null));
                        Utils.setParameter(ps,4, Optional.of(activity).map(Activity::getServiceAreas).map(Collection::stream).orElseGet(Stream::empty).map(CodeNamePair::getCode).collect(Collectors.joining(",")));
                        Utils.setParameter(ps,5, Optional.of(activity).map(Activity::getScope).map(CodeNamePair::getCode).orElse(null));
                        Utils.setParameter(ps,6, Optional.of(activity).map(Activity::getScope).map(CodeNamePair::getName).orElse(null));
                        Utils.setParameter(ps,7, Optional.of(activity).map(Activity::getTool).map(CodeNamePair::getCode).orElse(null));
                        Utils.setParameter(ps,8, Optional.of(activity).map(Activity::getTool).map(CodeNamePair::getName).orElse(null));
                        Utils.setParameter(ps,9, Optional.of(activity).map(Activity::getType).map(CodeNamePair::getCode).orElse(null));
                        Utils.setParameter(ps,10, Optional.of(activity).map(Activity::getType).map(CodeNamePair::getName).orElse(null));
                        Utils.setParameter(ps,11, calendarStartDate, calendarForTargetTimezoneConfig);
                        Utils.setParameter(ps,12, calendarEndDate, calendarForTargetTimezoneConfig);
                        Utils.setParameter(ps,13, Optional.of(activity).map(Activity::getCalendar).map(Calendar::getSubject).orElse(null));
                        Utils.setParameter(ps,14, Optional.of(activity).map(Activity::getCreatedAt).map(Timestamp::valueOf).orElse(null), calendarForTargetTimezoneConfig);
                        Utils.setParameter(ps,15, activity.getCreatedBy());
                        Utils.setParameter(ps,16, Optional.of(activity).map(Activity::getLastUpdated).map(Timestamp::valueOf).orElse(null), calendarForTargetTimezoneConfig);
                        Utils.setParameter(ps,17, activity.getLastUpdatedBy());
                        Utils.setParameter(ps,18, Timestamp.from(snapshotInstant), calendarForTargetTimezoneConfig);
                        Utils.setParameter(ps,19, internalParticipants);
                        Utils.setParameter(ps,20, Optional.of(activity).map(Activity::getStatus).map(ActivityStatus::name).orElse(getDefaultActivityStatusName(activity)));

                        ps.executeUpdate();
                    }
                    
                    connection.commit();
                    pageIndex++;
                    
                } else {
                    break;
                }
            }

            connection.commit();
            ps.close();
            connection.close();
            
        } catch (Exception e) {
            Utils.rollbackQuietly(connection);
            Utils.closeQuietly(ps);
            Utils.closeQuietly(connection);
            throw new ApplicationException("", e);
        } finally {
            logger.info("activity snapshot finished");
        }
    }
    
    private String getDefaultActivityStatusName(Activity activity) {
    	ActivityStatus status = ActivityStatus.COMPLETED;
    	if(Optional.of(activity)
    		.map(Activity::getCalendar)
    		.map(Calendar::getEndDate)
    		.map(FixedZoneDateTime::getDateTimeUtc)
    		.filter(ZonedDateTime.now(ZoneId.of("UTC")).toInstant()::isBefore)
    		.isPresent()) {
    		status = ActivityStatus.OPEN;
    	}
    	return status.name();
    	
    }
    
    public void updateCalenderIndividualParticipantUsername() {
    	final int pageSize = 100;
    	int page = 0;
    	Page<Activity> result = null;
    	
    	User[] users = userServiceClient.searchUsers("ACTIVE_DIRECTORY", Boolean.TRUE);
    	Map<String, String> usersMapEmailKey = Arrays.stream(users).collect(Collectors.toMap(User::getEmail, User::getUsername, (x, y)->x));
    	Map<String, User> usersMapUserNameKey = Arrays.stream(users).collect(Collectors.toMap(User::getUsername, p->p, (x, y)->x));
    	
    	do {
    		Pageable pageable = new PageRequest(page, pageSize);
    		result = repository.findAllByDeletedFalseAndCalendarNotNull(pageable); 
    		List<Activity> activities = result.getContent();
    		activities.forEach(activity->{
    			List<Individual> participants = Optional.ofNullable(activity.getCalendar().getInternalParticipants()).orElseGet(ArrayList::new);
    			participants.forEach(t->t.setUsername(usersMapEmailKey.get(t.getEmailAddress())));
    			if(participants.stream().noneMatch(t->Objects.equals(t.getUsername(), activity.getCreatedBy()))) {
    				Optional.ofNullable(usersMapUserNameKey.get(activity.getCreatedBy())).ifPresent(u->participants.add(Individual.builder().id(u.getId()).name(u.getDisplayName()).username(u.getUsername()).emailAddress(u.getEmail()).build()));
    			}
    			activity.getCalendar().setInternalParticipants(participants);
    			repository.save(activity);
    		});
    		page += 1;
    	} while(result.hasNext());
    }

}
