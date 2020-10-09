package ekol.crm.activity.sf.batchjob;

import java.sql.*;
import java.time.*;
import java.util.*;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.item.data.*;
import org.springframework.batch.item.database.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.jdbc.core.RowMapper;

import ekol.crm.activity.sf.domain.*;
import lombok.AllArgsConstructor;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ActivityJobConfiguration {

	private JobBuilderFactory jobBuilderFactory;
	private StepBuilderFactory stepBuilderFactory;
    private MongoTemplate mongoTemplate;
    private DataSource dataSource;
    private ActivityProcessor activityProcessor;

	@Bean
    public Job activityTransfer(Step step1, Step step2) {
        return jobBuilderFactory
        		.get("activityTransfer")
        		.start(step1)
        		.next(step2)
        		.build();
    }

    @Bean
    public Step step1(JdbcCursorItemReader<ActivityOra> jdbcReader) {
    	return stepBuilderFactory.get("step1").<ActivityOra, Activity>chunk(200)
        		.reader(jdbcReader)
        		.processor(activityProcessor)
                .writer(mongoWriter())
                .build();
    }
    
    @Bean
    public Step step2(MongoItemReader<Activity> mongoReader) {
        return stepBuilderFactory.get("step2").<Activity, Activity>chunk(200)
        		.reader(mongoReader)
                .writer(jdbcWriter())
                .build();
    }
    
    @Bean
    @StepScope
    public JdbcCursorItemReader<ActivityOra> jdbcReader(@Value("#{jobParameters['param']}") final String jobParam) {
    	JdbcCursorItemReader<ActivityOra> reader = new JdbcCursorItemReader<>();
    	reader.setDataSource(dataSource);
    	reader.setName("activityReader");
        reader.setSql("select * from INSERT_CRM_ACTIVITY where ACTIVITY_ID is null order by SF_ACTIVITY_ID");
        reader.setRowMapper(new RowMapper<ActivityOra>() {
			
			@Override
			public ActivityOra mapRow(ResultSet rs, int rowNum) throws SQLException {
				final ZoneId zone = ZoneId.of("Europe/Istanbul");
				final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(zone));
				final String splitter = ", ";
				
				return ActivityOra.builder()
					.accountId(rs.getLong("ACCOUNT_ID"))
					.activityId(rs.getString("ACTIVITY_ID"))
					.calendarContent(rs.getString("CALENDAR_CONTENT"))
					.calendarEndDate(ZonedDateTime.ofInstant(rs.getTimestamp("CALENDAR_END_DATE", cal).toInstant(), zone))
					.calendarStartDate(ZonedDateTime.ofInstant(rs.getTimestamp("CALENDAR_START_DATE", cal).toInstant(), zone))
					.calendarSubject(rs.getString("CALENDAR_SUBJECT"))
					.companyId(rs.getLong("COMPANY_ID"))
					.createdAt(ZonedDateTime.ofInstant(rs.getTimestamp("CREATED_AT",cal).toInstant(), zone))
					.createdBy(rs.getString("CREATED_BY"))
					.externalParticipants(Optional.ofNullable(rs.getString("EXTERNAL_PARTICIPANTS")).map(t->StringUtils.split(t, splitter)).orElse(new String[] {}))
					.internalParticipants(Optional.ofNullable(rs.getString("INTERNAL_PARTICIPANTS")).map(t->StringUtils.split(t, splitter)).orElse(new String[] {}))
					.organizer(rs.getString("ORGANIZER"))
					.scopeCode(rs.getString("SCOPE_CODE"))
					.serviceAreas(Optional.ofNullable(rs.getString("SERVICE_AREAS")).map(t->StringUtils.split(t, splitter)).orElse(new String[] {}))
					.sfAccountId(rs.getString("SF_ACCOUNT_ID"))
					.sfActivityId(rs.getString("SF_ACTIVITY_ID"))
					.status(rs.getString("STATUS"))
					.toolCode(rs.getString("TOOL_CODE"))
					.typeCode(rs.getString("TYPE_CODE"))
					.jobParam(jobParam)
					.build();
			}
		});
        return reader;
    }

    private MongoItemWriter<Activity> mongoWriter() {
        MongoItemWriter<Activity> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection(Activity.class.getAnnotation(Document.class).collection());
        return writer;
    }
    
    @Bean
    @StepScope
    public MongoItemReader<Activity> mongoReader(@Value("#{jobParameters['param']}") final String jobParam){
    	Map<String, Direction> sorts = new HashMap<>();
        sorts.put("lastUpdated", Sort.Direction.DESC);
    	
    	MongoItemReader<Activity> reader = new MongoItemReader<>();
    	reader.setCollection(Activity.class.getAnnotation(Document.class).collection());
    	reader.setQuery("{ 'sfId': {$exists: true}, 'jobParam': '" + jobParam + "'}");
    	reader.setTemplate(mongoTemplate);
    	reader.setTargetType(Activity.class);
    	reader.setSort(sorts);
    	return reader;
    }
    
    private JdbcBatchItemWriter<Activity> jdbcWriter(){
    	JdbcBatchItemWriter<Activity> writer = new JdbcBatchItemWriter<>();
    	writer.setDataSource(dataSource);
    	writer.setSql("update INSERT_CRM_ACTIVITY set ACTIVITY_ID = ? where SF_ACTIVITY_ID = ?");
    	writer.setItemPreparedStatementSetter(new ItemPreparedStatementSetter<Activity>() {
			
			@Override
			public void setValues(Activity item, PreparedStatement ps) throws SQLException {
				ps.setString(1, item.getId());
				ps.setString(2, item.getSfId());
			}
		});
    	return writer;
    }

}
