package ekol.hibernate5.domain.embeddable;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FixedZoneDateTime.class)
public abstract class FixedZoneDateTime_ {

	public static volatile SingularAttribute<FixedZoneDateTime, LocalDateTime> dateTime;
	public static volatile SingularAttribute<FixedZoneDateTime, String> timeZone;

}

