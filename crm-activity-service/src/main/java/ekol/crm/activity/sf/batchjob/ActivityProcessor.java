package ekol.crm.activity.sf.batchjob;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.*;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.*;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ekol.crm.activity.client.*;
import ekol.crm.activity.client.dto.*;
import ekol.crm.activity.client.dto.Contact.*;
import ekol.crm.activity.common.FixedZoneDateTime;
import ekol.crm.activity.domain.*;
import ekol.crm.activity.domain.Calendar;
import ekol.crm.activity.sf.domain.Activity;
import ekol.crm.activity.sf.domain.ActivityOra;
import ekol.json.serializers.common.*;
import ekol.model.*;
import ekol.resource.oauth2.SessionOwner;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ActivityProcessor implements ItemProcessor<ActivityOra, Activity> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ActivityProcessor.class);
	
	private static final EnumConverter converter = new InitialCase();

	private SessionOwner sessionOwner;
	private AccountServiceClient accountServiceClient;
	private UserServiceClient userServiceClient;
	private KartoteksServiceClient kartoteksServiceClient;

	@Override
	public Activity process(ActivityOra item) throws Exception {
		
		try {
			Account account = accountServiceClient.getAccount(item.getAccountId());
			CodeNamePair scope = CodeNamePair.with(item.getScopeCode(), converter.convert(ActivityScope.valueOf(item.getScopeCode())));
			CodeNamePair tool = CodeNamePair.with(item.getToolCode(), converter.convert(ActivityTool.valueOf(item.getToolCode())));
			CodeNamePair type = CodeNamePair.with(item.getTypeCode(), converter.convert(ActivityType.valueOf(item.getTypeCode())));
			List<CodeNamePair> serviceAreas = Stream.of(item.getServiceAreas()).map(t->CodeNamePair.with(t, t)).collect(Collectors.toList());

			return Activity.builder()
					.sfId(item.getSfActivityId())
					.account(IdNamePair.with(account.getId(), account.getName()))
					.createdAt(item.getCreatedAt().toLocalDateTime())
					.createdBy(item.getCreatedBy())
					.lastUpdated(LocalDateTime.now())
					.lastUpdatedBy(sessionOwner.getCurrentUser().getUsername())
					.version(0L)
					.deleted(false)
					.scope(scope)
					.tool(tool)
					.type(type)
					.serviceAreas(serviceAreas)
					.status(ActivityStatus.valueOf(item.getStatus()))
					.calendar(mapCalendar(item))
					.jobParam(item.getJobParam())
					.build();
		} catch(Exception e) {
			LOGGER.warn("Activity transfer process is faild. sfActivityId: " + item.getSfActivityId(), e);
			throw e;
		}
	}
	
	private Calendar mapCalendar(ActivityOra item) {
		Calendar calendar = new Calendar();
		calendar.setContent(item.getCalendarContent());
		calendar.setEndDate(new FixedZoneDateTime(item.getCalendarEndDate().toInstant(), item.getCalendarEndDate().getZone().getId()));
		calendar.setStartDate(new FixedZoneDateTime(item.getCalendarStartDate().toInstant(), item.getCalendarStartDate().getZone().getId()));
		calendar.setSubject(item.getCalendarSubject());
		calendar.setOrganizer(mapUser(item.getOrganizer()));
		calendar.setInternalParticipants(Stream.of(item.getInternalParticipants()).map(this::mapUser).collect(Collectors.toList()));
		calendar.setExternalParticipants(Stream.of(item.getExternalParticipants()).map(contact->this.mapContact(item.getCompanyId(), contact)).collect(Collectors.toList()));
		return calendar;
	}
	
	private Individual mapContact(Long companyId, String contactId) {
		Contact contact = kartoteksServiceClient.getContact(companyId, Long.valueOf(StringUtils.trim(contactId)));
		String emailAddress = Optional.ofNullable(contact.getEmails())
				.map(Collection::stream)
				.orElseGet(Stream::empty)
				.map(EmailType::getEmail)
				.map(Email::getEmailAddress)
				.findAny()
				.orElse(null);
		return Individual.builder()
				.id(contact.getId())
				.name(contact.getFullname())
				.emailAddress(emailAddress)
				.build();
	}
	
	private Individual mapUser(String username) {
		User user = userServiceClient.getUserDetail(username);
		return Individual.builder()
				.emailAddress(user.getEmail())
				.id(user.getId())
				.name(user.getDisplayName())
				.username(username)
				.build();
	}

}
