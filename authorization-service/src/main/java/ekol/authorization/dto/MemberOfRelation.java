package ekol.authorization.dto;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.hibernate5.domain.embeddable.DateWindow;

/**
 * Created by ozer on 09/03/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberOfRelation extends RelationWithLevel {

    private DateWindow membershipDateRange;

    public DateWindow getMembershipDateRange() {
        return membershipDateRange;
    }

    public void setMembershipDateRange(DateWindow membershipDateRange) {
        this.membershipDateRange = membershipDateRange;
    }
    
    public static MemberOfRelation fromEntity(ekol.authorization.domain.auth.MemberOfRelation entity) {
    	MemberOfRelation json = new MemberOfRelation();
    	json.setFrom(Node.with(entity.getUser()));
    	json.setTo(Node.with(entity.getEntity()));
    	json.setLevel(Long.valueOf(entity.getLevel()));
    	entity.setStartAndEndDates();
    	json.setMembershipDateRange(Optional.of(entity).map(ekol.authorization.domain.auth.MemberOfRelation::setStartAndEndDates).map(t->new DateWindow(t.getStartDate(), t.getEndDate())).orElse(null));
    	return json;
    }
}
