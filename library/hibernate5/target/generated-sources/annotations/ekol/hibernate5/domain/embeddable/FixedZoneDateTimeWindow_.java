package ekol.hibernate5.domain.embeddable;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FixedZoneDateTimeWindow.class)
public abstract class FixedZoneDateTimeWindow_ {

	public static volatile SingularAttribute<FixedZoneDateTimeWindow, LocalDateTime> start;
	public static volatile SingularAttribute<FixedZoneDateTimeWindow, String> timeZone;
	public static volatile SingularAttribute<FixedZoneDateTimeWindow, LocalDateTime> end;

}

