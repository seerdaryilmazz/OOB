package ekol.authorization.domain.auth;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ekol.authorization.serializer.BaseEntityDeserializer;
import ekol.json.serializers.LocalDateDeserializer;
import ekol.json.serializers.LocalDateSerializer;
import org.neo4j.ogm.annotation.*;

import java.time.LocalDate;

/**
 * Created by kilimci on 03/01/2018.
 */
@RelationshipEntity(type="MEMBER_OF")
public class MemberOfRelation {

    @GraphId
    private Long id;

    @Property
    private Integer level;

    @Property(name = "start_date")
    private Long startDateDays;

    @Property(name = "end_date")
    private Long endDateDays;

    @Transient
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate startDate;

    @Transient
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate endDate;

    @StartNode
    @JsonBackReference
    private AuthUser user;

    @EndNode
    @JsonDeserialize(using = BaseEntityDeserializer.class)
    private BaseEntity entity;

    public MemberOfRelation setStartAndEndDates(){
        if(getStartDateDays() != null){
            this.startDate = LocalDate.ofEpochDay(getStartDateDays());
        }
        if(getEndDateDays() != null){
            this.endDate = LocalDate.ofEpochDay(getEndDateDays());
        }
        return this;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public AuthUser getUser() {
        return user;
    }

    public void setUser(AuthUser user) {
        this.user = user;
    }

    public BaseEntity getEntity() {
        return entity;
    }

    public void setEntity(BaseEntity entity) {
        this.entity = entity;
    }

    public Long getStartDateDays() {
        return startDateDays;
    }

    public void setStartDateDays(Long startDateDays) {
        this.startDateDays = startDateDays;
    }

    public Long getEndDateDays() {
        return endDateDays;
    }

    public void setEndDateDays(Long endDateDays) {
        this.endDateDays = endDateDays;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
