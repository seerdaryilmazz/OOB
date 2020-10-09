package ekol.hibernate5.domain.embeddable;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DateWindow.class)
public abstract class DateWindow_ {

	public static volatile SingularAttribute<DateWindow, LocalDate> endDate;
	public static volatile SingularAttribute<DateWindow, LocalDate> startDate;

}

