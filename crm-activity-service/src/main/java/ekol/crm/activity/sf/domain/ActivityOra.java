package ekol.crm.activity.sf.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;

import lombok.*;

@Data
@Builder
public class ActivityOra implements Serializable {
	private String sfActivityId;
	private String sfAccountId;
	private Long accountId;
	private Long companyId;
	private String activityId;
	private String[] serviceAreas;
	private String scopeCode;
	private String toolCode;
	private String typeCode;
	private ZonedDateTime calendarStartDate;
	private ZonedDateTime calendarEndDate;
	private String calendarSubject;
	private ZonedDateTime createdAt;
	private String createdBy;
	private String organizer;
	private String[] internalParticipants;
	private String[] externalParticipants;
	private String status;
	private String calendarContent;
	private String jobParam;
}
