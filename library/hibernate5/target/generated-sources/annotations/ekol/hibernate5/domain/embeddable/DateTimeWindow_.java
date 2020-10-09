package ekol.hibernate5.domain.embeddable;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DateTimeWindow.class)
public abstract class DateTimeWindow_ {

	public static volatile SingularAttribute<DateTimeWindow, LocalDateTime> start;
	public static volatile SingularAttribute<DateTimeWindow, LocalDateTime> end;

}

