package ekol.kartoteks.event;

import ekol.hibernate5.domain.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "with")
public class CompanyEvent {

	private BaseEntity entity;
	
}
